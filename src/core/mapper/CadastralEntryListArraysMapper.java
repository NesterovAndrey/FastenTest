package core.mapper;

import core.data.CadastralEntry;
import core.data.CadastralNumberConverter;
import util.SimpleDateFormatFactory;

import java.text.ParseException;
import java.util.Date;

/**
 * Преобразовываем массив строк в CadastralEntry
 */
public class CadastralEntryListArraysMapper implements IResultMapper<CadastralEntry,String[]> {

    private final SimpleDateFormatFactory simpleDateFormatFactory;
    private final CadastralNumberConverter cadastralNumberConverter;

    public CadastralEntryListArraysMapper(SimpleDateFormatFactory simpleDateFormatFactory, CadastralNumberConverter cadastralNumberConverter) {
        this.simpleDateFormatFactory = simpleDateFormatFactory;
        this.cadastralNumberConverter = cadastralNumberConverter;
    }
    @Override
    public CadastralEntry map(String[] result) {
        CadastralEntry entry=null;
        try {

            Date string= this.simpleDateFormatFactory.create().parse(result[3]);
            String string1=this.simpleDateFormatFactory.create().format(string);
            entry=new CadastralEntry(result[2],
                            result[1],
                            string,
                            this.cadastralNumberConverter.parse(result[0]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return entry;
    }
}
