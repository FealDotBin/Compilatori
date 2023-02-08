package main.syntaxtree.wrapper;

import main.syntaxtree.node.expr.Expr;

public class ExprAndOut {

    private Expr expr;
    private boolean isOut;

    public ExprAndOut(Expr expr, boolean isOut) {
        this.expr = expr;
        this.isOut = isOut;
    }

    public Expr getExprNode() {
        return expr;
    }

    public void setExprNode(Expr expr) {
        this.expr = expr;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }
}
