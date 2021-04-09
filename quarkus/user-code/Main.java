import io.quarkus.runtime.annotations.RegisterForReflection;
import runtime.model.XObject;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import runtime.log.Logger;

@RegisterForReflection
public class Main {
    public static Object main(RegisterReq originParams) {
        return new RegisterResp(originParams,System.getenv());
    }

    public static XObject main1(Param params) throws IOException {
        Logger.println("xixi");
        return handler(params);
    }

    private static XObject handler(Param params) throws IOException {
        String path=params.getType()+ "";
        String ext = params.getExt();
        path += ext;
        if(params.getSubType() != null){
            ext= params.getSubType();
        }
        return new XObject(params.getType()+"/"+ ext,getBytes(path));
    }
    private static byte[]  getBytes(String path) throws IOException {
        File file = new File("/opt/code/" + path);
        FileInputStream fis = new FileInputStream(file);
        byte[] body = new byte[(int)file.length()];
        fis.read(body,0,body.length);
        return body;
    }
    @RegisterForReflection
    public static class RegisterReq {
        private String name="lin";
        private String type="json";

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    @RegisterForReflection
    public static class RegisterResp {
        private RegisterReq originParams;
        private Map<String,String> env;

        public RegisterReq getOriginParams() {
            return originParams;
        }

        public void setOriginParams(RegisterReq originParams) {
            this.originParams = originParams;
        }

        public Map<String, String> getEnv() {
            return env;
        }

        public void setEnv(Map<String, String> env) {
            this.env = env;
        }

        public RegisterResp(RegisterReq originParams, Map<String, String> env) {
            this.originParams = originParams;
            this.env = env;
        }
    }
    @RegisterForReflection
    public static class Param{
        private String type;
        private String subType;
        private String ext;
        private Date date;

        private SubParam sub;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getExt() {
            return ext;
        }

        public void setExt(String ext) {
            this.ext = ext;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public SubParam getSub() {
            return sub;
        }

        public void setSub(SubParam sub) {
            this.sub = sub;
        }

        public String getSubType() {
            return subType;
        }

        public void setSubType(String subType) {
            this.subType = subType;
        }
    }
    @RegisterForReflection
    public static class SubParam {
        private Integer integer;
        private Integer[] integers;

        public Integer getInteger() {
            return integer;
        }

        public void setInteger(Integer integer) {
            this.integer = integer;
        }

        public Integer[] getIntegers() {
            return integers;
        }

        public void setIntegers(Integer[] integers) {
            this.integers = integers;
        }
    }
}
