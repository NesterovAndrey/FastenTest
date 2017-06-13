package core.dataProducer;

@FunctionalInterface
public interface IProducerFactory<T,C> {
    C create(T source);
}
