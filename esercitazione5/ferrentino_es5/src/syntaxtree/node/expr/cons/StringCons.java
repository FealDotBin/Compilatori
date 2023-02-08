package syntaxtree.node.expr.cons;

import visitor.Visitor;

public class StringCons extends Cons {

    private String attribute;

    public StringCons(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
