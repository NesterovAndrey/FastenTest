package util;

import java.text.SimpleDateFormat;

public class SimpleDateFormatFactory {
    private final String pattern;

    public SimpleDateFormatFactory(String pattern) {
        this.pattern = pattern;
    }

    public SimpleDateFormat create()
    {
        return create("yyyy-MM-dd");
    }
    public SimpleDateFormat create(String format)
    {
        return new SimpleDateFormat(format);
    }
}
