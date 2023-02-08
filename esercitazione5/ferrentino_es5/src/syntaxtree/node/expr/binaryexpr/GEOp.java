package syntaxtree.node.expr.binaryexpr;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;

public class GEOp extends BinaryExpr {

    public GEOp(Expr left, Expr right) {
        super(left, right);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
