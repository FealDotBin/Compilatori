package main.syntaxtree.node.expr.binaryexpr;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;

public class StrCatOp extends BinaryExpr {

    public StrCatOp(Expr left, Expr right) {
        super(left, right);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
