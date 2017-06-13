package core.request.select;

import core.request.IPartialRequestFactory;
import core.request.IRequest;
import core.request.Request;

import java.sql.Connection;
import java.sql.ResultSet;

public class SelectAllByMonth implements IPartialRequestFactory<Connection, ResultSet> {
    private final String sqlString;
    private final String table;
    private final String date;

    public SelectAllByMonth(String sqlString, String table, String date) {
        this.sqlString = sqlString;
        this.table = table;
        this.date = date;
    }

    @Override
    public IRequest<ResultSet> create(Connection data, int offset, int limit) {
        return new Request(String.format(this.sqlString,
                this.table,
                limit,
                offset, date, data), data);
    }
}
