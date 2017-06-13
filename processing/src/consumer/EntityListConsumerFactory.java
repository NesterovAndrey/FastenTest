package consumer;

import core.dataConsumer.ConsumerFactory;
import core.dataConsumer.IDataConsumer;
import core.forkjoin.IForkJoinPerformer;
import core.mapper.IResultMapper;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

public class EntityListConsumerFactory<T> implements ConsumerFactory<IDataConsumer,Collection<T>> {

    private final ConsumerFactory<IDataConsumer,Map.Entry<String,Collection<T>>> consumerFactory;
    private final IResultMapper<Map<String,Collection<T>>,Collection<T>> mapper;
    private final Supplier<IForkJoinPerformer> forkJoinPerformerSupplier;

    public EntityListConsumerFactory(ConsumerFactory<IDataConsumer, Map.Entry<String, Collection<T>>> consumerFactory,
                                     IResultMapper<Map<String, Collection<T>>, Collection<T>> mapper, Supplier<IForkJoinPerformer> forkJoinPerformerSupplier) {
        this.consumerFactory = consumerFactory;
        this.mapper=mapper;
        this.forkJoinPerformerSupplier = forkJoinPerformerSupplier;
    }

    @Override
    public IDataConsumer create(Collection<T> data) {
        return new DataListConsumer<T>(data,this.consumerFactory,this.mapper, forkJoinPerformerSupplier.get());
    }
}
