package core.dataProducer;

import com.opencsv.CSVReader;
import core.mapper.IResultMapper;
import core.request.IPartialRequestFactory;
import core.request.IRequest;

import java.io.InputStream;
import java.util.Collection;
import java.util.List;

public class CSVDataProducerFactory<T> implements IProducerFactory<InputStream,PartialDataProducer<Collection<T>,?>> {

    private final IResultMapper<Collection<T>, Collection<String[]>> resultMapper;
    private final IPartialRequestFactory<CSVReader,Collection<String[]>> partialRequestFactory;
    public CSVDataProducerFactory(IResultMapper<Collection<T>, Collection<String[]>> resultMapper,
                                  IPartialRequestFactory<CSVReader, Collection<String[]>> partialRequestFactory) {

        this.resultMapper = resultMapper;
        this.partialRequestFactory = partialRequestFactory;
    }

    @Override
    public PartialDataProducer<Collection<T>,IRequest<Collection<String[]>>> create(InputStream source) {
        return new CSVDataProducer<>(resultMapper,partialRequestFactory,source);
    }
}
