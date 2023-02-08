package visitor;

import env.enventry.EnvEntry;
import env.enventry.FunEnvEntry;
import env.enventry.ParamType;
import env.enventry.VarEnvEntry;
import kind.Kind;
import syntaxtree.node.*;
import syntaxtree.node.expr.Expr;
import syntaxtree.node.expr.FunCallOpExpr;
import syntaxtree.node.expr.Id;
import syntaxtree.node.expr.binaryexpr.*;
import syntaxtree.node.expr.cons.*;
import syntaxtree.node.expr.unaryexpr.NotOp;
import syntaxtree.node.expr.unaryexpr.ParOp;
import syntaxtree.node.expr.unaryexpr.UminusOp;
import syntaxtree.node.expr.unaryexpr.UnaryExpr;
import syntaxtree.node.stat.*;
import syntaxtree.wrapper.IdAndExpr;
import type.Type;

import java.util.*;

public class TypeCheckVisitor extends AbstractVisitor implements Visitor {

    private BinaryTypeMap arithTypeMap;
    private BinaryTypeMap relopTypeMap;
    private String funId;
    private HashSet<Stat> statGoToReturnSet;

    public TypeCheckVisitor() {
        arithTypeMap = BinaryTypeMap.getArithTypeMap();
        relopTypeMap = BinaryTypeMap.getRelopTypeMap();
        funId = null;
        statGoToReturnSet = new HashSet<>();
    }

    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        super.visit(bodyOp);

        for(VarDeclOp varDeclOp : bodyOp.getVarDeclOpList()) {
            String type = varDeclOp.getType();
            if(!Type.basicTypes.contains(type) && !type.equals(Type.VAR)) {
                throw new RuntimeException("Errore di tipo!");
            }
        }

        for(Stat stat : bodyOp.getStatList()) {
            if(!stat.getType().equals(Type.NOTYPE)) {
                throw new RuntimeException("Errore di tipo!");
            }
        }

        bodyOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) {
        // Setto funId col nome della funzione così da permettere agli eventuali returnOp di recuperare le info
        // sulla funzione dal type env.
        funId = funDeclOp.getId().getAttribute();

        super.visit(funDeclOp);

        String kind = funDeclOp.getId().getKind();
        String id = funDeclOp.getId().getAttribute();
        FunEnvEntry funEnvEntry = (FunEnvEntry) funDeclOp.getEnv().lookup(kind, id);
        String returnType = funEnvEntry.getReturnType();
        ArrayList<ParamType> paramTypeList = funEnvEntry.getParamTypeList();
        int i = 0;

        // se la funzione NON è void ed il body NON va a return, lancio eccezione
        if(!returnType.equals(Type.VOID) && !bodyGoToReturn(funDeclOp.getBodyOp())) {
            throw new RuntimeException("Errore: La funzione NON VOID deve avere un return statement!");
        }

        // controllo se il tipo di ritorno della firma della funzione è corretto
        if(!funDeclOp.getTypeOp().getType().equals(returnType)) {
            throw new RuntimeException("Errore: Il tipo di ritorno della funzione non è definito correttamente!");
        }

        // controllo se la firma della funzione è definita correttamente
        for(ParDeclOp parDeclOp : funDeclOp.getParDeclOpList()) {
            for(Id idObj : parDeclOp.getIdList()) {
                ParamType paramType = paramTypeList.get(i++);
                VarEnvEntry varEnvEntry = (VarEnvEntry) idObj.getEnv().lookup(idObj.getKind(), idObj.getAttribute());

                // controllo se il parametro ha lo stesso tipo definito nella firma
                if(!paramType.getType().equals(idObj.getType())) {
                    throw new RuntimeException("Errore: La firma della funzione non è definita correttamente!");
                }

                // controllo se il parametro è in/out come nella firma
                if(paramType.isOut() != varEnvEntry.isOut()) {
                    throw new RuntimeException("Errore: La firma della funzione non è definita correttamente!");
                }
            }
        }

        funDeclOp.setType(returnType);

        // reimposto i valori a quelli di default
        funId = null;

        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        super.visit(parDeclOp);

        String type = parDeclOp.getTypeOp().getType();

        for(Id idObj : parDeclOp.getIdList()) {
            VarEnvEntry varEnvEntry = (VarEnvEntry) idObj.getEnv().lookup(idObj.getKind(), idObj.getAttribute());

            // controllo se il tipo del parametro corrisponde a quello del TypeOp
            if(!idObj.getType().equals(type)) {
                throw new RuntimeException("Errore: Il tipo del parametro non corrisponde a quello del TypeOp!");
            }

            // controllo se il parametro in/out è definito correttamente
            if(varEnvEntry.isOut() != parDeclOp.isOut()) {
                throw new RuntimeException("Errore: Il parametro in/out non è definito correttamente");
            }
        }

        parDeclOp.setType(type);

        return null;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        super.visit(programOp);

        // controllo che i FunDeclOp siano basicType oppure void
        for(FunDeclOp funDeclOp : programOp.getFunDeclOpList()) {
            String type = funDeclOp.getType();
            if(!Type.basicTypes.contains(type) && !type.equals(Type.VOID)) {
                throw new RuntimeException("Errore: Il tipo di FunDeclOp non è né basicType, né VOID!");
            }
        }

        // controllo che i VarDeclOp siano basicType oppure var
        for(VarDeclOp varDeclOp : programOp.getVarDeclOpList()) {
            String type = varDeclOp.getType();
            if(!Type.basicTypes.contains(type) && !type.equals(Type.VAR)) {
                throw new RuntimeException("Errore: Il tipo di VarDeclOp non è né basicType, né VAR!");
            }
        }

        programOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        super.visit(varDeclOp);

        String type = varDeclOp.getTypeOp().getType();

        for(IdAndExpr idAndExpr : varDeclOp.getIdAndExprList()) {
            Id idObj = idAndExpr.getId();
            Expr expr = idAndExpr.getExpr();

            // se expr non è null, controllo che il tipo di Id sia uguale al tipo di Expr
            if(expr != null && !idObj.getType().equals(expr.getType())) {
                throw new RuntimeException("Errore: Il tipo della variabile non corrisponde a quello dell'espressione!");
            }

            // se type non è var, controllo che il tipo di Id sia uguale a quello di TyoeOp
            if(!type.equals(Type.VAR) && !type.equals(idObj.getType())) {
                throw new RuntimeException("Errore: Il tipo della variabile non corrisponde a quello del TypeOp!");
            }
        }

        varDeclOp.setType(type);

        return null;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        typeOp.setType(typeOp.getAttribute());

        return null;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        super.visit(assignOp);

        ArrayList<Id> idList = assignOp.getIdList();
        ArrayList<Expr> exprList = assignOp.getExprList();

        if(idList.size() != exprList.size()) {
            throw new RuntimeException("Errore: Il numero di variabili deve essere uguale al numero di espressioni!");
        }

        // controllo che il tipo delle variabili sia lo stesso di quello delle espressioni
        for(int i = 0; i < idList.size(); i++) {
            Id id = idList.get(i);
            Expr expr = exprList.get(i);

            if(!id.getType().equals(expr.getType())) {
                throw new RuntimeException("Errore: Il tipo della variabile non corrisponde a quello dell'espressione!");
            }
        }

        assignOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(ForOp forOp) {
        super.visit(forOp);

        if(!forOp.getFromValue().getType().equals(Type.INT)) {
            throw new RuntimeException("Errore di tipo!");
        }

        if(!forOp.getToValue().getType().equals(Type.INT)) {
            throw new RuntimeException("Errore di tipo!");
        }

        if(!forOp.getBodyOp().getType().equals(Type.NOTYPE)) {
            throw new RuntimeException("Errore di tipo!");
        }

        forOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(IfOp ifOp) {
        super.visit(ifOp);

        // verifico che expr sia di tipo bool
        if(!ifOp.getExpr().getType().equals(Type.BOOL)) {
            throw new RuntimeException("Errore di tipo!");
        }

        // verifico che il body-if sia di tipo notype
        if(!ifOp.getBodyOp().getType().equals(Type.NOTYPE)) {
            throw new RuntimeException("Errore di tipo");
        }

        // verifico che il body-else sia di tipo notype (se esiste)
        BodyOp bodyOpElse = ifOp.getBodyOpElse();
        if(bodyOpElse != null && !bodyOpElse.getType().equals(Type.NOTYPE)) {
            throw new RuntimeException("Errore di tipo");
        }

        ifOp.setType(Type.NOTYPE);

        // controllo se può andare a return
        if(ifOp.getBodyOpElse() != null) {
            if(bodyGoToReturn(ifOp.getBodyOp()) && bodyGoToReturn(ifOp.getBodyOpElse())) {
                statGoToReturnSet.add(ifOp);
            }
        }

        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        super.visit(readOp);

        // verifico che tutti gli id abbiano basic type
        for(Id idObj : readOp.getIdList()) {
            if(!Type.basicTypes.contains(idObj.getType())) {
                throw new RuntimeException("Errore di tipo!");
            }
        }

        // verifico che expr abbia type string (eventualmente)
        Expr expr = readOp.getExpr();
        if(expr != null && !expr.getType().equals(Type.STRING)) {
            throw new RuntimeException("Errore di tipo!");
        }

        readOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        super.visit(returnOp);

        String returnType = ((FunEnvEntry) returnOp.getEnv().lookup(Kind.FUN, funId)).getReturnType();
        Expr expr = returnOp.getExpr();

        // aggiungo il nodo al set per indicare che può andare a return
        statGoToReturnSet.add(returnOp);

        // se la funzione NON è void e non restituisco nulla, lancio eccezione
        if(!returnType.equals(Type.VOID) && expr == null) {
            throw new RuntimeException("Errore: return non può essere vuota in una funziona NON void!");
        }

        // se la funzione NON è void e restituisco qualcosa di tipo diverso rispetto al returnType, lancio eccezione
        if(!returnType.equals(Type.VOID) && expr != null && !expr.getType().equals(returnType)) {
            throw new RuntimeException("Errore: Il valore restituito dalla funzione non rispetta il tipo di ritorno!");
        }

        // se la funzione è void e restituisco qualcosa, lancio eccezione
        if(returnType.equals(Type.VOID) && expr != null) {
            throw new RuntimeException("Errore: return deve essere vuoto in una funzione void!");
        }

        returnOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        super.visit(whileOp);

        if(!whileOp.getExpr().getType().equals(Type.BOOL)) {
            throw new RuntimeException("Errore di tipo!");
        }
        if(!whileOp.getBodyOp().getType().equals(Type.NOTYPE)) {
            throw new RuntimeException("Errore di tipo!");
        }

        whileOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        super.visit(writeOp);

        for(Expr expr : writeOp.getExprList()) {
            if(!Type.basicTypes.contains(expr.getType())) {
                throw new RuntimeException("Errore di tipo!");
            }
        }

        writeOp.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        super.visit(funCallOpStat);

        String id = funCallOpStat.getId().getAttribute();
        String kind = Kind.FUN;
        FunEnvEntry funEnvEntry = (FunEnvEntry) funCallOpStat.getEnv().lookup(kind, id);
        ArrayList<ParamType> paramTypeList = funEnvEntry.getParamTypeList();
        ArrayList<Expr> exprList = funCallOpStat.getExprList();

        // controllo che il numero di parametri sia congruo alla firma della funzione
        if(paramTypeList.size() != exprList.size()) {
            throw new RuntimeException("Errore: Il numero di parametri passati alla funzione non è corretto!");
        }

        // controllo che i tipi dei parametri siano congrui con quelli definiti nella firma della funzione
        // controllo che venga passata una variabile quando il parametro è out
        for(int i = 0; i < paramTypeList.size(); i++) {
            ParamType paramType = paramTypeList.get(i);
            Expr expr = exprList.get(i);

            if(paramType.getType() != expr.getType()) {
                throw new RuntimeException("Errore: Il tipo dei parametri passati alla funzione non è corretto!");
            }
            if(paramType.isOut() && !(expr instanceof Id)) {
                throw new RuntimeException("Errore: Il parametro out passato alla funzione non è una variabile!");
            }
        }

        funCallOpStat.setType(Type.NOTYPE);

        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        super.visit(funCallOpExpr);

        String id = funCallOpExpr.getId().getAttribute();
        String kind = funCallOpExpr.getId().getKind();
        FunEnvEntry funEnvEntry = (FunEnvEntry) funCallOpExpr.getEnv().lookup(kind, id);
        ArrayList<ParamType> paramTypeList = funEnvEntry.getParamTypeList();
        ArrayList<Expr> exprList = funCallOpExpr.getExprList();

        // controllo che il numero di parametri sia congruo alla firma della funzione
        if(paramTypeList.size() != exprList.size()) {
            throw new RuntimeException("Errore: Il numero di parametri passati alla funzione non è corretto!");
        }

        // controllo che i tipi dei parametri siano congrui con quelli definiti nella firma della funzione
        // inoltre controllo che venga passata una variabile quando il parametro è out
        for(int i = 0; i < paramTypeList.size(); i++) {
            ParamType paramType = paramTypeList.get(i);
            Expr expr = exprList.get(i);

            if(paramType.getType() != expr.getType()) {
                throw new RuntimeException("Errore: Il tipo dei parametri passati alla funzione non è corretto!");
            }
            if(paramType.isOut() && !(expr instanceof Id)) {
                throw new RuntimeException("Errore: Il parametro out passato alla funzione non è una variabile!");
            }
        }

        // verifico che il tipo di ritorno sia un basic type
        String returnType = funEnvEntry.getReturnType();

        if(!Type.basicTypes.contains(returnType)) {
            throw new RuntimeException("Errore di tipo!");
        }
        funCallOpExpr.setType(returnType);

        return null;
    }

    @Override
    public Object visit(Id id) {
        String idAttr = id.getAttribute();
        String kind = id.getKind();
        EnvEntry envEntry = id.getEnv().lookup(kind, idAttr);
        String type = "";

        if(kind.equals(Kind.VAR)) {
            type = ((VarEnvEntry) envEntry).getType();
        } else if(kind.equals(Kind.FUN)) {
            type = ((FunEnvEntry) envEntry).getReturnType();
        } else {
            throw new RuntimeException("Errore: l'attributo Kind in Id ha un valore scorretto");
        }

        id.setType(type);

        return null;
    }

    @Override
    public Object visit(BinaryExpr binaryExpr) {
        return null;
    }

    @Override
    public Object visit(AddOp addOp) {
        super.visit(addOp);

        String leftType = addOp.getLeft().getType();
        String rightType = addOp.getRight().getType();
        String returnType = arithTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        addOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(AndOp andOp) {
        super.visit(andOp);

        String leftType = andOp.getLeft().getType();
        String rightType = andOp.getRight().getType();

        if(!leftType.equals(Type.BOOL) || !rightType.equals(Type.BOOL)) {
            throw new RuntimeException("Errore di tipo!");
        }
        andOp.setType(Type.BOOL);

        return null;
    }

    @Override
    public Object visit(DiffOp diffOp) {
        super.visit(diffOp);

        String leftType = diffOp.getLeft().getType();
        String rightType = diffOp.getRight().getType();
        String returnType = arithTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        diffOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(DivOp divOp) {
        super.visit(divOp);

        String leftType = divOp.getLeft().getType();
        String rightType = divOp.getRight().getType();
        String returnType = arithTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        divOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(EQOp eqOp) {
        super.visit(eqOp);

        String leftType = eqOp.getLeft().getType();
        String rightType = eqOp.getRight().getType();
        String returnType = relopTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        eqOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(GEOp geOp) {
        super.visit(geOp);

        String leftType = geOp.getLeft().getType();
        String rightType = geOp.getRight().getType();
        String returnType = relopTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        geOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(GTOp gtOp) {
        super.visit(gtOp);

        String leftType = gtOp.getLeft().getType();
        String rightType = gtOp.getRight().getType();
        String returnType = relopTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        gtOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(LEOp leOp) {
        super.visit(leOp);

        String leftType = leOp.getLeft().getType();
        String rightType = leOp.getRight().getType();
        String returnType = relopTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        leOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(LTOp ltOp) {
        super.visit(ltOp);

        String leftType = ltOp.getLeft().getType();
        String rightType = ltOp.getRight().getType();
        String returnType = relopTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        ltOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(MulOp mulOp) {
        super.visit(mulOp);

        String leftType = mulOp.getLeft().getType();
        String rightType = mulOp.getRight().getType();
        String returnType = arithTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        mulOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(NEOp neOp) {
        super.visit(neOp);

        String leftType = neOp.getLeft().getType();
        String rightType = neOp.getRight().getType();
        String returnType = relopTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        neOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(OrOp orOp) {
        super.visit(orOp);

        String leftType = orOp.getLeft().getType();
        String rightType = orOp.getRight().getType();

        if(!leftType.equals(Type.BOOL) || !rightType.equals(Type.BOOL)) {
            throw new RuntimeException("Errore di tipo!");
        }
        orOp.setType(Type.BOOL);

        return null;
    }

    @Override
    public Object visit(PowOp powOp) {
        super.visit(powOp);

        String leftType = powOp.getLeft().getType();
        String rightType = powOp.getRight().getType();
        String returnType = arithTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        powOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(StrCatOp strCatOp) {
        super.visit(strCatOp);

        BinaryTypeMap strCatOpTypeMap = BinaryTypeMap.getStrCatOpTypeMap();

        String leftType = strCatOp.getLeft().getType();
        String rightType = strCatOp.getRight().getType();
        String returnType = strCatOpTypeMap.checkType(leftType, rightType);

        if(returnType.equals(Type.ERROR)) {
            throw new RuntimeException("Errore di tipo!");
        }
        strCatOp.setType(returnType);

        return null;
    }

    @Override
    public Object visit(Cons cons) {
        return null;
    }

    @Override
    public Object visit(CharCons charCons) {
        charCons.setType(Type.CHAR);

        return null;
    }

    @Override
    public Object visit(FalseCons falseCons) {
        falseCons.setType(Type.BOOL);

        return null;
    }

    @Override
    public Object visit(IntegerCons integerCons) {
        integerCons.setType(Type.INT);

        return null;
    }

    @Override
    public Object visit(RealCons realCons) {
        realCons.setType(Type.REAL);

        return null;
    }

    @Override
    public Object visit(StringCons stringCons) {
        stringCons.setType(Type.STRING);

        return null;
    }

    @Override
    public Object visit(TrueCons trueCons) {
        trueCons.setType(Type.BOOL);

        return null;
    }

    @Override
    public Object visit(UnaryExpr unaryExpr) {
        return null;
    }

    @Override
    public Object visit(NotOp notOp) {
        super.visit(notOp);

        String type = notOp.getExpr().getType();

        if(!type.equals(Type.BOOL)) {
            throw new RuntimeException("Errore di tipo!");
        }
        notOp.setType(type);

        return null;
    }

    @Override
    public Object visit(ParOp parOp) {
        super.visit(parOp);

        String type = parOp.getExpr().getType();

        if(!Type.basicTypes.contains(type)) {
            throw new RuntimeException("Errore di tipo!");
        }

        parOp.setType(type);

        return null;
    }

    @Override
    public Object visit(UminusOp uminusOp) {
        super.visit(uminusOp);

        String type = uminusOp.getExpr().getType();

        if(!type.equals(Type.INT) && !type.equals(Type.REAL)) {
            throw new RuntimeException("Errore di tipo!");
        }
        uminusOp.setType(type);

        return null;
    }

    private boolean bodyGoToReturn(BodyOp bodyOp) {
        for(Stat stat : bodyOp.getStatList()){
            if(statGoToReturnSet.contains(stat))
                return true;
        }
        return false;
    }

    private static class BinaryTypeMap {
        private HashMap<TypeMapKey, String> typeMap;

        private BinaryTypeMap(HashMap<TypeMapKey, String> typeMap) {
            this.typeMap = typeMap;
        }

        public static BinaryTypeMap getArithTypeMap() {
            HashMap<TypeMapKey, String> typeMap = new HashMap<>();
            typeMap.put(new TypeMapKey(Type.INT, Type.INT), Type.INT);
            typeMap.put(new TypeMapKey(Type.INT, Type.REAL), Type.REAL);

            typeMap.put(new TypeMapKey(Type.REAL, Type.INT), Type.REAL);
            typeMap.put(new TypeMapKey(Type.REAL, Type.REAL), Type.REAL);

            return new BinaryTypeMap(typeMap);
        }

        public static BinaryTypeMap getRelopTypeMap() {
            HashMap<TypeMapKey, String> typeMap = new HashMap<>();
            typeMap.put(new TypeMapKey(Type.INT, Type.INT), Type.BOOL);
            typeMap.put(new TypeMapKey(Type.INT, Type.REAL), Type.BOOL);

            typeMap.put(new TypeMapKey(Type.REAL, Type.INT), Type.BOOL);
            typeMap.put(new TypeMapKey(Type.REAL, Type.REAL), Type.BOOL);

            typeMap.put(new TypeMapKey(Type.STRING, Type.STRING), Type.BOOL);

            typeMap.put(new TypeMapKey(Type.CHAR, Type.CHAR), Type.BOOL);

            return new BinaryTypeMap(typeMap);
        }

        public static BinaryTypeMap getStrCatOpTypeMap() {
            HashMap<TypeMapKey, String> typeMap = new HashMap<>();
            typeMap.put(new TypeMapKey(Type.STRING, Type.STRING), Type.STRING);
            typeMap.put(new TypeMapKey(Type.STRING, Type.INT), Type.STRING);
            typeMap.put(new TypeMapKey(Type.STRING, Type.REAL), Type.STRING);
            typeMap.put(new TypeMapKey(Type.STRING, Type.CHAR), Type.STRING);
            typeMap.put(new TypeMapKey(Type.STRING, Type.BOOL), Type.STRING);

            typeMap.put(new TypeMapKey(Type.INT, Type.STRING), Type.STRING);
            typeMap.put(new TypeMapKey(Type.REAL, Type.STRING), Type.STRING);
            typeMap.put(new TypeMapKey(Type.CHAR, Type.CHAR), Type.STRING);
            typeMap.put(new TypeMapKey(Type.BOOL, Type.STRING), Type.STRING);

            return new BinaryTypeMap(typeMap);
        }

        public String checkType(String type1, String type2) {
            String returnType = typeMap.get(new TypeMapKey(type1, type2));

            if(returnType == null)
                return Type.ERROR;
            else
                return returnType;
        }
    }

    private static class TypeMapKey {
        private String type1;
        private String type2;

        public TypeMapKey(String type1, String type2) {
            this.type1 = type1;
            this.type2 = type2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(type1, type2);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            TypeMapKey key = (TypeMapKey) obj;
            return Objects.equals(type1, key.type1) && Objects.equals(type2, key.type2);
        }
    }
}

