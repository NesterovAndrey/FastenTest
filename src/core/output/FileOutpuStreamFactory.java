package core.output;

import java.io.*;

public class FileOutpuStreamFactory implements IOutputStreamFactory<String> {

    @Override
    public OutputStream create(String data){
        OutputStream outputStream=null;
        File file = new File(data);
        file.getParentFile().mkdirs();
        try {
            outputStream=new FileOutputStream(data,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return outputStream;
    }
}
