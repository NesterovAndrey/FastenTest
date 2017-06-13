package core.request;

import java.sql.Connection;
import java.util.Properties;

public interface IRequestFactory {
    IRequest create(Connection connection, Properties properties);
}
