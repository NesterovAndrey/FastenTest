package core.output;

import core.dataConsumer.ResultWriter;
import core.dataConsumer.ResultWriterFactory;
import java.io.OutputStream;
import java.util.Collection;
import java.util.Map;

public class MapValueWriterFactory<T,C> implements ResultWriterFactory<Map.Entry<C,  Collection<T>>,OutputStream> {

    private final ResultWriterFactory<Collection<T>,OutputStream> writerFactory;

    public MapValueWriterFactory(ResultWriterFactory<Collection<T>, OutputStream> writerFactory) {
        this.writerFactory = writerFactory;
    }

    @Override
    public ResultWriter create(Map.Entry<C, Collection<T>> data, OutputStream source) {
        return this.writerFactory.create(data.getValue(),source);
    }
}
