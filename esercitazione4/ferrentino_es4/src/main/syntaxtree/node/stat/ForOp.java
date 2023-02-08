package main.syntaxtree.node.stat;

import main.Visitor.Visitor;
import main.syntaxtree.node.BodyOp;
import main.syntaxtree.node.expr.Id;
import main.syntaxtree.node.expr.cons.IntegerCons;

public class ForOp extends Stat {

    private Id id;
    private IntegerCons fromValue;
    private IntegerCons toValue;
    private BodyOp bodyOp;

    public ForOp(Id id, IntegerCons fromValue, IntegerCons toValue, BodyOp bodyOp) {
        this.id = id;
        this.fromValue = fromValue;
        this.toValue = toValue;
        this.bodyOp = bodyOp;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public IntegerCons getFromValue() {
        return fromValue;
    }

    public void setFromValue(IntegerCons fromValue) {
        this.fromValue = fromValue;
    }

    public IntegerCons getToValue() {
        return toValue;
    }

    public void setToValue(IntegerCons toValue) {
        this.toValue = toValue;
    }

    public BodyOp getBodyOp() {
        return bodyOp;
    }

    public void setBodyOp(BodyOp bodyOp) {
        this.bodyOp = bodyOp;
    }

    public Object accept(Visitor visitor){
        return visitor.visit(this);
    }
}
