package env;

import java.util.Objects;

public class EnvKey {

    private String kind;
    private String id;

    public EnvKey(String kind, String id) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnvKey key = (EnvKey) o;
        return Objects.equals(kind, key.kind) && Objects.equals(id, key.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(kind, id);
    }
}
