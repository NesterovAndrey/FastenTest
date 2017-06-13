package core.mapper;

import core.data.CadastralEntry;
import core.data.CadastralNumberConverter;
import util.SimpleDateFormatFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * ПРеобразует ResultSet в коллекцию CadastralEnty
 */
public class CadastralEntrySetMapper implements IResultMapper<Collection<CadastralEntry>,ResultSet> {

    private final SimpleDateFormatFactory simpleDateFormatFactory;
    private final CadastralNumberConverter cadastralNumberConverter;

    public CadastralEntrySetMapper(SimpleDateFormatFactory simpleDateFormatFactory, CadastralNumberConverter cadastralNumberConverter) {
        this.simpleDateFormatFactory = simpleDateFormatFactory;
        this.cadastralNumberConverter = cadastralNumberConverter;
    }
    @Override
    public Collection<CadastralEntry> map(ResultSet resultSet) {
        Set<CadastralEntry> set=new TreeSet<>();
        try {
            while(resultSet.next()) {
                CadastralEntry entry = new CadastralEntry(
                        resultSet.getString("owner_passport"),
                        resultSet.getString("owner_id"),
                        simpleDateFormatFactory.create().parse(resultSet.getString("reg_date")),
                        cadastralNumberConverter.parse(resultSet.getString("cn")));
                set.add(entry);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return set;
    }
}
