package syntaxtree.node;

import type.Type;
import visitor.Visitor;

public class TypeOp extends Node {

    private String attribute;

    public TypeOp(String attribute) {
        if(!checkAttribute(attribute))
            throw new RuntimeException("Impossibile creare TypeOp con questo attributo");
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        if(!checkAttribute(attribute))
            throw new RuntimeException("Impossibile creare TypeOp con questo attributo");
        this.attribute = attribute;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }

    private boolean checkAttribute(String type) {
        return (Type.VAR.equals(type) || Type.VOID.equals(type) || Type.basicTypes.contains(type));
    }
}
