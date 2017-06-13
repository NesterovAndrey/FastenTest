package core.dataProducer;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;


/**
 * Производитель данных. Складывает получаенные данные в блокирующую очередь
 * @param <T> Тип производимых данных
 */
public class BlockingQueueDataProducer<T> extends AbstractProducer<T>{

    private final BlockingQueue<T> queue;
    private final IDataProducer<T> producer;
    private final int timeout;
    public BlockingQueueDataProducer(BlockingQueue<T> queue, IDataProducer<T> producer, int timeout) {
        this.queue = queue;
        this.producer=producer;
        this.timeout = timeout;
    }


    @Override
    protected void putResult(T result) {
        if(!isCompleted()) {
            try {
                this.queue.offer(result, timeout, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected T rertrieve() {
        return this.producer.produce();
    }

    @Override
    public boolean isCompleted() {
        return this.producer.isCompleted();
    }
}
