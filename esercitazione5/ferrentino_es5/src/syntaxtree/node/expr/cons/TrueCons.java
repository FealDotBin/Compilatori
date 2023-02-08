package syntaxtree.node.expr.cons;

import visitor.Visitor;

public class TrueCons extends Cons {

    public TrueCons() {
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
