package app.model.utas;

public class ResponseTop {

    private long currentTime;
    private int version;
    private String status;
    private int code;
    private String text;
    private ResponseData data;

    public long getCurrentTime() {
        return currentTime;
    }

    public int getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public int getCode() {
        return code;
    }

    public ResponseData getData() {
        return data;
    }

    public String getText() {
        return text;
    }
}
