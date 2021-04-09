package runtime;

import runtime.log.Logger;
import runtime.model.Pair;
import runtime.model.XObject;
import runtime.util.FuncUtil;
import runtime.util.JsonUtil;
import runtime.util.ModelUtil;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Provider
@PreMatching
public class Server implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext ctx) {
        String requestId = ctx.getHeaderString("x-request-id");
        if(requestId != null){
            Logger.REQUEST_ID.set(requestId);
        }
        Logger.println("function start!");

        if(!"/".equals(ctx.getUriInfo().getPath())){
            ctx.abortWith(Response.ok("ok!").build());
            Logger.println("function end!");
            return;
        }
        Response.ResponseBuilder builder;
        try{
            MultivaluedMap<String, String> queryMap = ctx.getUriInfo().getQueryParameters();
            StringBuilder sb = new StringBuilder();
            for(Map.Entry<String, List<String>>  entry :queryMap.entrySet()){
                String name = entry.getKey();
                for(String value :entry.getValue()){
                    sb.append(name).append('=').append(value).append('&');
                }
            }
            Object params = null;
            String reqType = ctx.getHeaderString("Content-Type");
            BufferedReader reader = new BufferedReader(new InputStreamReader(ctx.getEntityStream(), StandardCharsets.UTF_8));
            if("application/x-www-form-urlencoded".equals(reqType)){
                String line;
                while ((line = reader.readLine()) != null){
                    sb.append(line);
                }
            }else if(reqType != null){
                params = JsonUtil.fromJson(reader, paramType);
            }

            if(params == null && sb.length() != 0){
                params = paramType.getConstructor().newInstance();
                mergeQuery(params, sb.toString());
            }
            Object output = main.invoke(null, params);
            if(output instanceof XObject){
                XObject xo = (XObject) output;
                String contentType = xo.getContentType();
                byte[] body = xo.getBody();
                if(contentType == null || body == null){
                    builder = Response.status(502).entity("property contentType or body should not be null");
                }else{
                    builder = Response.ok(body, contentType);
                }
            }else{
                builder = Response.ok(output, MediaType.APPLICATION_JSON_TYPE);
            }
        }catch (Exception e){
            Logger.println(e.toString());
            builder = Response.status(502).entity("An error has occurred (see logs for details): " + e);
        }
        ctx.abortWith(builder.build());
        Logger.println("function end!");
    }

    private static Method main;
    private static Class<?> paramType;
    static{
        Pair<Method, Class<?>> pair = FuncUtil.getEntryInfo();
        main = pair.key;
        paramType = pair.value;
    }

    private static void mergeQuery(Object o,String query) throws NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        for (String param : query.split("&")) {
            if(param.length()>0){
                String[] p = param.split("=");
                if (p.length>1) {
                    ModelUtil.set(o, p[0], p[1]);
                }
            }
        }
    }
}