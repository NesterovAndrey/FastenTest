package core.dataConsumer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Потребитель данных извлекающий их из блокирующей очереди
 * @param <T> Тип потреблямых данных
 */
public class BlockingQueueDataConsumer<T> implements IDataConsumer {

    private final BlockingQueue<T> queue;
    private final ConsumerFactory<IDataConsumer, T> consumerFactory;
    private final int timeout;
    public BlockingQueueDataConsumer(BlockingQueue<T> queue, ConsumerFactory<IDataConsumer, T> consumerFactory, int timeout) {
        this.queue = queue;
        this.consumerFactory = consumerFactory;
        this.timeout = timeout;
    }

    @Override
    public void consume() {
        T result = null;
        try {
            while ((result = queue.poll(timeout, TimeUnit.SECONDS)) != null)
            {
                this.consumerFactory.create(result).consume();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
