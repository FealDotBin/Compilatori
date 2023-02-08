package env.enventry;

public class VarEnvEntry extends EnvEntry {

    private boolean isOut;
    private String type;

    public VarEnvEntry(String kind, String id, String type) {
        super(kind, id);
        isOut = false;
        this.type = type;
    }

    public VarEnvEntry(String kind, String id, boolean isOut, String type) {
        super(kind, id);
        this.isOut = isOut;
        this.type = type;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
