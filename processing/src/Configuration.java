import core.StringConstants;
import core.data.CadastralEntry;
import core.data.DefaultCadastralNumberConverter;
import core.dataConsumer.BlockingQueueDataConsumer;
import core.dataConsumer.CSVResultWriterFactory;
import core.dataConsumer.DefaultConsumerFactory;
import core.dataConsumer.IDataConsumer;
import core.dataProducer.BlockingQueueDataProducer;
import core.dataProducer.CSVDataProducer;
import core.dataProducer.IDataProducer;
import core.environment.Environment;
import core.forkjoin.ForkJoinPerformer;
import core.mapper.*;
import core.output.*;
import core.request.CSVDataRequestFactory;
import org.apache.commons.dbcp2.BasicDataSource;
import consumer.EntityListConsumerFactory;
import util.SimpleDateFormatFactory;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPInputStream;

public class Configuration {

    public static int CONST=0;
    public static int MULTISUM=0;
    private static SimpleDateFormatFactory simpleDateFormatFactory = new SimpleDateFormatFactory("yyyy-MM-dd");

    private static IDataProducer<Collection<CadastralEntry>> sqlDataProducer;

    private static IDataConsumer dataConsumer;

    private static DataSource dataSource;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private static BlockingQueue<Collection<CadastralEntry>> queue = new LinkedBlockingQueue<>(30);

    private static IOutputStreamFactory outputStreamFactory;

    private static IOutputStreamFactory<String> fileOutputStreamFactory;

    private static InputStream inputStream;


    public static ExecutorService executorService() {
        return executorService;
    }

    public static SimpleDateFormatFactory getSimpleDateFormatFactory() {
        return simpleDateFormatFactory;
    }

    public static IDataProducer<Collection<CadastralEntry>> getDataProducer() {
        return sqlDataProducer = sqlDataProducer == null ? newDataProducer() : sqlDataProducer;
    }

    public static IDataConsumer getDataConsumer() {
        return dataConsumer = dataConsumer == null ? newDataConsumer() : dataConsumer;
    }

    public static DataSource dataSource() {
        return dataSource = dataSource == null ? newDataSource() : dataSource();
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
    public static SimpleDateFormatFactory simpleDateFormatFactory()
    {
        return simpleDateFormatFactory;
    }

    public static <T> IOutputStreamFactory<T> outputStreamFactory() {
        return outputStreamFactory = outputStreamFactory == null ? newOutputStreamFactory() : outputStreamFactory;
    }
    public static IOutputStreamFactory<String> fileOutputStreamFactory()
    {
        return fileOutputStreamFactory=fileOutputStreamFactory==null?newFileOutputStream(): fileOutputStreamFactory;
    }
    private static IDataConsumer newDataConsumer() {
        return new BlockingQueueDataConsumer<>(queue,
                        new EntityListConsumerFactory<>(
                                new DefaultConsumerFactory<>(
                                        new MapValueWriterFactory<>(
                                                new CSVResultWriterFactory<>(","
                                                        ,simpleDateFormatFactory()
                                                        ,new CollectionMapper<>(
                                                                new CadastralEntryToStringArr(simpleDateFormatFactory()))))
                                        ,outputStreamFactory()),
                                new CollectionToPathMap<>(
                                        new DefaultPathExtractor(
                                                Environment.getEnvironmentPropertie(StringConstants.RESULT_FOLDER),
                                Environment.getEnvironmentPropertie(StringConstants.FILE_TYPE)))
                                , ForkJoinPerformer::new)
                ,Integer.parseInt(Environment.getEnvironmentPropertie(StringConstants.TIMEOUT)));
    }

    private static IDataProducer<Collection<CadastralEntry>> newDataProducer() {
       return new BlockingQueueDataProducer<>(queue,
               new CSVDataProducer<>(
                       new CollectionMapper<>(
                               new CadastralEntryListArraysMapper(simpleDateFormatFactory,
                                       new DefaultCadastralNumberConverter(":"))),
                       new CSVDataRequestFactory(),
                       inputStream()),
               Integer.parseInt(Environment.getEnvironmentPropertie(StringConstants.TIMEOUT)) );

    }

    private static IOutputStreamFactory<Map.Entry<String,?>> newOutputStreamFactory() {
        return new MapKeyOutputStreamFactory(
                new GzipOutputStreamFactory(fileOutputStreamFactory()));
    }

    private static InputStream newInputStream() {
        InputStream inputStream = null;
        try {
            inputStream = new GZIPInputStream(
                    new FileInputStream(String.format("%s/%s",
                            Environment.getEnvironmentPropertie(StringConstants.INPUT_FOLDER),
                            Environment.getEnvironmentPropertie(StringConstants.INPUT_FILE))));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    private static IOutputStreamFactory<String> newFileOutputStream()
    {
        return new FileOutpuStreamFactory();
    }


}
