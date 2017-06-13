package core.request;

import core.mapper.IResultMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Request implements IRequest<ResultSet> {

    private final Connection connection;
    private final String sqlString;
    public Request(String sqlString,Connection connection)
    {
        this.sqlString=sqlString;
        this.connection=connection;
    }

    @Override
    public ResultSet execute() {
        ResultSet resultSet=null;
        try {
            resultSet=connection.createStatement().executeQuery(sqlString);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    @Override
    public <T> T execute(IResultMapper<T,ResultSet> mapper) {
        return mapper.map(execute());
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
