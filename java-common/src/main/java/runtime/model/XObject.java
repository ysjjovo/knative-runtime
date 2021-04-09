package runtime.model;

public class XObject {
    private String contentType;
    private byte[] body;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public XObject(String contentType, byte[] body) {
        this.contentType = contentType;
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

}
