package core.dataConsumer;

import com.opencsv.CSVWriter;
import core.mapper.IResultMapper;
import util.SimpleDateFormatFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

/**
 * Записывает данных в CSV файл
 * @param <T> тип записываемых данных
 */
public class CSVResultWriter<T> implements ResultWriter {
    private static Logger log = Logger.getLogger(DefaultConsumer.class.getName());


    private final CSVWriter csvWriter;
    private final T result;
    private final IResultMapper<Collection<String[]>,T> mapper;
    public CSVResultWriter(T result, CSVWriter csvWriter, IResultMapper<Collection<String[]>,T> mapper){
        this.result=result;
        this.csvWriter=csvWriter;

        this.mapper = mapper;
    }
    @Override
    public void write() throws IOException {

        Collection<String[]> strings=mapper.map(result);
        log.info("WRITE "+strings.size());

        this.csvWriter.writeAll(strings,false);
        this.csvWriter.flush();

    }

}
