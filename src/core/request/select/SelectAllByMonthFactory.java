package core.request.select;

import core.request.IPartialRequestFactory;
import core.request.IRequest;
import core.request.IRequestFactory;
import core.request.Request;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Properties;

public class SelectAllByMonthFactory implements IPartialRequestFactory<Connection,ResultSet> {
    private final String sqlString;
    private final String table;
    private final String year;
    private final String month;

    public SelectAllByMonthFactory(String sqlString, String table, String year,String month)
    {
        this.sqlString=sqlString;
        this.table=table;
        this.year=year;
        this.month=month;
    }

    @Override
    public IRequest<ResultSet> create(Connection data, int offset, int limit) {
        return new Request(String.format(this.sqlString,
                this.table,
                this.year,this.month,limit,offset),data);
    }
}
