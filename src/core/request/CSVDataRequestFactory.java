package core.request;

import com.opencsv.CSVReader;

import java.util.Collection;

public class CSVDataRequestFactory implements IPartialRequestFactory<CSVReader,Collection<String[]>>{

    @Override
    public IRequest<Collection<String[]>> create(CSVReader data, int offset, int limit) {
        return new CSVDataRequest(offset,limit,data);
    }
}
