package dataUploadConsumer;

import core.dataConsumer.ConsumerFactory;
import core.dataConsumer.IDataConsumer;
import data.FolderData;
import dataUploadProducer.Folder;

import java.util.Collection;
import java.util.concurrent.ExecutorService;

/**
 * Обрабатывает данные из папок
 * @param <T> Тип данных получаемых из папок
 */
public class FoldersDataConsumer<T> implements IDataConsumer {

    private final Collection<FolderData<T>> folderData;
    private final ExecutorService executorService;
    private final ConsumerFactory<IDataConsumer, Folder> folderNameConsumer;
    private final ConsumerFactory<IDataConsumer,FolderData<T>> folderDataConsumer;

    public FoldersDataConsumer(Collection<FolderData<T>> folderData, ExecutorService executorService, ConsumerFactory<IDataConsumer, Folder> folderNameConsumer, ConsumerFactory<IDataConsumer, FolderData<T>> folderDataConsumer) {
        this.folderData = folderData;
        this.executorService = executorService;
        this.folderNameConsumer = folderNameConsumer;
        this.folderDataConsumer = folderDataConsumer;
    }

    @Override
    public void consume() {
        this.folderData.stream().forEach((folderData)->
        {
                this.executorService.submit(()->
                {
                this.folderNameConsumer.create(folderData.getFolder()).consume();
                this.folderDataConsumer.create(folderData).consume();});
            });

    }
}
