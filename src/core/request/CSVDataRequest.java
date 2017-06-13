package core.request;

import com.opencsv.CSVReader;
import core.mapper.IResultMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Получение данных из CSV файла
 */
public class CSVDataRequest implements IRequest<Collection<String[]>> {

    private final int offset;
    private final int limit;
    private final CSVReader csvReader;

    public CSVDataRequest(int offset, int limit, CSVReader csvReader) {
        this.offset = offset;
        this.limit = limit;
        this.csvReader = csvReader;
    }

    @Override
    public Collection<String[]> execute() {

        Collection<String[]> collection=new ArrayList<>();
        for (int i = this.offset; i < (this.offset+ this.limit); i++) {
            String[] result = null;
            try {
                if ((result=csvReader.readNext()) == null)
                {
                    break;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            collection.add(result);
        }
        return collection;
    }

    @Override
    public <T> T execute(IResultMapper<T, Collection<String[]>> mapper) {
        return mapper.map(execute());
    }

    @Override
    public void close() {

    }

}
