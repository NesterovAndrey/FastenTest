package core.dataConsumer;

import java.util.concurrent.ExecutorService;

public class AsyncConsumerFactory<T> implements ConsumerFactory<IDataConsumer,T> {

    private final ExecutorService executorService;
    private final ConsumerFactory<IDataConsumer,T> consumerFactory;

    public AsyncConsumerFactory(ExecutorService executorService,ConsumerFactory<IDataConsumer, T> consumerFactory) {
        this.executorService = executorService;
        this.consumerFactory = consumerFactory;
    }

    @Override
    public IDataConsumer create(T data) {
        return new AsyncConsumer<>(this.executorService,data,this.consumerFactory);
    }
}
