package runtime.util;

import runtime.converter.DateConverter;
import runtime.converter.IntegerArrayConverter;
import runtime.converter.IntegerConverter;
import runtime.converter.TypeConverter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ModelUtil {
    private static Map<Class<?>, TypeConverter<?>> converters =new HashMap<>();
    static{
        converters.put(Integer.class,new IntegerConverter());
        converters.put(Integer[].class,new IntegerArrayConverter());
        converters.put(Date.class, new DateConverter());
    }
    public  static <T> void set(T o, String k, String v) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        set(o, k.split("\\."),0, v);
    }

    public  static void set(Object o, String[] path,int cur, String v) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        Class<?> tClass = o.getClass();
        Field field;
        try {
            field = tClass.getDeclaredField(path[cur]);
        } catch (NoSuchFieldException e) {
            StringBuilder sb = new StringBuilder();
            for(String p: path){
                sb.append(p).append(".");
            }
            System.out.println("field: "+ sb.toString() + " not exists!");
            return;
        }
        field.setAccessible(true);
        Class<?> fieldType = field.getType();
        if (cur == path.length - 1) {
            if(fieldType.equals(String.class)){
                field.set(o, v);
            }else {
                field.set(o, converters.get(fieldType).convert(v));
            }
        } else {
            Object value = field.get(o);
            if (value == null) {
                value = fieldType.getConstructor().newInstance();
                field.set(o, value);
            }
            set(value, path, ++cur, v);
        }
    }
}
