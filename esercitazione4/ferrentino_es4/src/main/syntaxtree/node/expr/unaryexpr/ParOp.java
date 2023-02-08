package main.syntaxtree.node.expr.unaryexpr;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;
import main.syntaxtree.node.expr.binaryexpr.BinaryExpr;

public class ParOp extends UnaryExpr {

    public ParOp(Expr expr) {
        super(expr);
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
