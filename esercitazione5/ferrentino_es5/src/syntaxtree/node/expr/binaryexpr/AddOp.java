package syntaxtree.node.expr.binaryexpr;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;

public class AddOp extends BinaryExpr {

    public AddOp(Expr left, Expr right) {
        super(left, right);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
