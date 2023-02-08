package syntaxtree.node.expr.unaryexpr;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;

public class NotOp extends UnaryExpr {

    public NotOp(Expr expr) {
        super(expr);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
