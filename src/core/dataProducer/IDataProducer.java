package core.dataProducer;

public interface IDataProducer<T> {
    T produce();
    boolean isCompleted();
}
