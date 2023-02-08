package main.syntaxtree.node.expr.cons;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;

public abstract class Cons extends Expr {

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
