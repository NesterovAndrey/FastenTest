package core.dataConsumer;

import core.dataProducer.PartialDataProducer;
import core.output.IOutputStreamFactory;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * Простой потребитель данных. Записывает данные с помощью ResultWriter
 * @param <T> Тип потребляемых данных
 */
public class DefaultConsumer<T> implements IDataConsumer {

    private final T data;
    private final ResultWriterFactory<T,OutputStream> resultWriterFactory;
    private final IOutputStreamFactory<T> outputStreamFactory;
    public DefaultConsumer(T data,
                           ResultWriterFactory<T,OutputStream> resultWriterFactory,
                           IOutputStreamFactory<T> outputStreamFactory) {
        this.resultWriterFactory=resultWriterFactory;
        this.outputStreamFactory=outputStreamFactory;
        this.data=data;
    }

    public void consume() {
            try {
                OutputStream outputStream = this.outputStreamFactory.create(data);
                ResultWriter resultWriter = this.resultWriterFactory.create(data, outputStream);
                resultWriter.write();
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
