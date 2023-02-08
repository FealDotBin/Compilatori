package main.syntaxtree.node.expr;

import main.Visitor.Visitor;

public class Id extends Expr {

    private String attribute;

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

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
