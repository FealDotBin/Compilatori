package syntaxtree.node;

import type.Type;
import visitor.Visitor;
import syntaxtree.node.expr.Id;

import java.util.ArrayList;

public class ParDeclOp extends Node {

    private boolean isOut;
    private TypeOp typeOp;
    private ArrayList<Id> idList;

    public ParDeclOp(boolean isOut, TypeOp typeOp, ArrayList<Id> idList) {
        if(idList.isEmpty())
            throw new RuntimeException("idList non può essere vuota");
        if(!validTypeOp(typeOp))
            throw new RuntimeException("Il TypeOp inserito non ha un tipo valido.");

        this.isOut = isOut;
        this.typeOp = typeOp;
        this.idList = idList;
    }

    public boolean isOut() {
        return isOut;
    }

    public void setOut(boolean out) {
        isOut = out;
    }

    public TypeOp getTypeOp() {
        return typeOp;
    }

    public void setTypeOp(TypeOp typeOp) {
        if(!validTypeOp(typeOp))
            throw new RuntimeException("Il TypeOp inserito non ha un tipo valido.");

        this.typeOp = typeOp;
    }

    public ArrayList<Id> getIdList() {
        return idList;
    }

    public void setIdList(ArrayList<Id> idList) {
        this.idList = idList;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }

    private boolean validTypeOp(TypeOp typeOp) {
        if(Type.basicTypes.contains(typeOp.getAttribute())) return true;
        return false;
    }
}
