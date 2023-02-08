package main.syntaxtree.node;

import main.Visitor.Visitor;
import main.syntaxtree.node.expr.Id;
import main.type.TypeValidator;

import java.util.ArrayList;

public class FunDeclOp extends Node {

    private boolean isMain;
    private Id id;
    private ArrayList<ParDeclOp> parDeclOpList;
    private TypeOp typeOp;
    private BodyOp bodyOp;

    public FunDeclOp(boolean isMain, Id id, ArrayList<ParDeclOp> parDeclOpList, TypeOp typeOp, BodyOp bodyOp) {
        if(!validTypeOp(typeOp))
            throw new RuntimeException("Il TypeOp inserito non ha un tipo valido.");

        this.isMain = isMain;
        this.id = id;
        this.parDeclOpList = parDeclOpList;
        this.typeOp = typeOp;
        this.bodyOp = bodyOp;
    }

    public FunDeclOp(Id id, ArrayList<ParDeclOp> parDeclOpList, TypeOp typeOp, BodyOp bodyOp) {
        if(!validTypeOp(typeOp))
            throw new RuntimeException("Il TypeOp inserito non ha un tipo valido.");

        this.isMain = false;
        this.id = id;
        this.parDeclOpList = parDeclOpList;
        this.typeOp = typeOp;
        this.bodyOp = bodyOp;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public ArrayList<ParDeclOp> getParDeclOpList() {
        return parDeclOpList;
    }

    public void setParDeclOpList(ArrayList<ParDeclOp> parDeclOpList) {
        this.parDeclOpList = parDeclOpList;
    }

    public TypeOp getTypeOp() {
        return typeOp;
    }

    public void setTypeOp(TypeOp typeOp) {
        if(!validTypeOp(typeOp))
            throw new RuntimeException("Il TypeOp inserito non ha un tipo valido.");

        this.typeOp = typeOp;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
    }

    public boolean isMain() {
        return isMain;
    }

    public void setMain(boolean main) {
        isMain = main;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }

    private boolean validTypeOp(TypeOp typeOp) {
        TypeValidator typeValidator = new TypeValidator(typeOp.getType());
        if(typeValidator.validateBasicType()) return true;
        if(typeValidator.validateVoid()) return true;
        return false;
    }
}
