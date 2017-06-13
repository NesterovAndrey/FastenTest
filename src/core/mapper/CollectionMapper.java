package core.mapper;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Маппер коллекции данных
 * @param <T> Тип данных результирующей коллекции
 * @param <C> Тип данных исходной коллекции
 */
public class CollectionMapper<T,C> implements IResultMapper<Collection<T>,Collection<C>> {

    private final IResultMapper<T,C> resultMapper;

    public CollectionMapper(IResultMapper<T, C> resultMapper) {
        this.resultMapper = resultMapper;
    }

    @Override
    public Collection<T> map(Collection<C> result) {
        return result.stream().map(entry->resultMapper.map(entry)).collect(Collectors.toList());
    }
}
