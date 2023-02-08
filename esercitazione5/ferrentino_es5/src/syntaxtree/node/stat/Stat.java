package syntaxtree.node.stat;

import visitor.Visitor;
import syntaxtree.node.Node;

public abstract class Stat extends Node {

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
