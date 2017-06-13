package dataUploadConsumer;

import core.dataConsumer.ConsumerFactory;
import core.dataConsumer.IDataConsumer;
import core.forkjoin.ForkJoinPerformer;
import data.FolderData;
import dataUploadProducer.Folder;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public class FoldersDataConsumerFactory<T> implements ConsumerFactory<IDataConsumer,Collection<FolderData<T>>> {

    private final ConsumerFactory<IDataConsumer, Folder> folderNameConsumer;
    private final ConsumerFactory<IDataConsumer,FolderData<T>> folderDataConsumer;
    private final ExecutorService executorService;
    public FoldersDataConsumerFactory(ExecutorService executorService,
                                      ConsumerFactory<IDataConsumer, Folder> folderNameConsumer,
                                      ConsumerFactory<IDataConsumer, FolderData<T>> folderDataConsumer) {
        this.executorService=executorService;
        this.folderNameConsumer = folderNameConsumer;
        this.folderDataConsumer = folderDataConsumer;
    }

    @Override
    public IDataConsumer create(Collection<FolderData<T>> data) {
        return new FoldersDataConsumer<>(data,executorService,folderNameConsumer,folderDataConsumer);
    }
}
