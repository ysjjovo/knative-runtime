package runtime.converter;

public interface TypeConverter<T> {
    T convert(String v);
}
