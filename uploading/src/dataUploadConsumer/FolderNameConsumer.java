package dataUploadConsumer;

import core.StringConstants;
import core.dataConsumer.DefaultConsumer;
import core.dataConsumer.IDataConsumer;
import core.mapper.IPathMaker;
import core.sql.SQLStrings;
import dataUploadProducer.Folder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Создаёт новую таблицу в базе если необходимо
 */
public class FolderNameConsumer implements IDataConsumer {
    private static Logger log = Logger.getLogger(DefaultConsumer.class.getName());

    private final DataSource dataSource;
    private final Folder folder;
    private final IPathMaker<Folder> pathMaker;
    public FolderNameConsumer(DataSource dataSource, Folder folder, IPathMaker<Folder> pathMaker) {
        this.dataSource = dataSource;
        this.folder = folder;
        this.pathMaker = pathMaker;
    }

    @Override
    public void consume() {
        try {
            log.info(String.format("CREATE TABLE %s IF NOT EXISTS ",this.folder.getFolderName()));
                String sql=null;
                Connection connection = dataSource.getConnection();
                sql = String.format(SQLStrings.CREATE_TABLE, this.pathMaker.make(this.folder));
                connection.createStatement().executeUpdate(sql);
                connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
