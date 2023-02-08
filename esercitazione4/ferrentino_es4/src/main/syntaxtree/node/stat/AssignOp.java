package main.syntaxtree.node.stat;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;
import main.syntaxtree.node.expr.Id;

import java.util.ArrayList;

public class AssignOp extends Stat {

    private ArrayList<Id> idList;
    private ArrayList<Expr> exprList;

    public AssignOp(ArrayList<Id> idList, ArrayList<Expr> exprList) {
        if(idList.isEmpty())
            throw new RuntimeException("idList non può essere vuota");
        if(exprList.isEmpty())
            throw new RuntimeException("exprList non può essere vuota");
        this.idList = idList;
        this.exprList = exprList;
    }

    public ArrayList<Id> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<Id> idList) {
        this.idList = idList;
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
