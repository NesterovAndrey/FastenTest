import core.StringConstants;
import core.data.CadastralEntry;
import core.data.DefaultCadastralNumberConverter;
import core.dataConsumer.*;
import core.dataProducer.BlockingQueueDataProducer;
import core.dataProducer.CSVDataProducerFactory;
import core.dataProducer.IDataProducer;
import core.dataProducer.IProducerFactory;
import core.environment.Environment;
import core.forkjoin.ForkJoinWorker;
import core.forkjoin.IForkJoinWorker;
import core.input.FileInputStreamFactory;
import core.input.GzipInputStreamFactory;
import core.mapper.*;
import core.output.GzipOutputStreamFactory;
import core.output.IOutputStreamFactory;
import core.request.CSVDataRequestFactory;
import data.FolderData;
import dataUploadConsumer.FoldersDataConsumerFactory;
import dataUploadConsumer.FolderNameConsumerFactory;
import mapper.FolderToMonthData;
import org.apache.commons.dbcp2.BasicDataSource;
import dataUploadConsumer.SQLDataConsumerFactory;
import dataUploadProducer.CSVFilesDataProducer;
import dataUploadProducer.Folder;
import dataUploadProducer.FolderDataProducerFactory;
import files.IFilesList;
import files.MonthFilesList;
import util.SimpleDateFormatFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Configuration {
    public static int COUNT=0;
    private static SimpleDateFormatFactory simpleDateFormatFactory = simpleDateFormatFactory();

    private static IProducerFactory<Object,IDataProducer<?>> sqlDataProducer;

    //private static IDataConsumer dataConsumer;

    private static DataSource dataSource;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private static BlockingQueue<Collection<FolderData<CadastralEntry>>> queue = new LinkedBlockingQueue<>(10);

    private static ResultWriterFactory<Collection<CadastralEntry>,OutputStream> resultWriterFactory;

    private static ConsumerFactory<IDataConsumer,List<CadastralEntry>> asyncConsumerFactory;

    private static IOutputStreamFactory<String> outputStreamFactory;

    private static InputStream inputStream;

    private static IFilesList<Queue<Folder>> filesList;
    static ExecutorService executorService() {
        return executorService;
    }

    public static SimpleDateFormatFactory getSimpleDateFormatFactory() {
        return simpleDateFormatFactory;
    }

    static IDataProducer<Collection<FolderData<CadastralEntry>>> getDataProducer() {
        return new BlockingQueueDataProducer<Collection<FolderData<CadastralEntry>>>(queue,
                new CSVFilesDataProducer<FolderData<CadastralEntry>>(filesList().getFiles(),
                        new FolderDataProducerFactory<CadastralEntry,FolderData<CadastralEntry>>(
                                new CSVDataProducerFactory<CadastralEntry>(
                                        new CollectionMapper<>(
                                                new CadastralEntryListArraysMapper(simpleDateFormatFactory,
                                                        new DefaultCadastralNumberConverter(":"))),
                                        new CSVDataRequestFactory()),
                                ()->new ForkJoinWorker<Collection<CadastralEntry>,Collection<CadastralEntry>>(ArrayList::new,Collection::addAll),
                                new GzipInputStreamFactory(
                                        new FileInputStreamFactory()),
                                new ResultMapperFactory<FolderData<CadastralEntry>,Collection<CadastralEntry>,Folder>(FolderToMonthData::new), new DefaultPathMaker(
                                        Environment.getEnvironmentPropertie(StringConstants.RESULT_FOLDER),
                                        Environment.getEnvironmentPropertie(StringConstants.FILE_TYPE))),
                        new ForkJoinWorker<>(ArrayList::new,(arr,data)->arr.add(data))),
                Integer.parseInt(Environment.getEnvironmentPropertie(StringConstants.TIMEOUT)));
    }

    static IDataConsumer getDataConsumer() {
        return new BlockingQueueDataConsumer<>(
                queue,
                        new AsyncConsumerFactory<>(
                                executorService(),
                                new FoldersDataConsumerFactory<>(
                                        executorService()/*ForkJoinPerformer::new*/,
                                        new FolderNameConsumerFactory(
                                                dataSource(),(folder)->folder.getFolderName().replaceFirst("-","")),
                                        new SQLDataConsumerFactory<>(
                                                dataSource(),
                                                new FromEntityToValuesString(simpleDateFormatFactory()),
                                                (folder)->folder.getFolderName().replaceFirst("-","")))),
                Integer.parseInt(Environment.getEnvironmentPropertie(StringConstants.TIMEOUT)));
    }

    static DataSource dataSource() {
        return dataSource = dataSource == null ? newDataSource() : dataSource;
    }

    public static InputStream inputStream() {
        return inputStream = inputStream == null ? newInputStream() : inputStream;
    }

    private static DataSource newDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(Environment.getEnvironmentInstance().getPropertie(StringConstants.DRIVER));
        dataSource.setUsername(Environment.getEnvironmentInstance().getPropertie(StringConstants.USER));
        dataSource.setPassword(Environment.getEnvironmentInstance().getPropertie(StringConstants.PASSWORD));
        dataSource.setUrl(Environment.getEnvironmentInstance().getPropertie(StringConstants.DB_CONNECTION_URL));

        return dataSource;

    }

    static SimpleDateFormatFactory simpleDateFormatFactory()
    {
        return new SimpleDateFormatFactory("yyyy-MM-dd");
    }

    static IOutputStreamFactory<String> outputStreamFactory() {
        return outputStreamFactory = outputStreamFactory == null ? newOutputStreamFactory() : outputStreamFactory;
    }

    static IFilesList<Queue<Folder>> filesList()
    {
        return filesList=filesList==null?newFilesList():filesList;
    }

    private static <T> Supplier<IForkJoinWorker<Collection<T>,Collection<T>>> forkJoinWorker()
    {
        return ()->new ForkJoinWorker<>(ArrayList::new, Collection::addAll);
    }

    private static <T> T get(T field,Supplier<T> supplier)
    {
        return field=field==null?supplier.get():field;
    }

    private static IOutputStreamFactory newOutputStreamFactory() {
        return new GzipOutputStreamFactory(outputStreamFactory());
    }

    private static InputStream newInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = new GZIPInputStream(
                    new FileInputStream(String.format("%s/%s",
                            Environment.getEnvironmentPropertie(StringConstants.INPUT_FOLDER),
                            Environment.getEnvironmentPropertie(StringConstants.INPUT_FILE))));//);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
    private static IFilesList<Queue<Folder>> newFilesList() {
        return new MonthFilesList(Environment.getEnvironmentPropertie(StringConstants.INPUT_FOLDER),
                Pattern.compile("(\\d{4})-(\\d{1})"));
    }

}
