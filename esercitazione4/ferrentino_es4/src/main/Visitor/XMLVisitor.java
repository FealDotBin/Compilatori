package main.Visitor;

import main.syntaxtree.node.*;
import main.syntaxtree.node.stat.*;
import main.syntaxtree.node.expr.*;
import main.syntaxtree.node.expr.binaryexpr.*;
import main.syntaxtree.node.expr.cons.*;
import main.syntaxtree.node.expr.unaryexpr.*;
import main.syntaxtree.wrapper.IdAndExpr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

public class XMLVisitor implements Visitor {

    private Document document;

    public XMLVisitor() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        document = documentBuilder.newDocument();
    }

    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        // creo l'elemento col nome della classe
        String className = bodyOp.getClass().getSimpleName();
        Element bodyOpElement = document.createElement(className);

        // appendo i nodi "VarDeclOp"
        bodyOpElement.appendChild((Element) visitList(bodyOp.getVarDeclOpList(), "VarDeclOp"));

        // appendo i nodi "Stat"
        bodyOpElement.appendChild((Element) visitList(bodyOp.getStatList(), "Stat"));

        return bodyOpElement;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) {
        // creo l'elemento col nome della classe
        String className = funDeclOp.getClass().getSimpleName();
        Element funDeclOpElement = document.createElement(className);

        // creo e appendo il nodo "isMain"
        Element isMainElement = document.createElement("isMain");
        String isMain = Boolean.toString(funDeclOp.isMain());
        isMainElement.appendChild(document.createTextNode(isMain));
        funDeclOpElement.appendChild(isMainElement);

        // appendo il nodo "id"
        funDeclOpElement.appendChild((Element) funDeclOp.getId().accept(this));

        // appendo i nodi "ParDeclOp"
        funDeclOpElement.appendChild((Element) visitList(funDeclOp.getParDeclOpList(), "ParDeclOp"));

        // appendo il nodo "TypeOp"
        funDeclOpElement.appendChild((Element) funDeclOp.getTypeOp().accept(this));

        // appendo il nodo "BodyOp"
        funDeclOpElement.appendChild((Element) funDeclOp.getBodyOp().accept(this));

        return funDeclOpElement;

    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        // creo l'elemento col nome della classe
        String className = parDeclOp.getClass().getSimpleName();
        Element parDeclOpElement = document.createElement(className);

        // creo ed appendo il nodo "isOut"
        Element isOutElement = document.createElement("isOut");
        String isOut = Boolean.toString(parDeclOp.isOut());
        isOutElement.appendChild(document.createTextNode(isOut));
        parDeclOpElement.appendChild(isOutElement);

        // appendo il nodo TypeOp
        parDeclOpElement.appendChild((Element) parDeclOp.getType().accept(this));

        // appendo i nodi "Id"
        parDeclOpElement.appendChild((Element) visitList(parDeclOp.getIdList(), "Id"));

        return parDeclOpElement;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        // creo l'elemento col nome della classe
        String className = programOp.getClass().getSimpleName();
        Element programOpElement = document.createElement(className);

        // appendo i nodi VarDeclOp
        programOpElement.appendChild((Element) visitList(programOp.getVarDeclOpList(), "VarDeclOp"));

        // appendo i nodi FunDeclOp
        programOpElement.appendChild((Element) visitList(programOp.getFunDeclOpList(), "FunDeclOp"));

        // appendo l'elemento al documento finale
        document.appendChild(programOpElement);
        return document;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        // creo l'elemento col nome della classe
        String className = varDeclOp.getClass().getSimpleName();
        Element varDeclOpElement = document.createElement(className);

        // appendo il nodo TypeOp
        varDeclOpElement.appendChild((Element) varDeclOp.getTypeOp().accept(this));

        // appendo i nodi contenuti nel wrapper IdAndExpr
        varDeclOpElement.appendChild((Element) visitIdAndExprList(varDeclOp.getIdAndExprList()));

        return varDeclOpElement;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        // creo l'elemento col nome della classe
        String className = typeOp.getClass().getSimpleName();
        Element typeOpElement = document.createElement(className);

        // creo e appendo il nodo con la stringa type
        typeOpElement.appendChild(document.createTextNode(typeOp.getType()));

        return typeOpElement;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        // creo l'elemento col nome della classe
        String className = assignOp.getClass().getSimpleName();
        Element assignOpElement = document.createElement(className);

        // appendo i nodi id
        assignOpElement.appendChild((Element) visitList(assignOp.getIdList(), "Id"));

        // appendo i nodi expr
        assignOpElement.appendChild((Element) visitList(assignOp.getExprList(), "Expr"));

        return assignOpElement;
    }

    @Override
    public Object visit(ForOp forOp) {
        // creo l'elemento col nome della classe
        String className = forOp.getClass().getSimpleName();
        Element forOpElement = document.createElement(className);

        // appendo il nodo id
        forOpElement.appendChild((Element) forOp.getId().accept(this));

        // appendo il nodo fromValue
        forOpElement.appendChild((Element) forOp.getFromValue().accept(this));

        // appendo il nodo toValue
        forOpElement.appendChild((Element) forOp.getToValue().accept(this));

        // appendo il nodo bodyOp
        forOpElement.appendChild((Element) forOp.getBodyOp().accept(this));

        return forOpElement;
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        // creo l'elemento col nome della classe
        String className = funCallOpStat.getClass().getSimpleName();
        Element funCallOpStatElement = document.createElement(className);

        // appendo il nodo id
        funCallOpStatElement.appendChild((Element) funCallOpStat.getId().accept(this));

        // appendo i nodi expr
        funCallOpStatElement.appendChild((Element) visitList(funCallOpStat.getExprList(), "Expr"));

        return funCallOpStatElement;
    }

    @Override
    public Object visit(IfOp ifOp) {
        // creo l'elemento col nome della classe
        String className = ifOp.getClass().getSimpleName();
        Element ifOpElement = document.createElement(className);

        // appendo il nodo expr
        ifOpElement.appendChild((Element) ifOp.getExpr().accept(this));

        // appendo il nodo bodyOp
        ifOpElement.appendChild((Element) ifOp.getBodyOp().accept(this));

        // appendo il nodo bodyOpElse (se presente)
        BodyOp bodyOpElse = ifOp.getBodyOpElse();
        if(bodyOpElse != null){
            ifOpElement.appendChild((Element) bodyOpElse.accept(this));
        }

        return ifOpElement;
    }

    @Override
    public Object visit(ReadOp readOp) {
        // creo l'elemento col nome della classe
        String className = readOp.getClass().getSimpleName();
        Element readOpElement = document.createElement(className);

        // appendo i nodi id
        readOpElement.appendChild((Element) visitList(readOp.getIdList(),"Id"));

        // appendo il nodo expr (se presente)
        Expr expr = readOp.getExpr();
        if(expr != null){
            readOpElement.appendChild((Element) expr.accept(this));
        }

        return readOpElement;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        // creo l'elemento col nome della classe
        String className = returnOp.getClass().getSimpleName();
        Element returnOpElement = document.createElement(className);

        // appendo il nodo expr (se presente)
        Expr expr = returnOp.getExpr();
        if(expr != null){
            returnOpElement.appendChild((Element) expr.accept(this));
        }

        return returnOpElement;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        // creo l'elemento col nome della classe
        String className = whileOp.getClass().getSimpleName();
        Element whileOpElement = document.createElement(className);

        // appendo il nodo expr
        whileOpElement.appendChild((Element) whileOp.getExpr().accept(this));

        // appendo il nodo bodyOp
        whileOpElement.appendChild((Element) whileOp.getBodyOp().accept(this));

        return whileOpElement;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        // creo l'elemento col nome della classe
        String className = writeOp.getClass().getSimpleName();
        Element writeOpElement = document.createElement(className);

        // creo e appendo il nodo isLn
        Element isLnElement = document.createElement("isLn");
        String isLn = Boolean.toString(writeOp.isLn());
        isLnElement.appendChild(document.createTextNode(isLn));
        writeOpElement.appendChild(isLnElement);

        // appendo i nodi expr
        writeOpElement.appendChild((Element) visitList(writeOp.getExprList(), "Expr"));

        return writeOpElement;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        // creo l'elemento col nome della classe
        String className = funCallOpExpr.getClass().getSimpleName();
        Element funCallOpExprElement = document.createElement(className);

        // appendo il nodo id
        funCallOpExprElement.appendChild((Element) funCallOpExpr.getId().accept(this));

        // appendo i nodi expr
        funCallOpExprElement.appendChild((Element) visitList(funCallOpExpr.getExprList(), "Expr"));

        return funCallOpExprElement;
    }

    @Override
    public Object visit(Id id) {
        // creo l'elemento col nome della classe
        String className = id.getClass().getSimpleName();
        Element idElement = document.createElement(className);

        // creo e appendo il nodo con la stringa attribute (se presente)
        String attribute = id.getAttribute();
        if(attribute != null){
            idElement.appendChild(document.createTextNode(attribute));
        }

        return idElement;
    }

    public Object visit(BinaryExpr binaryExpr){
        // creo l'elemento col nome della classe
        String className = binaryExpr.getClass().getSimpleName();
        Element binaryExprElement = document.createElement(className);

        binaryExprElement.appendChild((Element) binaryExpr.getLeft().accept(this));
        binaryExprElement.appendChild((Element) binaryExpr.getRight().accept(this));

        return binaryExprElement;
    }

    @Override
    public Object visit(AddOp addOp) {
        return visit((BinaryExpr) addOp);
    }

    @Override
    public Object visit(AndOp andOp) {
        return visit((BinaryExpr) andOp);
    }

    @Override
    public Object visit(DiffOp diffOp) {
        return visit((BinaryExpr) diffOp);
    }

    @Override
    public Object visit(DivIntOp divIntOp) {
        return visit((BinaryExpr) divIntOp);
    }

    @Override
    public Object visit(DivOp divOp) {
        return visit((BinaryExpr) divOp);
    }

    @Override
    public Object visit(EQOp eqOp) {
        return visit((BinaryExpr) eqOp);
    }

    @Override
    public Object visit(GEOp geOp) {
        return visit((BinaryExpr) geOp);
    }

    @Override
    public Object visit(GTOp gtOp) {
        return visit((BinaryExpr) gtOp);
    }

    @Override
    public Object visit(LEOp leOp) {
        return visit((BinaryExpr) leOp);
    }

    @Override
    public Object visit(LTOp ltOp) {
        return visit((BinaryExpr) ltOp);
    }

    @Override
    public Object visit(MulOp mulOp) {
        return visit((BinaryExpr) mulOp);
    }

    @Override
    public Object visit(NEOp neOp) {
        return visit((BinaryExpr) neOp);
    }

    @Override
    public Object visit(OrOp orOp) {
        return visit((BinaryExpr) orOp);
    }

    @Override
    public Object visit(PowOp powOp) {
        return visit((BinaryExpr) powOp);
    }

    @Override
    public Object visit(StrCatOp strCatOp) {
        return visit((BinaryExpr) strCatOp);
    }

    @Override
    public Object visit(Cons cons) {
        return null;
    }

    @Override
    public Object visit(CharCons charCons) {
        // creo l'elemento col nome della classe
        String className = charCons.getClass().getSimpleName();
        Element charConsElement = document.createElement(className);

        // creo ed appendo il nodo con la stringa attribute
        charConsElement.appendChild(document.createTextNode(charCons.getAttribute()));

        return charConsElement;
    }

    @Override
    public Object visit(FalseCons falseCons) {
        // creo l'elemento col nome della classe
        String className = falseCons.getClass().getSimpleName();
        Element falseConsElement = document.createElement(className);

        return falseConsElement;
    }

    @Override
    public Object visit(IntegerCons integerCons) {
        // creo l'elemento col nome della classe
        String className = integerCons.getClass().getSimpleName();
        Element integerConsElement = document.createElement(className);

        // creo ed appendo il nodo con la stringa attribute
        integerConsElement.appendChild(document.createTextNode(integerCons.getAttribute()));

        return integerConsElement;
    }

    @Override
    public Object visit(RealCons realCons) {
        // creo l'elemento col nome della classe
        String className = realCons.getClass().getSimpleName();
        Element realConsElement = document.createElement(className);

        // creo ed appendo il nodo con la stringa attribute
        realConsElement.appendChild(document.createTextNode(realCons.getAttribute()));

        return realConsElement;
    }

    @Override
    public Object visit(StringCons stringCons) {
        // creo l'elemento col nome della classe
        String className = stringCons.getClass().getSimpleName();
        Element stringConsElement = document.createElement(className);

        // creo ed appendo il nodo con la stringa attribute
        stringConsElement.appendChild(document.createTextNode(stringCons.getAttribute()));

        return stringConsElement;
    }

    @Override
    public Object visit(TrueCons trueCons) {
        // creo l'elemento col nome della classe
        String className = trueCons.getClass().getSimpleName();
        Element trueConsElement = document.createElement(className);

        return trueConsElement;
    }

    @Override
    public Object visit(UnaryExpr unaryExpr) {
        // creo l'elemento col nome della classe
        String className = unaryExpr.getClass().getSimpleName();
        Element unaryExprElement = document.createElement(className);

        unaryExprElement.appendChild((Element) unaryExpr.getExpr().accept(this));

        return unaryExprElement;
    }

    @Override
    public Object visit(NotOp notOp) {
        return visit((UnaryExpr) notOp);
    }

    @Override
    public Object visit(ParOp parOp) {
        return visit((UnaryExpr) parOp);
    }

    @Override
    public Object visit(UminusOp uminusOp) {
        return visit((UnaryExpr) uminusOp);
    }

    private Object visitList(ArrayList<? extends Node> nodeList, String className) {
        // creo un nuovo element
        Element nodeListElement = document.createElement(className + "List");

        // appendo gli elementi contenuti nella lista
        for(Node node : nodeList){
            Element nodeElement = (Element) node.accept(this);
            nodeListElement.appendChild(nodeElement);
        }

        return nodeListElement;
    }

    private Object visitIdAndExprList(ArrayList<IdAndExpr> idAndExprList) {
        // creo un nuovo element
        Element idAndExprListElement = document.createElement("IdAndExprList");

        // appendo i nodi contenuti nel wrapper IdAndExpr
        for(IdAndExpr idAndExpr : idAndExprList) {
            // creo un idAndExprElement
            Element idAndExprElement = document.createElement("IdAndExpr");

            // appendo Id all'elemento idAndExprElement
            idAndExprElement.appendChild((Element) idAndExpr.getId().accept(this));

            // appendo Expr all'elemento idAndExprElement
            Expr expr = idAndExpr.getExpr();
            if(expr != null) {
                idAndExprElement.appendChild((Element) expr.accept(this));
            }

            idAndExprListElement.appendChild(idAndExprElement);
        }

        return idAndExprListElement;
    }

}
