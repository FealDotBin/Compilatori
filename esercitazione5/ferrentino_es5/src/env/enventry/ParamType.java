package env.enventry;

public class ParamType {

    private String type;
    private boolean isOut;

    public ParamType(String type, boolean isOut) {
        this.type = type;
        this.isOut = isOut;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }
}
