package syntaxtree.node;

import type.Type;
import visitor.Visitor;
import syntaxtree.wrapper.IdAndExpr;

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
        String type = typeOp.getAttribute();
        if(Type.basicTypes.contains(type))
            return true;
        if(type.equals(Type.VAR))
            return true;
        return false;
    }
}
