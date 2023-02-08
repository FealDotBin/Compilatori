package syntaxtree.node.expr.cons;

import visitor.Visitor;

public class CharCons extends Cons {

    private String attribute;

    public CharCons(String attribute) {
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
