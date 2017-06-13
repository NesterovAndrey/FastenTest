package core.output;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipOutputStreamFactory implements IOutputStreamFactory<String> {

    private final IOutputStreamFactory<String> outputStreamFactory;

    public GzipOutputStreamFactory(IOutputStreamFactory<String> outputStreamFactory) {
        this.outputStreamFactory = outputStreamFactory;
    }
    @Override
    public OutputStream create(String data) {
        OutputStream outputStream=this.outputStreamFactory.create(data);
        try {
            outputStream=new GZIPOutputStream(outputStream,true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }
}
