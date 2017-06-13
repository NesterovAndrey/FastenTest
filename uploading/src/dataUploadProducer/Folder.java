package dataUploadProducer;

import java.util.Collections;
import java.util.List;

public class Folder {
    private final List<String> files;
    private final String folderName;

    public Folder(String folderName,List<String> files) {
        this.files = Collections.unmodifiableList(files);
        this.folderName=folderName;
    }

    public String getFolderName() {
        return folderName;
    }

    public List<String> getFiles() {
        return files;
    }
}
