package main.syntaxtree.node;

import main.Visitor.Visitor;
import main.syntaxtree.wrapper.IdAndExpr;
import main.type.TypeValidator;

import java.util.ArrayList;

public class VarDeclOp extends Node {

    private TypeOp typeOp;
    private ArrayList<IdAndExpr> idAndExprList;

    public VarDeclOp(TypeOp typeOp, ArrayList<IdAndExpr> idAndExprList) {
        if(idAndExprList.isEmpty())
            throw new RuntimeException("idAndExprList non può essere vuota");
        if(!validTypeOp(typeOp))
            throw new RuntimeException("Il TypeOp inserito non ha un tipo valido.");

        this.typeOp = typeOp;
        this.idAndExprList = idAndExprList;
    }

    public TypeOp getTypeOp() {
        return typeOp;
    }

    public void setTypeOp(TypeOp typeOp) {
        if(!validTypeOp(typeOp))
            throw new RuntimeException("Il TypeOp inserito non ha un tipo valido.");

        this.typeOp = typeOp;
    }

    public ArrayList<IdAndExpr> getIdAndExprList() {
        return idAndExprList;
    }

    public void setIdAndExprList(ArrayList<IdAndExpr> idAndExprList) {
        this.idAndExprList = idAndExprList;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }

    private boolean validTypeOp(TypeOp typeOp) {
        TypeValidator typeValidator = new TypeValidator(typeOp.getType());
        if(typeValidator.validateBasicType()) return true;
        if(typeValidator.validateVar()) return true;
        return false;
    }
}
