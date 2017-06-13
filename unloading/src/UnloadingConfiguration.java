
import core.StringConstants;
import core.data.CadastralEntry;
import core.data.DefaultCadastralNumberConverter;
import core.dataConsumer.*;
import core.dataProducer.BlockingQueueDataProducer;
import core.dataProducer.IDataProducer;
import core.dataProducer.SQLDataProducer;
import core.environment.Environment;
import core.mapper.CadastralEntrySetMapper;
import core.mapper.CadastralEntryToStringArr;
import core.mapper.CollectionMapper;
import core.request.IPartialRequestFactory;
import core.request.select.*;
import core.sql.SQLStrings;
import org.apache.commons.dbcp2.BasicDataSource;
import util.SimpleDateFormatFactory;

import javax.sql.DataSource;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.zip.GZIPOutputStream;

public class UnloadingConfiguration {
    private static SimpleDateFormatFactory simpleDateFormatFactory=new SimpleDateFormatFactory("yyyy-MM-dd");

    private static IDataProducer<Collection<CadastralEntry>> sqlDataProducer;

    private static IDataConsumer dataConsumer;

    private static DataSource dataSource;

    private static ExecutorService executorService= Executors.newCachedThreadPool();

    private static BlockingQueue<Collection<CadastralEntry>> queue=new LinkedBlockingQueue<>(10000);

    private static ResultWriterFactory<Collection<CadastralEntry>,OutputStream> resultWriterFactory;

    private static OutputStream outPutStream;

    private static Map<String,IPartialRequestFactory<Connection,ResultSet>> requestFactoryMap;

    public static ExecutorService executorService()
    {
        return executorService;
    }

    public static SimpleDateFormatFactory simpleDateFormatFactory()
    {
        return simpleDateFormatFactory;
    }

    public static IDataProducer<Collection<CadastralEntry>> getSqlDataProducer()
    {
        return sqlDataProducer=sqlDataProducer==null?newSqlDataProducer():sqlDataProducer;
    }

    public static IDataConsumer getDataConsumer()
    {
        return dataConsumer=dataConsumer==null?newDataConsumer():dataConsumer;
    }
    public static Map<String,IPartialRequestFactory<Connection,ResultSet>> requestFactoryMap()
    {
        return requestFactoryMap=requestFactoryMap==null?newRequestFactoryMap():requestFactoryMap;
    }
    public static DataSource dataSource()
    {
        return dataSource=dataSource==null?newDataSource():dataSource();
    }

    private static DataSource newDataSource()
    {
        BasicDataSource dataSource=new BasicDataSource();

        dataSource.setDriverClassName(Environment.getEnvironmentInstance().getPropertie(StringConstants.DRIVER));
        dataSource.setUsername(Environment.getEnvironmentInstance().getPropertie(StringConstants.USER));
        dataSource.setPassword(Environment.getEnvironmentInstance().getPropertie(StringConstants.PASSWORD));
        dataSource.setUrl(Environment.getEnvironmentInstance().getPropertie(StringConstants.DB_CONNECTION_URL));

        return dataSource;
    }

    private static Map<String,IPartialRequestFactory<Connection,ResultSet>> newRequestFactoryMap()
    {
        Map<String,IPartialRequestFactory<Connection,ResultSet>> requestFactoryMap=new HashMap<>();
        requestFactoryMap.put(StringConstants.SELECT_ALL_REQUEST,
                new SelectAllFactory(SQLStrings.SELECT_ALL, Environment.getEnvironmentPropertie(StringConstants.DB_TABLE)));

        requestFactoryMap.put(StringConstants.SELECT_RANGE,
                new SelectAllByRange(SQLStrings.SELECT_ALL_BY_RANGE, Environment.getEnvironmentPropertie(StringConstants.DB_TABLE),
                        Environment.getEnvironmentPropertie(StringConstants.FROM),
                        Environment.getEnvironmentPropertie(StringConstants.TO)));

        requestFactoryMap.put(StringConstants.SELECT_BY_DATE,
                new SelectAllByDate(SQLStrings.SELECT_ALL_BY_DAY,
                        Environment.getEnvironmentPropertie(StringConstants.DB_TABLE),
                                Environment.getEnvironmentPropertie(StringConstants.DATE)));

        requestFactoryMap.put(StringConstants.SELECT_BY_MONTH,
                new SelectAllByMonthFactory(SQLStrings.SELECT_ALL_BY_MONTH,
                        Environment.getEnvironmentPropertie(StringConstants.DB_TABLE),
                        Environment.getEnvironmentPropertie(StringConstants.YEAR),
                        Environment.getEnvironmentPropertie(StringConstants.MONTH)));

        requestFactoryMap.put(StringConstants.SELECT_BY_YEAR,
                new SelectAllByYear(SQLStrings.SELECT_ALL_BY_YEAR,
                        Environment.getEnvironmentPropertie(StringConstants.DB_TABLE),
                        Environment.getEnvironmentPropertie(StringConstants.YEAR)));


        return requestFactoryMap;
    }
    public static ResultWriterFactory<Collection<CadastralEntry>,OutputStream> resultWriterFactory()
    {
        return resultWriterFactory=resultWriterFactory==null?newResultWriterFactory():resultWriterFactory;
    }

    public static OutputStream outputStream()
    {
        return outPutStream = outPutStream ==null? newOutputStream(): outPutStream;
    }

    private static IDataConsumer newDataConsumer()
    {
        return new BlockingQueueDataConsumer<>(queue,
                new DefaultConsumerFactory<>(resultWriterFactory(),
                        (data)-> newOutputStream()),
                Integer.parseInt(Environment.getEnvironmentPropertie(StringConstants.TIMEOUT)));
    }
    private static IDataProducer<Collection<CadastralEntry>> newSqlDataProducer()
    {
        IDataProducer<Collection<CadastralEntry>> dataProducer=null;
        try {
            dataProducer=new BlockingQueueDataProducer<>(queue,
                    new SQLDataProducer<>(dataSource(),
                    new CadastralEntrySetMapper(
                            simpleDateFormatFactory(),
                            new DefaultCadastralNumberConverter(":")),
                    requestFactoryMap().get(Environment.<String>getEnvironmentPropertie(StringConstants.REQUEST))),
                    Integer.parseInt(Environment.getEnvironmentPropertie(StringConstants.TIMEOUT)));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataProducer;
    }
    private static ResultWriterFactory<Collection<CadastralEntry>,OutputStream> newResultWriterFactory()
    {
        return new CSVResultWriterFactory<>(",", simpleDateFormatFactory,new CollectionMapper<>(
                new CadastralEntryToStringArr(simpleDateFormatFactory())));
    }
    private static OutputStream newOutputStream()
    {
        OutputStream outputStream=null;
        try {
            outputStream=new GZIPOutputStream(
                    new FileOutputStream(String.format("%s/%s",
                            Environment.getEnvironmentPropertie(StringConstants.RESULT_FOLDER),
                            Environment.getEnvironmentPropertie(StringConstants.RESULT_FILE)),true),true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }


}
