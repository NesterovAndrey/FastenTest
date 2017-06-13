package core.mapper;

import java.util.*;

/**
 * Преобразует коллекцию в Map в качестве ключа испузуется путь
 * @param <T>
 */
public class CollectionToPathMap<T> implements IResultMapper<Map<String,Collection<T>>,Collection<T>>{

    private final IPathExtractor<T> pathExtractor;

    public CollectionToPathMap(IPathExtractor<T> pathExtractor) {

        this.pathExtractor = pathExtractor;
    }
    @Override
    public Map<String, Collection<T>> map(Collection<T> data) {
        Map<String,Collection<T>> result=new HashMap<>();
        data.stream().forEach((i)->
        {
            String nextPaht=this.pathExtractor.getPath(i);
            Collection<T> queue=result.getOrDefault(nextPaht,new ArrayList<>());
            queue.add(i);
            result.putIfAbsent(nextPaht,queue);
        });
        return result;
    }
}
