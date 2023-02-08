package main.syntaxtree.node;

import main.Visitor.Visitor;

public abstract class Node {

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
