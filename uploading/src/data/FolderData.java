package data;

import dataUploadProducer.Folder;

import java.util.Collection;
import java.util.Collections;

public class FolderData<T> {
    private final Folder folder;
    private final Collection<T> data;

    public FolderData(Folder folder, Collection<T> data) {
        this.folder=folder;
        this.data = Collections.unmodifiableCollection(data);
    }

    public Folder getFolder() {
        return folder;
    }

    public Collection<T> getData() {
        return data;
    }
}
