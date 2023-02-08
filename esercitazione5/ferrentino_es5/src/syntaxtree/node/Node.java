package syntaxtree.node;

import env.Env;
import visitor.Visitor;

public abstract class Node {

    private String type;
    private Env env;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Env getEnv() {
        return env;
    }

    public void setEnv(Env env) {
        this.env = env;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }


}
