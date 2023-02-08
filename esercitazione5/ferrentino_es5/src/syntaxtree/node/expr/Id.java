package syntaxtree.node.expr;

import visitor.Visitor;

public class Id extends Expr {

    private String attribute;
    private String kind;

    public Id(){}

    public Id(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
