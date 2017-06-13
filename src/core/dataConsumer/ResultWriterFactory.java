package core.dataConsumer;

public interface ResultWriterFactory<T,C>{
    ResultWriter create(T data,C source);
}
