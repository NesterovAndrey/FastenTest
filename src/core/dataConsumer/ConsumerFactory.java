package core.dataConsumer;

public interface ConsumerFactory<T,C> {
    T create(C data);
}
