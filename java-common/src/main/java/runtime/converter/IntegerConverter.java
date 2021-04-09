package runtime.converter;

public class IntegerConverter implements TypeConverter<Integer> {
    @Override
    public Integer convert(String v) {
        return Integer.valueOf(v);
    }
}
