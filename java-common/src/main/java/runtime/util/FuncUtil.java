package runtime.util;

import runtime.log.Logger;
import runtime.model.Pair;

import java.lang.reflect.Method;

import static java.lang.Thread.currentThread;

public class FuncUtil {
    public static Pair<Method,Class<?>> getEntryInfo(){
        String mainName = System.getenv("MAIN");
        String className, methodName;

        if(mainName ==null || mainName.equals("")){
            className = "Main";
            methodName = "main";
        }else{
            int i = mainName.lastIndexOf(".");
            if(i == -1 || i == 0 || i == mainName.length() -1){
                throw new RuntimeException("Invalid function entry!");
            }
            className = mainName.substring(0, i);
            methodName = mainName.substring(i + 1);
        }
        try {
            Class<?> aClass;
            try{
                aClass = Class.forName(className);
            }catch (Exception e) {
                aClass = currentThread().getContextClassLoader().loadClass(className);
            }
            for(Method m : aClass.getDeclaredMethods()){
                if( (m.getModifiers()&1) == 1 && m.getName().equals(methodName)){
                    return new Pair<>(m,m.getParameterTypes()[0]);
                }
            }
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
            System.exit(-1);
        }
        throw new RuntimeException("入口函数main未找到!");
    }
}
