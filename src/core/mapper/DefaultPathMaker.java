package core.mapper;

public class DefaultPathMaker implements IPathMaker<String> {
    private final String resultDir;
    private final String fileFormat;

    public DefaultPathMaker(String resultDir, String fileFormat) {
        this.resultDir = resultDir;
        this.fileFormat = fileFormat;
    }

    @Override
    public String make(String data) {
        return String.format("%s/%s%s",this.resultDir, data,this.fileFormat);
    }
}
