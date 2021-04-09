package runtime.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Reader;

public  class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    static{
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }
    public static <T> T fromJson(Reader r, Class<T> tClass) throws IOException {
        return mapper.readValue(r,tClass);
    }
    public static <T> T fromJson(String s,Class<T> tClass) throws JsonProcessingException {
        return mapper.readValue(s,tClass);
    }
    public static <T> String toJson(T o) throws JsonProcessingException {
        return mapper.writeValueAsString(o);
    }
}
