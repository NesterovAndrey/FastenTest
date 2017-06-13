package core.mapper;

/**
 * Преобразователь данных
 * @param <T> Тип выходных данных
 * @param <C> Тип входных данных
 */
//Было бы хорошо переделать мапперы в императивном стиле. Но не стал тратить время
public interface IResultMapper<T,C> {
    T map(C result);
}
