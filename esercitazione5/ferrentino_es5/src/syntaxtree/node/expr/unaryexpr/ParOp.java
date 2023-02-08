package syntaxtree.node.expr.unaryexpr;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;

public class ParOp extends UnaryExpr {

    public ParOp(Expr expr) {
        super(expr);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
