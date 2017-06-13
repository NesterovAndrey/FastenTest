package core.mapper;

import core.data.CadastralEntry;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class StringArrToEntryCollection<T> implements IResultMapper<Collection<T>,Collection<String[]>> {

    private final IResultMapper<T,String[]> mapper;

    public StringArrToEntryCollection(IResultMapper<T, String[]> mapper) {
        this.mapper = mapper;
    }

    @Override
    public Collection<T> map(Collection<String[]> result) {
        return result.stream().map(this.mapper::map).collect(Collectors.toList());
    }
}
