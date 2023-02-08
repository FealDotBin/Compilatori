package visitor;

import syntaxtree.node.*;
import syntaxtree.node.stat.*;
import syntaxtree.node.expr.*;
import syntaxtree.node.expr.binaryexpr.*;
import syntaxtree.node.expr.cons.*;
import syntaxtree.node.expr.unaryexpr.*;
import syntaxtree.wrapper.IdAndExpr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.ArrayList;

public class XMLVisitor implements Visitor {

    private final Document document;

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
        Element bodyOpElement = createNodeEl(bodyOp);

        // appendo i nodi "VarDeclOp"
        bodyOpElement.appendChild(createNodeListEl(bodyOp.getVarDeclOpList(), "VarDeclOp"));

        // appendo i nodi "Stat"
        bodyOpElement.appendChild(createNodeListEl(bodyOp.getStatList(), "Stat"));

        return bodyOpElement;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) {
        // creo l'elemento col nome della classe
        Element funDeclOpElement = createNodeEl(funDeclOp);

        // creo e appendo il nodo "isMain"
        Element isMainElement = document.createElement("isMain");
        String isMain = Boolean.toString(funDeclOp.isMain());
        isMainElement.appendChild(document.createTextNode(isMain));
        funDeclOpElement.appendChild(isMainElement);

        // appendo il nodo "id"
        funDeclOpElement.appendChild((Element) funDeclOp.getId().accept(this));

        // appendo i nodi "ParDeclOp"
        funDeclOpElement.appendChild(createNodeListEl(funDeclOp.getParDeclOpList(), "ParDeclOp"));

        // appendo il nodo "TypeOp"
        funDeclOpElement.appendChild((Element) funDeclOp.getTypeOp().accept(this));

        // appendo il nodo "BodyOp"
        funDeclOpElement.appendChild((Element) funDeclOp.getBodyOp().accept(this));

        return funDeclOpElement;

    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        // creo l'elemento col nome della classe
        Element parDeclOpElement = createNodeEl(parDeclOp);

        // creo ed appendo il nodo "isOut"
        Element isOutElement = document.createElement("isOut");
        String isOut = Boolean.toString(parDeclOp.isOut());
        isOutElement.appendChild(document.createTextNode(isOut));
        parDeclOpElement.appendChild(isOutElement);

        // appendo il nodo TypeOp
        parDeclOpElement.appendChild((Element) parDeclOp.getTypeOp().accept(this));

        // appendo i nodi "Id"
        parDeclOpElement.appendChild(createNodeListEl(parDeclOp.getIdList(), "Id"));

        return parDeclOpElement;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        // creo l'elemento col nome della classe
        Element programOpElement = createNodeEl(programOp);

        // appendo i nodi VarDeclOp
        programOpElement.appendChild(createNodeListEl(programOp.getVarDeclOpList(), "VarDeclOp"));

        // appendo i nodi FunDeclOp
        programOpElement.appendChild(createNodeListEl(programOp.getFunDeclOpList(), "FunDeclOp"));

        // appendo l'elemento al documento finale
        document.appendChild(programOpElement);
        return document;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        // creo l'elemento col nome della classe
        Element varDeclOpElement = createNodeEl(varDeclOp);

        // appendo il nodo TypeOp
        varDeclOpElement.appendChild((Element) varDeclOp.getTypeOp().accept(this));

        // appendo i nodi contenuti nel wrapper IdAndExpr
        varDeclOpElement.appendChild(createIdAndExprListEl(varDeclOp.getIdAndExprList()));

        return varDeclOpElement;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        // creo l'elemento col nome della classe
        Element typeOpElement = createNodeEl(typeOp);

        // creo e appendo il nodo con la stringa type
        typeOpElement.appendChild(document.createTextNode(typeOp.getAttribute()));

        return typeOpElement;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        // creo l'elemento col nome della classe
        Element assignOpElement = createNodeEl(assignOp);

        // appendo i nodi id
        assignOpElement.appendChild(createNodeListEl(assignOp.getIdList(), "Id"));

        // appendo i nodi expr
        assignOpElement.appendChild(createNodeListEl(assignOp.getExprList(), "Expr"));

        return assignOpElement;
    }

    @Override
    public Object visit(ForOp forOp) {
        // creo l'elemento col nome della classe
        Element forOpElement = createNodeEl(forOp);

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
        Element funCallOpStatElement = createNodeEl(funCallOpStat);

        // appendo il nodo id
        funCallOpStatElement.appendChild((Element) funCallOpStat.getId().accept(this));

        // appendo i nodi expr
        funCallOpStatElement.appendChild(createNodeListEl(funCallOpStat.getExprList(), "Expr"));

        return funCallOpStatElement;
    }

    @Override
    public Object visit(IfOp ifOp) {
        // creo l'elemento col nome della classe
        Element ifOpElement = createNodeEl(ifOp);

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
        Element readOpElement = createNodeEl(readOp);

        // appendo i nodi id
        readOpElement.appendChild(createNodeListEl(readOp.getIdList(),"Id"));

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
        Element returnOpElement = createNodeEl(returnOp);

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
        Element whileOpElement = createNodeEl(whileOp);

        // appendo il nodo expr
        whileOpElement.appendChild((Element) whileOp.getExpr().accept(this));

        // appendo il nodo bodyOp
        whileOpElement.appendChild((Element) whileOp.getBodyOp().accept(this));

        return whileOpElement;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        // creo l'elemento col nome della classe
        Element writeOpElement = createNodeEl(writeOp);

        // creo e appendo il nodo isLn
        Element isLnElement = document.createElement("isLn");
        String isLn = Boolean.toString(writeOp.isLn());
        isLnElement.appendChild(document.createTextNode(isLn));
        writeOpElement.appendChild(isLnElement);

        // appendo i nodi expr
        writeOpElement.appendChild(createNodeListEl(writeOp.getExprList(), "Expr"));

        return writeOpElement;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        // creo l'elemento col nome della classe
        Element funCallOpExprElement = createNodeEl(funCallOpExpr);

        // appendo il nodo id
        funCallOpExprElement.appendChild((Element) funCallOpExpr.getId().accept(this));

        // appendo i nodi expr
        funCallOpExprElement.appendChild(createNodeListEl(funCallOpExpr.getExprList(), "Expr"));

        return funCallOpExprElement;
    }

    @Override
    public Object visit(Id id) {
        // creo l'elemento col nome della classe
        Element idElement = createNodeEl(id);

        // creo e appendo il nodo con la stringa attribute (se presente)
        String attribute = id.getAttribute();
        if(attribute != null){
            idElement.appendChild(document.createTextNode(attribute));
        }

        return idElement;
    }

    public Object visit(BinaryExpr binaryExpr){
        return null;
    }

    @Override
    public Object visit(AddOp addOp) {
        return createBinaryExprEl(addOp);
    }

    @Override
    public Object visit(AndOp andOp) {
        return createBinaryExprEl(andOp);
    }

    @Override
    public Object visit(DiffOp diffOp) {
        return createBinaryExprEl(diffOp);
    }

    @Override
    public Object visit(DivOp divOp) {
        return createBinaryExprEl(divOp);
    }

    @Override
    public Object visit(EQOp eqOp) {
        return createBinaryExprEl(eqOp);
    }

    @Override
    public Object visit(GEOp geOp) {
        return createBinaryExprEl(geOp);
    }

    @Override
    public Object visit(GTOp gtOp) {
        return createBinaryExprEl(gtOp);
    }

    @Override
    public Object visit(LEOp leOp) {
        return createBinaryExprEl(leOp);
    }

    @Override
    public Object visit(LTOp ltOp) {
        return createBinaryExprEl(ltOp);
    }

    @Override
    public Object visit(MulOp mulOp) {
        return createBinaryExprEl(mulOp);
    }

    @Override
    public Object visit(NEOp neOp) {
        return createBinaryExprEl(neOp);
    }

    @Override
    public Object visit(OrOp orOp) {
        return createBinaryExprEl(orOp);
    }

    @Override
    public Object visit(PowOp powOp) {
        return createBinaryExprEl(powOp);
    }

    @Override
    public Object visit(StrCatOp strCatOp) {
        return createBinaryExprEl(strCatOp);
    }

    @Override
    public Object visit(Cons cons) {
        return null;
    }

    @Override
    public Object visit(CharCons charCons) {
        // creo l'elemento col nome della classe
        Element charConsElement = createNodeEl(charCons);

        // creo ed appendo il nodo con la stringa attribute
        charConsElement.appendChild(document.createTextNode(charCons.getAttribute()));

        return charConsElement;
    }

    @Override
    public Object visit(FalseCons falseCons) {
        // creo l'elemento col nome della classe
        Element falseConsElement = createNodeEl(falseCons);

        return falseConsElement;
    }

    @Override
    public Object visit(IntegerCons integerCons) {
        // creo l'elemento col nome della classe
        Element integerConsElement = createNodeEl(integerCons);

        // creo ed appendo il nodo con la stringa attribute
        integerConsElement.appendChild(document.createTextNode(integerCons.getAttribute()));

        return integerConsElement;
    }

    @Override
    public Object visit(RealCons realCons) {
        // creo l'elemento col nome della classe
        Element realConsElement = createNodeEl(realCons);

        // creo ed appendo il nodo con la stringa attribute
        realConsElement.appendChild(document.createTextNode(realCons.getAttribute()));

        return realConsElement;
    }

    @Override
    public Object visit(StringCons stringCons) {
        // creo l'elemento col nome della classe
        Element stringConsElement = createNodeEl(stringCons);

        // creo ed appendo il nodo con la stringa attribute
        stringConsElement.appendChild(document.createTextNode(stringCons.getAttribute()));

        return stringConsElement;
    }

    @Override
    public Object visit(TrueCons trueCons) {
        // creo l'elemento col nome della classe
        Element trueConsElement = createNodeEl(trueCons);

        return trueConsElement;
    }

    @Override
    public Object visit(UnaryExpr unaryExpr) {
        return null;
    }

    @Override
    public Object visit(NotOp notOp) {
        return createUnaryExprEl(notOp);
    }

    @Override
    public Object visit(ParOp parOp) {
        return createUnaryExprEl(parOp);
    }

    @Override
    public Object visit(UminusOp uminusOp) {
        return createUnaryExprEl(uminusOp);
    }

    /* Private Methods */
    private Element createNodeEl(Node node) {
        // creo l'elemento col nome della classe
        String className = node.getClass().getSimpleName();
        Element nodeElement = document.createElement(className);
        nodeElement.setAttribute("type", node.getType());

        return nodeElement;
    }
    
    private Element createNodeListEl(ArrayList<? extends Node> nodeList, String className) {
        // creo un nuovo element
        Element nodeListElement = document.createElement(className + "List");

        // appendo gli elementi contenuti nella lista
        for(Node node : nodeList){
            Element nodeElement = (Element) node.accept(this);
            nodeListElement.appendChild(nodeElement);
        }

        return nodeListElement;
    }

    private Element createIdAndExprListEl(ArrayList<IdAndExpr> idAndExprList) {
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

    private Element createBinaryExprEl(BinaryExpr binaryExpr) {
        // creo l'elemento col nome della classe
        Element binaryExprElement = createNodeEl(binaryExpr);

        binaryExprElement.appendChild((Element) binaryExpr.getLeft().accept(this));
        binaryExprElement.appendChild((Element) binaryExpr.getRight().accept(this));

        return binaryExprElement;
    }

    private Element createUnaryExprEl(UnaryExpr unaryExpr) {
        // creo l'elemento col nome della classe
        Element unaryExprElement = createNodeEl(unaryExpr);

        unaryExprElement.appendChild((Element) unaryExpr.getExpr().accept(this));

        return unaryExprElement;
    }

}
