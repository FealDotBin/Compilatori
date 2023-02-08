package main.syntaxtree.node.stat;

import main.Visitor.Visitor;
import main.syntaxtree.node.BodyOp;
import main.syntaxtree.node.expr.Expr;

public class WhileOp extends Stat {

    private Expr expr;
    private BodyOp bodyOp;

    public WhileOp(Expr expr, BodyOp bodyOp) {
        this.expr = expr;
        this.bodyOp = bodyOp;
    }

    public Expr getExpr() {
        return expr;
    }

    public void setExprNode(Expr expr) {
        this.expr = expr;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
