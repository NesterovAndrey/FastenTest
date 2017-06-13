package core.dataConsumer;

import java.util.concurrent.ExecutorService;

/**
 * Обработчик запускающий обработку полученных данных в новом потоке
 * @param <T> Тип получаемых данных
 */
public class AsyncConsumer<T> implements IDataConsumer {
    private final ExecutorService executorService;
    private final ConsumerFactory<IDataConsumer,T> consumerFactory;
    private final T data;
    public AsyncConsumer(ExecutorService executorService, T data, ConsumerFactory<IDataConsumer, T> consumerFactory) {
        this.executorService = executorService;
        this.data = data;
        this.consumerFactory = consumerFactory;
    }
    @Override
    public void consume() {
        executorService.submit(()->consumerFactory.create(this.data).consume());
    }
}
