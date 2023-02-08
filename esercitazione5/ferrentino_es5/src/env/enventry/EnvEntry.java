package env.enventry;

public class EnvEntry {

    private String kind;
    private String id;

    public EnvEntry(String kind, String id) {
        this.kind = kind;
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
