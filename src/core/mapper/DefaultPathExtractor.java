package core.mapper;

import core.data.CadastralEntry;

import java.util.Calendar;

/**
 * Преобразует CadastralEntry в путь к файлу
 */
public class DefaultPathExtractor implements IPathExtractor<CadastralEntry> {

    private final String resultDir;
    private final String fileFormat;

    public DefaultPathExtractor(String resultDir, String fileFormat) {
        this.resultDir = resultDir;
        this.fileFormat = fileFormat;
    }

    @Override
    public String getCurrentFolder(CadastralEntry entry)
    {
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(entry.getRegistrationDate());
        return String.format("%d-%02d",calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH)+1);
    }
    @Override
    public String getFileName(CadastralEntry entry)
    {
        return String.format("%s%s%s",entry.getCadastralNumber().getCd(),entry.getCadastralNumber().getCa(),fileFormat);
    }
    @Override
    public String getPath(CadastralEntry entry)
    {
        return String.format("%s/%s/%s",this.resultDir,getCurrentFolder(entry),getFileName(entry));
    }
}
