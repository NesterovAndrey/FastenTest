package core.mapper;

import core.data.CadastralEntry;
import util.SimpleDateFormatFactory;

/**
 * Преобразует CadastralEntry в массив String
 */
public class CadastralEntryToStringArr implements IResultMapper<String[],CadastralEntry> {

    private final SimpleDateFormatFactory simpleDateFormatFactory;

    public CadastralEntryToStringArr(SimpleDateFormatFactory simpleDateFormatFactory) {
        this.simpleDateFormatFactory = simpleDateFormatFactory;
    }

    @Override
    public String[] map(CadastralEntry result) {

        String[] arr={result.getCadastralNumber().toString(),
                result.getOwnerId(),
                result.getOwnerPassport(),
                simpleDateFormatFactory.create().format(result.getRegistrationDate())};
        return arr;
    }
}
