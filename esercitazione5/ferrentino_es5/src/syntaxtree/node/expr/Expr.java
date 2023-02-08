package syntaxtree.node.expr;

import visitor.Visitor;
import syntaxtree.node.Node;

public abstract class Expr extends Node {

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
