package main.syntaxtree.node.expr.cons;

import main.Visitor.Visitor;

public class FalseCons extends Cons {

    public FalseCons() {
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
