package core.request.select;

import core.request.IPartialRequestFactory;
import core.request.IRequest;
import core.request.Request;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Properties;

public class SelectAllFactory implements IPartialRequestFactory<Connection,ResultSet> {

    private final String sqlString;
    private final String table;
    public SelectAllFactory(String sqlString,String table)
    {
        this.sqlString=sqlString;
        this.table=table;
    }


    @Override
    public IRequest<ResultSet> create(Connection data, int offset, int limit) {
        return new Request(String.format(this.sqlString,
                this.table,
                limit,
                offset),data);
    }
}
