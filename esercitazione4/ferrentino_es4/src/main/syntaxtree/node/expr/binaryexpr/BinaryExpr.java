package main.syntaxtree.node.expr.binaryexpr;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Expr;

public abstract class BinaryExpr extends Expr {

    private Expr left;
    private Expr right;

    public BinaryExpr(){}

    public BinaryExpr(Expr left, Expr right) {
        this.left = left;
        this.right = right;
    }

    public Expr getLeft() {
        return left;
    }

    public void setLeft(Expr left) {
        this.left = left;
    }

    public Expr getRight() {
        return right;
    }

    public void setRight(Expr right) {
        this.right = right;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
