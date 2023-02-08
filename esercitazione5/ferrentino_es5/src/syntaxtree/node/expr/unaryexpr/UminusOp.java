package syntaxtree.node.expr.unaryexpr;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;

public class UminusOp extends UnaryExpr {

    public UminusOp(Expr expr) {
        super(expr);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
