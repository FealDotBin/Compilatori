package syntaxtree.node.stat;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;
import syntaxtree.node.expr.Id;
import syntaxtree.wrapper.FunCall;

import java.util.ArrayList;

public class FunCallOpStat extends Stat {

    private Id id;
    private ArrayList<Expr> exprList;

    public FunCallOpStat(Id id, ArrayList<Expr> exprList) {
        this.id = id;
        this.exprList = exprList;
    }

    public FunCallOpStat(FunCall funCall){
        this.id = funCall.getId();
        this.exprList = funCall.getExprList();
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

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
