package core.dataProducer;

/**
 * Абрастный производитель
 * @param <T> типо производимых данных
 */
public abstract class AbstractProducer<T> implements IDataProducer<T> {
    protected abstract void putResult(T result);
    protected abstract T rertrieve();

    public T produce(){
        T result=rertrieve();
        putResult(result);
        return result;
    }
}
