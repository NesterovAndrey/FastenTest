package core.mapper;

@FunctionalInterface
public interface IPathMaker<T> {
    String make(T data);
}
