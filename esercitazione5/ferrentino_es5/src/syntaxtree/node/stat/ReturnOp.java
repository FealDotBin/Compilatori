package syntaxtree.node.stat;

import visitor.Visitor;
import syntaxtree.node.expr.Expr;
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

    public void setExpr(Expr expr) {
        this.expr = expr;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
