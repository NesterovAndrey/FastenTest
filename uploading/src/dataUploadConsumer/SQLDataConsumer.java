package dataUploadConsumer;

import core.dataConsumer.DefaultConsumer;
import core.dataConsumer.IDataConsumer;
import core.mapper.IPathMaker;
import core.mapper.IResultMapper;
import core.sql.SQLStrings;
import data.FolderData;
import dataUploadProducer.Folder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Плохой класс для загрузки данных в базу
 * @param <T> Объекты записываемые в базу
 */
//Не очень хороший класс. Создаёт запрос из коллекции полученных данных.
public class SQLDataConsumer<T> implements IDataConsumer {
    private static Logger log = Logger.getLogger(DefaultConsumer.class.getName());

    private final FolderData<T> folderData;
    private final DataSource dataSource;
    private final IResultMapper<String,T> resultMapper;
    private final IPathMaker<Folder> pathMaker;
    private static int sum=0;
    public SQLDataConsumer(FolderData<T> folderData, DataSource dataSource, IResultMapper<String, T> resultMapper, IPathMaker<Folder> pathMaker) {
        this.folderData = folderData;
        this.dataSource = dataSource;
        this.resultMapper=resultMapper;
        this.pathMaker = pathMaker;
    }
    @Override
    public void consume() {
        String sql=null;
        try {
            if(this.folderData.getData().size()>0) {
                log.info(String.format("UPLOAD to %s %d",this.folderData.getFolder().getFolderName(),this.folderData.getData().size()));
                Connection connection = dataSource.getConnection();
                sql = String.format(SQLStrings.INSERT_DATA, this.pathMaker.make(folderData.getFolder()), values(this.folderData.getData()));
                connection.createStatement().executeUpdate(sql);
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private String values(Collection<T> entrySet) {
        StringBuilder stringBuilder = new StringBuilder();
        entrySet.stream().forEach((entry ->
        {
           String res=this.resultMapper.map(entry);
           stringBuilder.append(String.format(SQLStrings.VALUE_SQL,res));

        }));
        int coma=stringBuilder.lastIndexOf(",");

        return stringBuilder.replace(coma,coma+1,";").toString();
    }


}
