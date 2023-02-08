package main.Visitor;

import main.syntaxtree.node.*;
import main.syntaxtree.node.stat.*;
import main.syntaxtree.node.expr.*;
import main.syntaxtree.node.expr.binaryexpr.*;
import main.syntaxtree.node.expr.cons.*;
import main.syntaxtree.node.expr.unaryexpr.*;

public interface Visitor {

    Object visit(Node node);

    Object visit(BodyOp bodyOp);

    Object visit(FunDeclOp funDeclOp);

    Object visit(ParDeclOp parDeclOp);

    Object visit(ProgramOp programOp);

    Object visit(VarDeclOp varDeclOp);

    Object visit(TypeOp typeOp);

    Object visit(Stat stat);

    Object visit(AssignOp assignOp);

    Object visit(ForOp forOp);

    Object visit(IfOp ifOp);

    Object visit(ReadOp readOp);

    Object visit(ReturnOp returnOp);

    Object visit(WhileOp whileOp);

    Object visit(WriteOp writeOp);

    Object visit(Expr expr);

    Object visit(FunCallOpExpr funCallOpExpr);

    Object visit(Id id);

    Object visit(BinaryExpr binaryExpr);

    Object visit(AddOp addOp);

    Object visit(AndOp andOp);

    Object visit(DiffOp diffOp);

    Object visit(DivIntOp divIntOp);

    Object visit(DivOp divOp);

    Object visit(EQOp eqOp);

    Object visit(GEOp geOp);

    Object visit(GTOp gtOp);

    Object visit(LEOp leOp);

    Object visit(LTOp ltOp);

    Object visit(MulOp mulOp);

    Object visit(NEOp neOp);

    Object visit(OrOp orOp);

    Object visit(PowOp powOp);

    Object visit(StrCatOp strCatOp);

    Object visit(Cons cons);

    Object visit(CharCons charCons);

    Object visit(FalseCons falseCons);

    Object visit(IntegerCons integerCons);

    Object visit(RealCons realCons);

    Object visit(StringCons stringCons);

    Object visit(TrueCons trueCons);

    Object visit(UnaryExpr unaryExpr);

    Object visit(NotOp notOp);

    Object visit(ParOp parOp);

    Object visit(UminusOp uminusOp);

    Object visit(FunCallOpStat funCallOpStat);

}
