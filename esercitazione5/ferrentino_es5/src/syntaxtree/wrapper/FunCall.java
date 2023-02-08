package syntaxtree.wrapper;

import syntaxtree.node.expr.Expr;
import syntaxtree.node.expr.Id;

import java.util.ArrayList;

public class FunCall {

    private Id id;
    private ArrayList<Expr> exprList;

    public FunCall(Id id, ArrayList<Expr> exprList) {
        this.id = id;
        this.exprList = exprList;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public ArrayList<Expr> getExprList() {
        return exprList;
    }

    public void setExprList(ArrayList<Expr> exprList) {
        this.exprList = exprList;
    }
}
