package core.input;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Paths;

public class FileInputStreamFactory implements IInputStreamFactory<String> {
    @Override
    public InputStream create(String data) {
        InputStream inputStream=null;
        try {
            File file=Paths.get(data).toFile();
            inputStream=new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
