package mapper;

import data.FolderData;
import core.mapper.IResultMapper;
import dataUploadProducer.Folder;

import java.util.Collection;

public class FolderToMonthData<T> implements IResultMapper<FolderData<T>,Collection<T>> {

    private final Folder folder;

    public FolderToMonthData(Folder folder) {
        this.folder = folder;
    }

    @Override
    public FolderData<T> map(Collection<T> result) {
        return new FolderData<>(folder,result);
    }
}
