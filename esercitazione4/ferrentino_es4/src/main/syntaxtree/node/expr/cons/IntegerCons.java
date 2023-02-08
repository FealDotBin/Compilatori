package main.syntaxtree.node.expr.cons;

import main.Visitor.Visitor;

public class IntegerCons extends Cons {

    private String attribute;

    public IntegerCons(String attribute) {
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
