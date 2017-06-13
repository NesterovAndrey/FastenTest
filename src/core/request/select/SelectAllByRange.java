package core.request.select;

import core.request.IPartialRequestFactory;
import core.request.IRequest;
import core.request.Request;

import java.sql.Connection;
import java.sql.ResultSet;
public class SelectAllByRange implements IPartialRequestFactory<Connection,ResultSet> {
    private final String sqlString;
    private final String table;
    private final String from;
    private final String to;

    public SelectAllByRange(String sqlString, String table, String from, String to) {
        this.sqlString = sqlString;
        this.table = table;
        this.from = from;
        this.to = to;
    }

    @Override
    public IRequest<ResultSet> create(Connection data, int offset, int limit) {
        return new Request(String.format(this.sqlString,
                this.table,
                this.from,this.to,limit,offset),data);
    }
}
