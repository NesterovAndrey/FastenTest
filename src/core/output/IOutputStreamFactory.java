package core.output;

import java.io.OutputStream;

@FunctionalInterface
public interface IOutputStreamFactory<T> {
    OutputStream create(T data);
}
