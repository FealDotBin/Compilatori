package main.syntaxtree.node.expr.cons;

import main.Visitor.Visitor;

public class RealCons extends Cons {

    private String attribute;

    public RealCons(){}

    public RealCons(String attribute) {
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
