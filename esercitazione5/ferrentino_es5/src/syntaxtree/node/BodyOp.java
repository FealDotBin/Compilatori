package syntaxtree.node;

import visitor.Visitor;
import syntaxtree.node.stat.Stat;

import java.util.ArrayList;

public class BodyOp extends Node {

    private ArrayList<VarDeclOp> varDeclOpList;
    private ArrayList<Stat> statList;

    public BodyOp(ArrayList<VarDeclOp> varDeclOpList) {
        this.varDeclOpList = varDeclOpList;
        this.statList = new ArrayList<>();
    }

    public BodyOp(ArrayList<VarDeclOp> varDeclOpList, ArrayList<Stat> statList) {
        this.varDeclOpList = varDeclOpList;
        this.statList = statList;
    }

    public ArrayList<VarDeclOp> getVarDeclOpList() {
        return varDeclOpList;
    }

    public void setVarDeclOpList(ArrayList<VarDeclOp> varDeclOpList) {
        this.varDeclOpList = varDeclOpList;
    }

    public ArrayList<Stat> getStatList() {
        return statList;
    }

    public void setStatList(ArrayList<Stat> statList) {
        this.statList = statList;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}