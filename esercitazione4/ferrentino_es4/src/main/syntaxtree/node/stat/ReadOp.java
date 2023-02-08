package main.syntaxtree.node.stat;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;
import main.syntaxtree.node.expr.Id;

import java.util.ArrayList;

public class ReadOp extends Stat {

    private ArrayList<Id> idList;
    private Expr expr;

    public ReadOp(ArrayList<Id> idList) {
        if(idList.isEmpty())
            throw new RuntimeException("idList non può essere vuota");
        this.idList = idList;
    }

    public ReadOp(ArrayList<Id> idList, Expr expr) {
        if(idList.isEmpty())
            throw new RuntimeException("idList non può essere vuota");
        this.idList = idList;
        this.expr = expr;
    }

    public ArrayList<Id> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<Id> idList) {
        this.idList = idList;
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
