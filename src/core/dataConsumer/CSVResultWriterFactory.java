package core.dataConsumer;

import com.opencsv.CSVWriter;
import core.mapper.IResultMapper;
import util.SimpleDateFormatFactory;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;

public class CSVResultWriterFactory<T> implements ResultWriterFactory<Collection<T>,OutputStream> {

    private final String separator;
    private final SimpleDateFormatFactory simpleDateFormatFactory;
    private final IResultMapper<Collection<String[]>,Collection<T>> resultMapper;
    public CSVResultWriterFactory(String separator,
                                  SimpleDateFormatFactory simpleDateFormatFactory,
                                  IResultMapper<Collection<String[]>,
                                          Collection<T>> resultMapper) {
        this.separator = separator;
        this.simpleDateFormatFactory = simpleDateFormatFactory;
        this.resultMapper = resultMapper;
    }
    @Override
    public ResultWriter create(Collection<T> data, OutputStream source) {
        ResultWriter resultWriter;
        resultWriter=new CSVResultWriter<>(data,
                new CSVWriter(
                        new OutputStreamWriter(source)),
                resultMapper);
        return resultWriter;
    }
}
