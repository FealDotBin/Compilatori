package visitor;

import kind.Kind;
import syntaxtree.node.*;
import syntaxtree.node.expr.Expr;
import syntaxtree.node.expr.FunCallOpExpr;
import syntaxtree.node.expr.Id;
import syntaxtree.node.expr.binaryexpr.*;
import syntaxtree.node.expr.cons.*;
import syntaxtree.node.expr.unaryexpr.NotOp;
import syntaxtree.node.expr.unaryexpr.ParOp;
import syntaxtree.node.expr.unaryexpr.UminusOp;
import syntaxtree.node.expr.unaryexpr.UnaryExpr;
import syntaxtree.node.stat.*;
import syntaxtree.wrapper.IdAndExpr;

import java.util.ArrayList;

public class IdSetKindVisitor extends AbstractVisitor implements Visitor {

    public IdSetKindVisitor() {
    }

    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        super.visit(bodyOp);

        return null;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) {
        funDeclOp.getId().setKind(Kind.FUN);

        super.visit(funDeclOp);

        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        setKindList(parDeclOp.getIdList());

        super.visit(parDeclOp);

        return null;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        super.visit(programOp);

        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        setKindIdAndExprList(varDeclOp.getIdAndExprList());

        super.visit(varDeclOp);

        return null;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        return null;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        setKindList(assignOp.getIdList());
        setKindList(assignOp.getExprList());

        super.visit(assignOp);

        return null;
    }

    @Override
    public Object visit(ForOp forOp) {
        forOp.getId().setKind(Kind.VAR);

        super.visit(forOp);

        return null;
    }

    @Override
    public Object visit(IfOp ifOp) {
        setKindExpr(ifOp.getExpr());

        super.visit(ifOp);

        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        setKindList(readOp.getIdList());
        if(readOp.getExpr() != null) {
            setKindExpr(readOp.getExpr());
        }

        super.visit(readOp);

        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        if(returnOp.getExpr() != null) {
            setKindExpr(returnOp.getExpr());
        }

        super.visit(returnOp);

        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        setKindExpr(whileOp.getExpr());

        super.visit(whileOp);

        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        setKindList(writeOp.getExprList());

        super.visit(writeOp);

        return null;
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        funCallOpStat.getId().setKind(Kind.FUN);
        setKindList(funCallOpStat.getExprList());

        super.visit(funCallOpStat);

        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        funCallOpExpr.getId().setKind(Kind.FUN);
        setKindList(funCallOpExpr.getExprList());

        super.visit(funCallOpExpr);

        return null;
    }

    @Override
    public Object visit(Id id) {
        return null;
    }

    @Override
    public Object visit(BinaryExpr binaryExpr) {
        return null;
    }

    @Override
    public Object visit(AddOp addOp) {
        setKindExpr(addOp.getLeft());
        setKindExpr(addOp.getRight());
        super.visit(addOp);

        return null;
    }

    @Override
    public Object visit(AndOp andOp) {
        setKindExpr(andOp.getLeft());
        setKindExpr(andOp.getRight());
        super.visit(andOp);

        return null;
    }

    @Override
    public Object visit(DiffOp diffOp) {
        setKindExpr(diffOp.getLeft());
        setKindExpr(diffOp.getRight());
        super.visit(diffOp);

        return null;
    }

    @Override
    public Object visit(DivOp divOp) {
        setKindExpr(divOp.getLeft());
        setKindExpr(divOp.getRight());
        super.visit(divOp);

        return null;
    }

    @Override
    public Object visit(EQOp eqOp) {
        setKindExpr(eqOp.getLeft());
        setKindExpr(eqOp.getRight());
        super.visit(eqOp);

        return null;
    }

    @Override
    public Object visit(GEOp geOp) {
        setKindExpr(geOp.getLeft());
        setKindExpr(geOp.getRight());
        super.visit(geOp);

        return null;
    }

    @Override
    public Object visit(GTOp gtOp) {
        setKindExpr(gtOp.getLeft());
        setKindExpr(gtOp.getRight());
        super.visit(gtOp);

        return null;
    }

    @Override
    public Object visit(LEOp leOp) {
        setKindExpr(leOp.getLeft());
        setKindExpr(leOp.getRight());
        super.visit(leOp);

        return null;
    }

    @Override
    public Object visit(LTOp ltOp) {
        setKindExpr(ltOp.getLeft());
        setKindExpr(ltOp.getRight());
        super.visit(ltOp);

        return null;
    }

    @Override
    public Object visit(MulOp mulOp) {
        setKindExpr(mulOp.getLeft());
        setKindExpr(mulOp.getRight());
        super.visit(mulOp);

        return null;
    }

    @Override
    public Object visit(NEOp neOp) {
        setKindExpr(neOp.getLeft());
        setKindExpr(neOp.getRight());
        super.visit(neOp);

        return null;
    }

    @Override
    public Object visit(OrOp orOp) {
        setKindExpr(orOp.getLeft());
        setKindExpr(orOp.getRight());
        super.visit(orOp);

        return null;
    }

    @Override
    public Object visit(PowOp powOp) {
        setKindExpr(powOp.getLeft());
        setKindExpr(powOp.getRight());
        super.visit(powOp);

        return null;
    }

    @Override
    public Object visit(StrCatOp strCatOp) {
        setKindExpr(strCatOp.getLeft());
        setKindExpr(strCatOp.getRight());
        super.visit(strCatOp);

        return null;
    }

    @Override
    public Object visit(Cons cons) {
        return null;
    }

    @Override
    public Object visit(CharCons charCons) {
        return null;
    }

    @Override
    public Object visit(FalseCons falseCons) {
        return null;
    }

    @Override
    public Object visit(IntegerCons integerCons) {
        return null;
    }

    @Override
    public Object visit(RealCons realCons) {
        return null;
    }

    @Override
    public Object visit(StringCons stringCons) {
        return null;
    }

    @Override
    public Object visit(TrueCons trueCons) {
        return null;
    }

    @Override
    public Object visit(UnaryExpr unaryExpr) {
        return null;
    }

    @Override
    public Object visit(NotOp notOp) {
        setKindExpr(notOp.getExpr());
        super.visit(notOp);

        return null;
    }

    @Override
    public Object visit(ParOp parOp) {
        setKindExpr(parOp.getExpr());
        super.visit(parOp);

        return null;
    }

    @Override
    public Object visit(UminusOp uminusOp) {
        setKindExpr(uminusOp.getExpr());
        super.visit(uminusOp);

        return null;
    }

    /* Private Methods */

    private void setKindExpr(Expr expr) {
        if(expr instanceof Id id) {
            id.setKind(Kind.VAR);
        } else {
            expr.accept(this);
        }
    }

    private void setKindList(ArrayList<? extends Node> nodeList) {
        for(Node node : nodeList){
            if(node instanceof Expr expr){
                setKindExpr(expr);
            }
        }
    }

    private void setKindIdAndExprList(ArrayList<IdAndExpr> idAndExprList) {
        for(IdAndExpr idAndExpr : idAndExprList) {
            idAndExpr.getId().setKind(Kind.VAR);
            if(idAndExpr.getExpr() != null) {
                setKindExpr(idAndExpr.getExpr());
            }
        }
    }

}
