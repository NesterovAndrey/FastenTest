package core.output;

import java.io.OutputStream;
import java.util.Map;

public class MapKeyOutputStreamFactory implements IOutputStreamFactory<Map.Entry<String,?>> {

    private final IOutputStreamFactory<String> outputStreamFactory;

    public MapKeyOutputStreamFactory(IOutputStreamFactory<String> outputStreamFactory) {
        this.outputStreamFactory = outputStreamFactory;
    }

    @Override
    public OutputStream create(Map.Entry<String,?> data) {
        return this.outputStreamFactory.create(data.getKey());
    }
}
