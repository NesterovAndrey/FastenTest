package core.request;

public interface IPartialRequestFactory<T,C> {
    IRequest<C> create(T data, int offset,int limit);
}
