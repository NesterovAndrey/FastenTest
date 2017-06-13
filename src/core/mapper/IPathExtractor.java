package core.mapper;

public interface IPathExtractor<T> {
    String getCurrentFolder(T entry);
    String getFileName(T entry);
    String getPath(T entry);
}
