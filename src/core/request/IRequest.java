package core.request;

import core.mapper.IResultMapper;

/**
 * Извлечение денных
 * @param <C> Результат сырых данных
 */
public interface IRequest<C> {
    C execute();

    /**
     * ИЗвлекает данные предварительно преобразовывая с помозью маппера
     * @param mapper преобразует сырые данные в нужный вид
     * @param <T> тип финального результата
     * @return Данные преобразованные в нужный вид маппером
     */
    <T> T execute(IResultMapper<T,C> mapper);
    void close();
}
