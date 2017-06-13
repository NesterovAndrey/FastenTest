package core.dataProducer;

import core.request.IPartialRequestFactory;
import core.request.IRequest;
import core.mapper.IResultMapper;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

/**
 * Поизводитель данных получаемыъ их базы данных
 * Читает данные частями.
 */
public class SQLDataProducer<T> extends PartialDataProducer<Collection<T>,IRequest<ResultSet>> {


    private static final Integer OFFSET=0;
    private static final Integer LIMIT=1000;

    private static Logger log = Logger.getLogger(SQLDataProducer.class.getName());

    private final DataSource dataSource;
    private final IPartialRequestFactory<Connection,ResultSet> requestFactory;
    private final IResultMapper<Collection<T>,ResultSet> resultMapper;

    public SQLDataProducer(Integer offset,Integer limit,
                           DataSource dataSource,
                           IResultMapper<Collection<T>,ResultSet> resultMapper,
                           IPartialRequestFactory<Connection,ResultSet> requestFactory) throws SQLException {
        super(offset,limit);

        this.dataSource=dataSource;
        this.resultMapper=resultMapper;
        this.requestFactory=requestFactory;
    }
    public SQLDataProducer(DataSource dataSource,
                           IResultMapper<Collection<T>,ResultSet> resultMapper,
                           IPartialRequestFactory<Connection,ResultSet> requestFactory) throws SQLException {
        this(OFFSET,LIMIT,dataSource,resultMapper,requestFactory);
    }

    @Override
    protected Collection<T> rertrieve(IRequest<ResultSet> request) {
            Collection<T> set=null;
                set=request.execute(this.resultMapper);
                request.close();
            if(set.size()<=0)
                complete();

            log.info("PRODUCED "+set.size());
            return set;
    }

    @Override
    protected synchronized IRequest<ResultSet> retrieveNext() {
        IRequest<ResultSet> request=null;
        try {
           request=this.requestFactory.create(dataSource.getConnection(),this.getOffset(),this.getLimit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        moveOffset(getLimit());
        return request;
    }
}
