package core.mapper;

import java.util.function.Function;

public class ResultMapperFactory<R,T,P> implements IResultMapperFactory<R,T,P> {

    private final Function<P,IResultMapper<R,T>> function;

    public ResultMapperFactory(Function<P,IResultMapper<R,T>> function) {
        this.function = function;
    }

    @Override
    public IResultMapper<R, T> create(P data) {
        return this.function.apply(data);
    }
}
