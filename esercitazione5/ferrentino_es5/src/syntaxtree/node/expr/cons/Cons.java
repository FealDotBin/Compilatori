package syntaxtree.node.expr.cons;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;

public abstract class Cons extends Expr {

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
