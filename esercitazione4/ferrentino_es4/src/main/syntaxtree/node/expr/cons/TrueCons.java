package main.syntaxtree.node.expr.cons;

import main.Visitor.Visitor;

public class TrueCons extends Cons {

    public TrueCons() {
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
