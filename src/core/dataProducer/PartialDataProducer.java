package core.dataProducer;

/**
 * Произоводитель данных возвращающий результат частями
 * @param <T> результат
 * @param <C> Тип данных для извлечения следующей порции
 */
public abstract class PartialDataProducer<T,C> implements IDataProducer<T> {
    private volatile Integer offset=0;
    private final Integer limit;
    private boolean completed=false;


    protected void putResult(T result){
        if(!isCompleted())
        {
           put(result);
        }
    }
    protected void put(T result)
    {

    }

    protected void complete()
    {
        completed=true;
    }

    protected abstract T rertrieve(C data);

    protected void moveOffset(int count)
    {
        this.offset+=count;
    }

    protected C next()
    {
        C next=retrieveNext();
        return next;
    }

    /**
     * Возвращает данных для извлечения следующей порции
     * @return
     */
    protected abstract C retrieveNext();

    public PartialDataProducer(Integer offset, Integer limit) {
        this.offset=offset;
        this.limit=limit;
    }
    @Override
    public T produce(){
        T result=rertrieve(next());
        putResult(result);
        return result;
    }
    public boolean isCompleted()
    {
        return this.completed;
    }

    public Integer getOffset() {
        return offset;
    }

    public Integer getLimit() {
        return limit;
    }

}
