package dataUploadConsumer;

import core.dataConsumer.ConsumerFactory;
import core.dataConsumer.IDataConsumer;
import core.mapper.IPathMaker;
import core.mapper.IResultMapper;
import data.FolderData;
import dataUploadProducer.Folder;

import javax.sql.DataSource;

public class SQLDataConsumerFactory<T> implements ConsumerFactory<IDataConsumer,FolderData<T>> {

    private final DataSource dataSource;
    private final IResultMapper<String,T> resultMapper;
    private final IPathMaker<Folder> pathMaker;
    public SQLDataConsumerFactory(DataSource dataSource, IResultMapper<String, T> resultMapper, IPathMaker<Folder> pathMaker) {
        this.dataSource = dataSource;
        this.resultMapper = resultMapper;
        this.pathMaker = pathMaker;
    }

    @Override
    public IDataConsumer create(FolderData<T> data) {
        return new SQLDataConsumer<>(data,this.dataSource,this.resultMapper,pathMaker );
    }
}
