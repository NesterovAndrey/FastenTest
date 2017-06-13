package dataUploadConsumer;

import core.dataConsumer.ConsumerFactory;
import core.dataConsumer.IDataConsumer;
import core.mapper.IPathMaker;
import dataUploadProducer.Folder;

import javax.sql.DataSource;

public class FolderNameConsumerFactory implements ConsumerFactory<IDataConsumer, Folder> {

    private final DataSource dataSource;
    private final IPathMaker<Folder> pathMaker;

    public FolderNameConsumerFactory(DataSource dataSource, IPathMaker<Folder> pathMaker) {
        this.dataSource = dataSource;
        this.pathMaker = pathMaker;
    }

    @Override
    public IDataConsumer create(Folder data) {
        return new FolderNameConsumer(dataSource,data, pathMaker);
    }
}
