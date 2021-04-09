package runtime.converter;

public class IntegerArrayConverter implements TypeConverter<Integer[]> {
    @Override
    public Integer[] convert(String v) {
        String[] split = v.split(",");
        Integer[] valArr = new Integer[split.length];
        for(int i=0;i<split.length;i++){
            valArr[i] = Integer.valueOf(split[i]);
        }
        return valArr;
    }
}
