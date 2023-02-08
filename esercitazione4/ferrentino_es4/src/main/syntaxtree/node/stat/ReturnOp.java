package main.syntaxtree.node.stat;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;
public class ReturnOp extends Stat {

    private Expr expr;

    public ReturnOp() {
    }

    public ReturnOp(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExprNode(Expr expr) {
        this.expr = expr;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
