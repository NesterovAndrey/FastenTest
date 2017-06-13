package files;

import dataUploadProducer.Folder;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MonthFilesList implements IFilesList<Queue<Folder>> {

    private final String folder;
    private final Pattern pattern;
    public MonthFilesList(String folder,Pattern pattern) {
        this.folder = folder;
        this.pattern=pattern;
    }

    @Override
    public Queue<Folder> getFiles() {
        Queue<Folder> folders = new ConcurrentLinkedQueue<>();
        File resFolder = Paths.get(this.folder).toFile();

        Arrays.stream(resFolder.listFiles()).forEach((folder) ->
        {
            String foldeName = folder.getName().replace(folder.getParent(), "");
            if (folder.isDirectory() && pattern.matcher(foldeName).find()) {
                folders.add(new Folder(folder.getName(), Arrays.stream(folder.listFiles()).filter((file) ->
                {
                    return !file.isDirectory();
                }).map((f) ->
                {
                    return f.getName().substring(0, f.getName().lastIndexOf(".csv"));
                }).collect(Collectors.toList())));
            }
        });
        return folders;
    }
}
