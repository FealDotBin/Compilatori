package visitor;

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

public abstract class AbstractVisitor implements Visitor {

    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        visitList(bodyOp.getVarDeclOpList());
        visitList(bodyOp.getStatList());

        return null;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) {
        funDeclOp.getId().accept(this);
        visitList(funDeclOp.getParDeclOpList());
        funDeclOp.getTypeOp().accept(this);
        funDeclOp.getBodyOp().accept(this);

        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        parDeclOp.getTypeOp().accept(this);
        visitList(parDeclOp.getIdList());

        return null;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        visitList(programOp.getVarDeclOpList());
        visitList(programOp.getFunDeclOpList());

        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        varDeclOp.getTypeOp().accept(this);
        visitIdAndExprList(varDeclOp.getIdAndExprList());

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
        visitList(assignOp.getIdList());
        visitList(assignOp.getExprList());

        return null;
    }

    @Override
    public Object visit(ForOp forOp) {
        forOp.getId().accept(this);
        forOp.getFromValue().accept(this);
        forOp.getToValue().accept(this);
        forOp.getBodyOp().accept(this);

        return null;
    }

    @Override
    public Object visit(IfOp ifOp) {
        ifOp.getExpr().accept(this);
        ifOp.getBodyOp().accept(this);
        BodyOp bodyOpElse = ifOp.getBodyOpElse();
        if(bodyOpElse != null) {
            bodyOpElse.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        visitList(readOp.getIdList());
        Expr expr = readOp.getExpr();
        if(expr != null) {
            expr.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        Expr expr = returnOp.getExpr();

        if(expr != null) {
            expr.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        whileOp.getExpr().accept(this);
        whileOp.getBodyOp().accept(this);

        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        visitList(writeOp.getExprList());

        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        funCallOpExpr.getId().accept(this);
        visitList(funCallOpExpr.getExprList());

        return null;
    }

    @Override
    public Object visit(Id id) {
        return null;
    }

    @Override
    public Object visit(BinaryExpr binaryExpr) {
        visitBinaryExpr(binaryExpr);

        return null;
    }

    @Override
    public Object visit(AddOp addOp) {
        visitBinaryExpr(addOp);

        return null;
    }

    @Override
    public Object visit(AndOp andOp) {
        visitBinaryExpr(andOp);

        return null;
    }

    @Override
    public Object visit(DiffOp diffOp) {
        visitBinaryExpr(diffOp);

        return null;
    }

    @Override
    public Object visit(DivOp divOp) {
        visitBinaryExpr(divOp);

        return null;
    }

    @Override
    public Object visit(EQOp eqOp) {
        visitBinaryExpr(eqOp);

        return null;
    }

    @Override
    public Object visit(GEOp geOp) {
        visitBinaryExpr(geOp);

        return null;
    }

    @Override
    public Object visit(GTOp gtOp) {
        visitBinaryExpr(gtOp);

        return null;
    }

    @Override
    public Object visit(LEOp leOp) {
        visitBinaryExpr(leOp);

        return null;
    }

    @Override
    public Object visit(LTOp ltOp) {
        visitBinaryExpr(ltOp);

        return null;
    }

    @Override
    public Object visit(MulOp mulOp) {
        visitBinaryExpr(mulOp);

        return null;
    }

    @Override
    public Object visit(NEOp neOp) {
        visitBinaryExpr(neOp);

        return null;
    }

    @Override
    public Object visit(OrOp orOp) {
        visitBinaryExpr(orOp);

        return null;
    }

    @Override
    public Object visit(PowOp powOp) {
        visitBinaryExpr(powOp);

        return null;
    }

    @Override
    public Object visit(StrCatOp strCatOp) {
        visitBinaryExpr(strCatOp);

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
        visitUnaryExpr(unaryExpr);

        return null;
    }

    @Override
    public Object visit(NotOp notOp) {
        visitUnaryExpr(notOp);

        return null;
    }

    @Override
    public Object visit(ParOp parOp) {
        visitUnaryExpr(parOp);

        return null;
    }

    @Override
    public Object visit(UminusOp uminusOp) {
        visitUnaryExpr(uminusOp);

        return null;
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        funCallOpStat.getId().accept(this);
        visitList(funCallOpStat.getExprList());

        return null;
    }

    /* Private Methods */

    private void visitList(ArrayList<? extends Node> nodeList) {
        for(Node node : nodeList){
            node.accept(this);
        }
    }

    private void visitIdAndExprList(ArrayList<IdAndExpr> idAndExprList) {
        for(IdAndExpr idAndExpr : idAndExprList) {
            idAndExpr.getId().accept(this);
            Expr expr = idAndExpr.getExpr();
            if(expr != null) {
                expr.accept(this);
            }
        }
    }

    private void visitBinaryExpr(BinaryExpr binaryExpr) {
        binaryExpr.getLeft().accept(this);
        binaryExpr.getRight().accept(this);
    }

    private void visitUnaryExpr(UnaryExpr unaryExpr) {
        unaryExpr.getExpr().accept(this);
    }
}
