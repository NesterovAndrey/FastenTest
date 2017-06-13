package core.mapper;

import core.data.CadastralEntry;
import util.SimpleDateFormatFactory;

public class FromEntityToValuesString implements IResultMapper<String, CadastralEntry> {

    private final SimpleDateFormatFactory simpleDateFormatFactory;

    public FromEntityToValuesString(SimpleDateFormatFactory simpleDateFormatFactory) {
        this.simpleDateFormatFactory = simpleDateFormatFactory;
    }


    @Override
    public String map(CadastralEntry result) {
        return String.format("'%s%s','%s','%s','%s','%s'",
                result.getCadastralNumber().getCd(),
                result.getCadastralNumber().getCa(),
                result.getCadastralNumber(),
                simpleDateFormatFactory.create().format(result.getRegistrationDate()),
                result.getOwnerId(),
                result.getOwnerPassport());
    }
}
