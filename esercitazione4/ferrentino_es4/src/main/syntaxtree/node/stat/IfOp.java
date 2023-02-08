package main.syntaxtree.node.stat;

import main.Visitor.Visitor;
import main.syntaxtree.node.BodyOp;
import main.syntaxtree.node.expr.Expr;

public class IfOp extends Stat {

    private Expr expr;
    private BodyOp bodyOp;
    private BodyOp bodyOpElse;

    public IfOp(Expr expr, BodyOp bodyOp) {
        this.expr = expr;
        this.bodyOp = bodyOp;
    }

    public IfOp(Expr expr, BodyOp bodyOp, BodyOp bodyOpElse) {
        this.expr = expr;
        this.bodyOp = bodyOp;
        this.bodyOpElse = bodyOpElse;
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

    public BodyOp getBodyOpElse() {
        return bodyOpElse;
    }

    public void setBodyOpElse(BodyOp bodyOpElse) {
        this.bodyOpElse = bodyOpElse;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
