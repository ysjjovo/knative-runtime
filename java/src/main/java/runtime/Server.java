package runtime;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import runtime.log.Logger;
import runtime.model.Pair;
import runtime.util.FuncUtil;
import runtime.util.JsonUtil;
import runtime.util.ModelUtil;
import runtime.model.XObject;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class Server {
    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), -1);

        server.createContext("/", t -> {
            String requestId = t.getRequestHeaders().getFirst("x-request-id");
            if(requestId != null){
                Logger.REQUEST_ID.set(requestId);
            }
            Logger.println("function start!");
            if(!"/".equals(t.getRequestURI().getPath())){
                resStr(t, 200,"OK");
                return;
            }

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(t.getRequestBody(), StandardCharsets.UTF_8));
                String query = t.getRequestURI().getQuery();
                Object params = null;
                String reqType = t.getRequestHeaders().getFirst("Content-Type");
                if("application/x-www-form-urlencoded".equals(reqType)){
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null){
                        sb.append(line);
                    }
                    if(query == null){
                        query = "";
                    }
                    query += sb.toString();
                }else if(reqType != null){
                    params = JsonUtil.fromJson(reader, paramType);
                }

                if(params == null && query != null){
                    params = paramType.getConstructor().newInstance();
                    mergeQuery(params, query);
                }
                Object output = main.invoke(null, params);
                Logger.REQUEST_ID.remove();
                if (output == null) {
                    resStr(t, 502, "The action should not returned null");;
                    return;
                }
                if(output instanceof XObject){
                    XObject xo = (XObject) output;

                    String contentType = xo.getContentType();
                    byte[] body = xo.getBody();
                    if(contentType == null || body == null){
                        resStr(t, 502, "property contentType or body should not be null");
                        return;
                    }
                    t.getResponseHeaders().set("Content-Type", contentType);
                    resBytes(t, 200, body);
                }else{
                    t.getResponseHeaders().set("Content-Type", "application/json");
                    resStr(t, 200, JsonUtil.toJson(output));
                }
            } catch (Exception e) {
                Logger.println(e.toString());
                resStr(t, 502, "An error has occurred (see logs for details): " + e);
            }

        });

        server.start();
    }

    private static Method main;
    private static Class<?> paramType;
    static{
        Pair<Method, Class<?>> pair = FuncUtil.getEntryInfo();
        main = pair.key;
        paramType = pair.value;
    }
    private static void resStr(HttpExchange t, int code, String content) throws IOException {
        byte[] bytes = content.getBytes(StandardCharsets.UTF_8);
        resBytes(t, code, bytes);
    }

    private static void resBytes(HttpExchange t, int code, byte[] bytes) throws IOException {
        t.sendResponseHeaders(code, bytes.length);
        OutputStream os = t.getResponseBody();
        os.write(bytes);
        os.close();
        Logger.println("function end!");
    }
    private static void mergeQuery(Object o,String query) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        for (String param : query.split("&")) {
            String[] p = param.split("=");
            if (p.length>1) {
                ModelUtil.set(o, p[0], p[1]);
            }
        }
    }
}
