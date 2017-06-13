package core.mapper;

@FunctionalInterface
public interface IResultMapperFactory<R,T,P> {
        IResultMapper<R,T> create(P data);
}
