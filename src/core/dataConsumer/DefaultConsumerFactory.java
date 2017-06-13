package core.dataConsumer;

import core.output.IOutputStreamFactory;

import java.io.OutputStream;

public class DefaultConsumerFactory<T> implements ConsumerFactory<IDataConsumer,T> {
    private final ResultWriterFactory<T,OutputStream> resultWriterFactory;
    private final IOutputStreamFactory<T> outputStreamFactory;

    public DefaultConsumerFactory(ResultWriterFactory<T, OutputStream> resultWriterFactory, IOutputStreamFactory<T> outputStreamFactory) {
        this.resultWriterFactory = resultWriterFactory;
        this.outputStreamFactory = outputStreamFactory;
    }

    @Override
    public IDataConsumer create(T data) {
        return new DefaultConsumer<>(data,resultWriterFactory,outputStreamFactory);
    }
}
