package runtime.model;

public class Base64XObject {
    private String contentType;
    private String body;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Base64XObject(String contentType, String body) {
        this.contentType = contentType;
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
