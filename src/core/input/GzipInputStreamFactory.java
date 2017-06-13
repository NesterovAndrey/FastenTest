package core.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class GzipInputStreamFactory implements IInputStreamFactory<String> {

    private final IInputStreamFactory<String> inputStreamFactory;

    public GzipInputStreamFactory(IInputStreamFactory<String> inputStreamFactory) {
        this.inputStreamFactory = inputStreamFactory;
    }

    @Override
    public InputStream create(String data) {
        InputStream inputStream=null;
        try {
            inputStream=new GZIPInputStream(inputStreamFactory.create(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
