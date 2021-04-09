package runtime.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter implements TypeConverter<Date> {
    @Override
    public Date convert(String v) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(v);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
