package core.data;

/**
 * Преобразует строку в CadastralNumber
 */
public class DefaultCadastralNumberConverter implements CadastralNumberConverter {
    private final String separator;

    public DefaultCadastralNumberConverter(String separator) {
        this.separator = separator;
    }

    @Override
    public CadastralNumber parse(String entry) {
        String[] arr=entry.split(this.separator);
        return new CadastralNumber(arr[0],arr[1],arr[2],arr[3]);
    }
}
