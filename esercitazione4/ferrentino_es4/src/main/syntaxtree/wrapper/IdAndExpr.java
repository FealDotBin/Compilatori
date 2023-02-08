package main.syntaxtree.wrapper;

import main.syntaxtree.node.expr.Expr;
import main.syntaxtree.node.expr.Id;

public class IdAndExpr {

    private Id id;
    private Expr expr;

    public IdAndExpr(Id id) {
        this.id = id;
    }

    public IdAndExpr(Id id, Expr expr) {
        this.id = id;
        this.expr = expr;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExprNode(Expr expr) {
        this.expr = expr;
    }
}
