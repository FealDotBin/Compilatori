package main.syntaxtree.node.expr.unaryexpr;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;

public abstract class UnaryExpr extends Expr {
    Expr expr;

    public UnaryExpr(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
