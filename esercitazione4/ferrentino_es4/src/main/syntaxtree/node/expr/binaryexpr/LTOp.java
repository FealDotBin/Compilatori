package main.syntaxtree.node.expr.binaryexpr;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;

public class LTOp extends BinaryExpr {

    public LTOp(Expr left, Expr right) {
        super(left, right);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
