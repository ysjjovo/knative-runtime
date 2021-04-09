package runtime;

import runtime.model.Base64XObject;
import runtime.model.Pair;
import runtime.model.XObject;
import runtime.util.FuncUtil;
import runtime.util.JsonUtil;

import java.lang.reflect.Method;
import java.util.Base64;

public class Test {
    public static void main(String[] args) throws Exception {
        if(args.length != 1){
            System.out.println("参数有且仅有1个，函数入参<param>!");
        }
        String json = new String(Base64.getDecoder().decode(args[0]));
        Pair<Method, Class<?>> pair = FuncUtil.getEntryInfo();
        Object output = pair.key.invoke(null, JsonUtil.fromJson(json, pair.value));
        if(output instanceof XObject){
            XObject xo = (XObject) output;
            output = new Base64XObject(xo.getContentType(), new String(Base64.getEncoder().encode(xo.getBody())));
        }
        System.out.println("--boundary--");
        System.out.println(JsonUtil.toJson(output));
    }
}
