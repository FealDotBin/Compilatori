package main.syntaxtree.node;

import main.Visitor.Visitor;
import main.syntaxtree.node.Node;
import main.type.TypeValidator;

public class TypeOp extends Node {

    private String type;

    public TypeOp(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
