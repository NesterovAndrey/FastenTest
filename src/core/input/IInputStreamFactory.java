package core.input;

import java.io.InputStream;

public interface IInputStreamFactory<T> {
    InputStream create(T data);
}
