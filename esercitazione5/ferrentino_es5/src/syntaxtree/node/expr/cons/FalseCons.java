package syntaxtree.node.expr.cons;

import visitor.Visitor;

public class FalseCons extends Cons {

    public FalseCons() {
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
