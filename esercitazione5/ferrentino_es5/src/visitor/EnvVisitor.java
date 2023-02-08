package visitor;

import env.*;
import env.enventry.FunEnvEntry;
import env.enventry.ParamType;
import env.enventry.VarEnvEntry;
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
import type.Type;

import java.util.ArrayList;
import java.util.LinkedList;

public class EnvVisitor extends AbstractVisitor implements Visitor {

    private LinkedList<BodyOp> bannedBodies;

    public EnvVisitor() {
        bannedBodies = new LinkedList<>();
    }

    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        Env currEnv = bodyOp.getEnv();

        // controllo se creare un nuovo env
        if(!bannedBodies.contains(bodyOp)) {
            currEnv = new Env(currEnv);
            bodyOp.setEnv(currEnv);
        }

        // setto l'env ai figli
        setEnvList(bodyOp.getVarDeclOpList(), currEnv);
        setEnvList(bodyOp.getStatList(), currEnv);

        // visito i figli
        super.visit(bodyOp);

        return null;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) {
        Env currEnv = funDeclOp.getEnv();

        // colleziono le informazioni per la costruzione della symbol table entry rel. ad una funzione
        String kind = funDeclOp.getId().getKind();
        String id = funDeclOp.getId().getAttribute();
        String returnType = funDeclOp.getTypeOp().getAttribute();

        // costruisco la paramTypeList
        ArrayList<ParamType> paramTypeList = new ArrayList<>();
        for (ParDeclOp parDeclOp : funDeclOp.getParDeclOpList()) {
            String type = parDeclOp.getTypeOp().getAttribute();
            boolean isOut = parDeclOp.isOut();

            // aggiungo un paramType per ogni Id che ha quel type
            for(Id idObj : parDeclOp.getIdList()) {
                ParamType paramType = new ParamType(type, isOut);
                paramTypeList.add(paramType);
            }
        }

        // costruisco la entry e la inserisco nello scope del parent
        FunEnvEntry funEnvEntry = new FunEnvEntry(kind, id, returnType, paramTypeList);
        currEnv.put(kind, id, funEnvEntry);

        // creo un nuovo scope e setto l'env al nodo
        currEnv = new Env(currEnv);
        funDeclOp.setEnv(currEnv);

        // setto l'env ai figli
        funDeclOp.getId().setEnv(currEnv);
        setEnvList(funDeclOp.getParDeclOpList(), currEnv);
        funDeclOp.getTypeOp().setEnv(currEnv);
        funDeclOp.getBodyOp().setEnv(currEnv);

        // proibisco al body di creare uno scope
        bannedBodies.add(funDeclOp.getBodyOp());

        // visito i figli
        super.visit(funDeclOp);

        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        Env currEnv = parDeclOp.getEnv();

        // aggiungo i parametri allo scope corrente
        for(Id idObj : parDeclOp.getIdList()){
            String kind = idObj.getKind();
            String id = idObj.getAttribute();
            boolean isOut = parDeclOp.isOut();
            String type = parDeclOp.getTypeOp().getAttribute();

            VarEnvEntry varEnvEntry = new VarEnvEntry(kind, id, isOut, type);
            currEnv.put(kind, id, varEnvEntry);
        }

        // setto l'env ai figli
        parDeclOp.getTypeOp().setEnv(currEnv);
        setEnvList(parDeclOp.getIdList(), currEnv);

        // visito i figli
        super.visit(parDeclOp);

        return null;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        Env currEnv = new Env();
        programOp.setEnv(currEnv);

        // setto l'env ai figli
        setEnvList(programOp.getVarDeclOpList(), currEnv);
        setEnvList(programOp.getFunDeclOpList(), currEnv);

        // visito i figli
        super.visit(programOp);

        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        Env currEnv = varDeclOp.getEnv();

        // aggiungo i parametri allo scope corrente
        for(IdAndExpr idAndExpr : varDeclOp.getIdAndExprList()) {
            String kind = idAndExpr.getId().getKind();
            String id = idAndExpr.getId().getAttribute();
            String type = varDeclOp.getTypeOp().getAttribute();
            if(type == Type.VAR) {
                type = (String) idAndExpr.getExpr().accept(this);
            }

            VarEnvEntry varEnvEntry = new VarEnvEntry(kind, id, type);
            currEnv.put(kind, id, varEnvEntry);
        }

        // setto l'env ai figli
        varDeclOp.getTypeOp().setEnv(currEnv);
        setEnvIdAndExprList(varDeclOp.getIdAndExprList(), currEnv);

        // visito i figli
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
        Env currEnv = assignOp.getEnv();

        // setto l'env ai figli
        setEnvList(assignOp.getIdList(), currEnv);
        setEnvList(assignOp.getExprList(), currEnv);

        // visito i figli
        super.visit(assignOp);

        return null;
    }

    @Override
    public Object visit(ForOp forOp) {
        Env currEnv = forOp.getEnv();

        // creo un nuovo scope e setto l'env al nodo
        currEnv = new Env(currEnv);
        forOp.setEnv(currEnv);

        // aggiungo la variabile al nuovo scope
        String kind = forOp.getId().getKind();
        String id = forOp.getId().getAttribute();
        String type = Type.INT;

        VarEnvEntry varEnvEntry = new VarEnvEntry(kind, id, type);
        currEnv.put(kind, id, varEnvEntry);

        // setto l'env ai figli
        forOp.getId().setEnv(currEnv);
        forOp.getFromValue().setEnv(currEnv);
        forOp.getToValue().setEnv(currEnv);
        forOp.getBodyOp().setEnv(currEnv);

        // proibisco al body di creare uno scope
        bannedBodies.add(forOp.getBodyOp());

        // visito i figli
        super.visit(forOp);

        return null;
    }

    @Override
    public Object visit(IfOp ifOp) {
        Env currEnv = ifOp.getEnv();

        // setto l'env ai figli
        ifOp.getExpr().setEnv(currEnv);
        ifOp.getBodyOp().setEnv(currEnv);
        BodyOp bodyOpElse = ifOp.getBodyOpElse();
        if(bodyOpElse != null) {
            bodyOpElse.setEnv(currEnv);
        }

        // visito i figli
        super.visit(ifOp);

        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        Env currEnv = readOp.getEnv();

        // setto l'env ai figli
        setEnvList(readOp.getIdList(), currEnv);
        Expr expr = readOp.getExpr();
        if(expr != null) {
            expr.setEnv(currEnv);
        }

        // visito i figli
        super.visit(readOp);

        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        Env currEnv = returnOp.getEnv();

        // setto l'env ai figli
        Expr expr = returnOp.getExpr();
        if(expr != null) {
            expr.setEnv(currEnv);
        }

        // visito i figli
        super.visit(returnOp);

        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        Env currEnv = whileOp.getEnv();

        // setto l'env ai figli
        whileOp.getExpr().setEnv(currEnv);
        whileOp.getBodyOp().setEnv(currEnv);

        // visito i figli
        super.visit(whileOp);

        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        Env currEnv = writeOp.getEnv();

        // setto l'env ai figli
        setEnvList(writeOp.getExprList(), currEnv);

        // visito i figli
        super.visit(writeOp);

        return null;
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        Env currEnv = funCallOpStat.getEnv();

        // setto l'env ai figli
        funCallOpStat.getId().setEnv(currEnv);
        setEnvList(funCallOpStat.getExprList(), currEnv);

        // visito i figli
        super.visit(funCallOpStat);

        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        Env currEnv = funCallOpExpr.getEnv();

        // setto l'env ai figli
        funCallOpExpr.getId().setEnv(currEnv);
        setEnvList(funCallOpExpr.getExprList(), currEnv);

        // visito i figli
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
        setEnvBinaryExpr(addOp);
        super.visit(addOp);

        return null;
    }

    @Override
    public Object visit(AndOp andOp) {
        setEnvBinaryExpr(andOp);
        super.visit(andOp);

        return null;
    }

    @Override
    public Object visit(DiffOp diffOp) {
        setEnvBinaryExpr(diffOp);
        super.visit(diffOp);

        return null;
    }

    @Override
    public Object visit(DivOp divOp) {
        setEnvBinaryExpr(divOp);
        super.visit(divOp);

        return null;
    }

    @Override
    public Object visit(EQOp eqOp) {
        setEnvBinaryExpr(eqOp);
        super.visit(eqOp);

        return null;
    }

    @Override
    public Object visit(GEOp geOp) {
        setEnvBinaryExpr(geOp);
        super.visit(geOp);

        return null;
    }

    @Override
    public Object visit(GTOp gtOp) {
        setEnvBinaryExpr(gtOp);
        super.visit(gtOp);

        return null;
    }

    @Override
    public Object visit(LEOp leOp) {
        setEnvBinaryExpr(leOp);
        super.visit(leOp);

        return null;
    }

    @Override
    public Object visit(LTOp ltOp) {
        setEnvBinaryExpr(ltOp);
        super.visit(ltOp);

        return null;
    }

    @Override
    public Object visit(MulOp mulOp) {
        setEnvBinaryExpr(mulOp);
        super.visit(mulOp);

        return null;
    }

    @Override
    public Object visit(NEOp neOp) {
        setEnvBinaryExpr(neOp);
        super.visit(neOp);

        return null;
    }

    @Override
    public Object visit(OrOp orOp) {
        setEnvBinaryExpr(orOp);
        super.visit(orOp);

        return null;
    }

    @Override
    public Object visit(PowOp powOp) {
        setEnvBinaryExpr(powOp);
        super.visit(powOp);

        return null;
    }

    @Override
    public Object visit(StrCatOp strCatOp) {
        setEnvBinaryExpr(strCatOp);
        super.visit(strCatOp);

        return null;
    }

    @Override
    public Object visit(Cons cons) {
        return null;
    }

    @Override
    public Object visit(CharCons charCons) {
        return Type.CHAR;
    }

    @Override
    public Object visit(FalseCons falseCons) {
        return Type.BOOL;
    }

    @Override
    public Object visit(IntegerCons integerCons) {
        return Type.INT;
    }

    @Override
    public Object visit(RealCons realCons) {
        return Type.REAL;
    }

    @Override
    public Object visit(StringCons stringCons) {
        return Type.STRING;
    }

    @Override
    public Object visit(TrueCons trueCons) {
        return Type.BOOL;
    }

    @Override
    public Object visit(UnaryExpr unaryExpr) {
        return null;
    }

    @Override
    public Object visit(NotOp notOp) {
        setEnvUnaryExpr(notOp);
        super.visit(notOp);

        return null;
    }

    @Override
    public Object visit(ParOp parOp) {
        setEnvUnaryExpr(parOp);
        super.visit(parOp);

        return null;
    }

    @Override
    public Object visit(UminusOp uminusOp) {
        setEnvUnaryExpr(uminusOp);
        super.visit(uminusOp);

        return null;
    }

    /* Private Methods */

    private void setEnvUnaryExpr(UnaryExpr unaryExpr) {
        Env currEnv = unaryExpr.getEnv();

        // setto l'env al nodo
        unaryExpr.getExpr().setEnv(currEnv);
    }

    private void setEnvBinaryExpr(BinaryExpr binaryExpr) {
        Env currEnv = binaryExpr.getEnv();

        // setto l'env ai figli
        binaryExpr.getLeft().setEnv(currEnv);
        binaryExpr.getRight().setEnv(currEnv);
    }

    private void setEnvList(ArrayList<? extends Node> nodeList, Env currEnv) {
        for(Node node : nodeList){
            node.setEnv(currEnv);
        }
    }

    private void setEnvIdAndExprList(ArrayList<IdAndExpr> idAndExprList, Env currEnv) {
        for(IdAndExpr idAndExpr : idAndExprList) {
            idAndExpr.getId().setEnv(currEnv);
            Expr expr = idAndExpr.getExpr();
            if(expr != null) {
                expr.setEnv(currEnv);
            }
        }
    }
}
