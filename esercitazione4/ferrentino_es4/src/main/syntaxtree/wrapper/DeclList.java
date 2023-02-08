package main.syntaxtree.wrapper;

import main.syntaxtree.node.FunDeclOp;
import main.syntaxtree.node.VarDeclOp;

import java.util.ArrayList;

public class DeclList {
    private ArrayList<VarDeclOp> varDeclOpList;
    private ArrayList<FunDeclOp> funDeclOpList;

    public DeclList(){
        this.varDeclOpList = new ArrayList<>();
        this.funDeclOpList = new ArrayList<>();
    }
    public DeclList(ArrayList<VarDeclOp> varDeclOpList, ArrayList<FunDeclOp> funDeclOpList){
        this.varDeclOpList = varDeclOpList;
        this.funDeclOpList = funDeclOpList;
    }

    public ArrayList<VarDeclOp> getVarDeclOpList(){
        return varDeclOpList;
    }

    public ArrayList<FunDeclOp> getFunDeclOpList(){
        return funDeclOpList;
    }

    public void setVarDeclOpList(ArrayList<VarDeclOp> varDeclOpList){
        this.varDeclOpList = varDeclOpList;
    }

    public void setFunDeclOpList(ArrayList<FunDeclOp> funDeclOpList){
        this.funDeclOpList = funDeclOpList;
    }

    public void merge(DeclList declList){
        ArrayList<FunDeclOp> funDeclOpList = declList.getFunDeclOpList();
        this.funDeclOpList.addAll(funDeclOpList);

        ArrayList<VarDeclOp> varDeclOpList = declList.getVarDeclOpList();
        this.varDeclOpList.addAll(varDeclOpList);
    }
}
