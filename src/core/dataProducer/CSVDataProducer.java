package core.dataProducer;

import com.opencsv.CSVReader;
import core.request.IPartialRequestFactory;
import core.request.IRequest;
import core.mapper.IResultMapper;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.logging.Logger;

/**
 * Призводит данные полученные из CSV файла3
 * @param <T> Тип проивводимых данных
 */
public class CSVDataProducer<T> extends PartialDataProducer<Collection<T>,IRequest<Collection<String[]>>> {

    private static final Integer OFFSET=0;
    private static final Integer LIMIT=10000;

    private static Logger log = Logger.getLogger(PartialDataProducer.class.getName());

    private final IResultMapper<Collection<T>, Collection<String[]>> resultMapper;
    private final CSVReader csvReader;
    private final IPartialRequestFactory<CSVReader,Collection<String[]>> partialRequestFactory;

    private static int sum=0;
    public CSVDataProducer(Integer offset, Integer limit,
                           IResultMapper<Collection<T>, Collection<String[]>> resultMapper,
                           InputStream inputStream, IPartialRequestFactory<CSVReader, Collection<String[]>> partialRequestFactory) {
        super(offset,limit);
        this.resultMapper = resultMapper;
        this.csvReader = new CSVReader(new InputStreamReader(inputStream));
        this.partialRequestFactory=partialRequestFactory;
    }
    public CSVDataProducer(IResultMapper<Collection<T>, Collection<String[]>> resultMapper,
                           IPartialRequestFactory<CSVReader, Collection<String[]>> partialRequestFactory, InputStream inputStream) {
        this(OFFSET,LIMIT,resultMapper, inputStream, partialRequestFactory);
    }
    @Override
    protected Collection<T> rertrieve(IRequest<Collection<String[]>> request) {
        Collection<T> result =null;
        result=request.execute(this.resultMapper);
        moveOffset(getLimit());
        if(result.size()<=0)
            complete();
        log.info("PRODUCED "+result.size());
        return result;
    }

    @Override
    protected IRequest<Collection<String[]>> retrieveNext() {
        return this.partialRequestFactory.create(csvReader,this.getOffset(),this.getLimit());
    }
}
