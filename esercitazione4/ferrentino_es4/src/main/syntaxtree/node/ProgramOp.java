package main.syntaxtree.node;

import main.Visitor.Visitor;

import java.util.ArrayList;

public class ProgramOp extends Node {
    private ArrayList<VarDeclOp> varDeclOpList;
    private ArrayList<FunDeclOp> funDeclOpList;

    public ProgramOp(ArrayList<VarDeclOp> varDeclOpList, ArrayList<FunDeclOp> funDeclOpList) {
        if(funDeclOpList.isEmpty())
            throw new RuntimeException("funDeclOpList non può essere vuota!");
        this.varDeclOpList = varDeclOpList;
        this.funDeclOpList = funDeclOpList;
    }

    public ArrayList<VarDeclOp> getVarDeclOpList() {
        return varDeclOpList;
    }

    public void setVarDeclOpList(ArrayList<VarDeclOp> varDeclOpList) {
        this.varDeclOpList = varDeclOpList;
    }

    public ArrayList<FunDeclOp> getFunDeclOpList() {
        return funDeclOpList;
    }

    public void setFunDeclOpList(ArrayList<FunDeclOp> funDeclOpList) {
        this.funDeclOpList = funDeclOpList;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
