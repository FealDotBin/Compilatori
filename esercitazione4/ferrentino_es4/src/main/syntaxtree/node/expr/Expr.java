package main.syntaxtree.node.expr;

import main.Visitor.Visitor;
import main.syntaxtree.node.Node;

public abstract class Expr extends Node {

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
