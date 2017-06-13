package consumer;

import core.dataConsumer.ConsumerFactory;
import core.dataConsumer.IDataConsumer;
import core.forkjoin.IForkJoinPerformer;
import core.mapper.IResultMapper;

import java.util.Collection;
import java.util.Map;

/**
 * Потребитель который с помозью маппера разбивает данные на части. И выполняет каждую часть отедльно.
 * @param <T> Тип потребляемых данных
 */
//Не стал запускать DataListConsumer параллельно иначе получаются битые архивы.
public class DataListConsumer<T> implements IDataConsumer {

    private final Collection<T> cadastralEntries;
    private final ConsumerFactory<IDataConsumer,Map.Entry<String,Collection<T>>> consumerFactory;
    private final IResultMapper<Map<String,Collection<T>>,Collection<T>> mapper;
    private final IForkJoinPerformer forkJoinPerformer;

    public DataListConsumer(Collection<T> data, ConsumerFactory<IDataConsumer, Map.Entry<String, Collection<T>>> consumerFactory,
                            IResultMapper<Map<String, Collection<T>>, Collection<T>> mapper, IForkJoinPerformer forkJoinPerformer) {
        this.cadastralEntries = data;
        this.consumerFactory = consumerFactory;
        this.mapper=mapper;
        this.forkJoinPerformer = forkJoinPerformer;
    }

    @Override
    public void consume() {
        //разбиваем данных на части
        Map<String,Collection<T>> result=this.mapper.map(this.cadastralEntries);
        result.entrySet().forEach((entry)->
        {
            //Выполняем кажду часть параллельно
            forkJoinPerformer.submitTask(()-> consumerFactory.create(entry).consume());
        });
        //ждём выполнения
        forkJoinPerformer.join();
    }

}
