package syntaxtree.node.stat;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;

import java.util.ArrayList;

public class WriteOp extends Stat {

    private boolean isLn;
    private ArrayList<Expr> exprList;

    public WriteOp(boolean isLn, ArrayList<Expr> exprList) {
        if(exprList.isEmpty())
            throw new RuntimeException("exprList non può essere vuota");
        this.isLn = isLn;
        this.exprList = exprList;
    }

    public boolean isLn() {
        return isLn;
    }

    public void setLn(boolean ln) {
        isLn = ln;
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
