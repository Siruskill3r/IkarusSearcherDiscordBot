package app.model.utas;

import java.util.List;

public class ResponseData {
    private List<Vehicle> list;
    private boolean outOfRange;
    private boolean limitExceeded;
    private References references;

    public List<Vehicle> getList() {
        return list;
    }

    public boolean isOutOfRange() {
        return outOfRange;
    }

    public References getReferences() {
        return references;
    }

    public boolean isLimitExceeded() {
        return limitExceeded;
    }
}
