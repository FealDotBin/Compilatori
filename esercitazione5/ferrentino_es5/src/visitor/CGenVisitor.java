package visitor;

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

import java.util.ArrayList;

public class CGenVisitor implements Visitor {

    StringBuilder headBuffer;
    StringBuilder signBuffer;
    StringBuilder globalBuffer;
    StringBuilder codeBuffer;
    StringBuilder varDeclBuffer;
    StringBuilder varAssignBuffer;
    StringBuilder workingBuffer; // il buffer con cui si sta lavorando

    public CGenVisitor() {
        headBuffer = new StringBuilder();
        signBuffer = new StringBuilder();
        globalBuffer = new StringBuilder();
        codeBuffer = new StringBuilder();
        varDeclBuffer = new StringBuilder();
        varAssignBuffer = new StringBuilder();

        headBuffer.append(
                "#include <stdio.h>\n" +
                "#include <stdlib.h>\n" +
                "#include <string.h> \n" +
                "#include <malloc.h> \n" +
                "#include <math.h> \n" +
                "#include <errno.h> \n" +
                "#include <limits.h> \n" +
                "#include <stdbool.h> \n\n"
        );

        signBuffer.append("char* concat_strings(char* string1, char* string2);\n");
        codeBuffer.append(
                "char* concat_strings(char* string1, char* string2){\n" +
                "    size_t len = strlen(string1) + strlen(string2);\n" +
                "    char * buffer = (char *)malloc(len + 1);\n" +
                "    buffer[0] = '\\0';\n" +
                "    buffer = strncat(buffer, string1, strlen(string1));\n" +
                "    buffer = strncat(buffer, string2, strlen(string2));\n" +
                "    return buffer;\n" +
                "}\n\n"
        );

        signBuffer.append("void read_int(int *num); \n");
        codeBuffer.append(
                "void read_int(int *num){\n" +
                "    long a; \n" +
                "    char *buf = \"\"; \n" +
                "    read_string(&buf); \n" +
                "    errno = 0; \n" +
                "    char *endptr; \n" +
                "    a = strtol(buf, &endptr, 10); \n" +
                "    if (errno == ERANGE){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    if (endptr == buf){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    if (*endptr && *endptr != '\\n'){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    if (a > INT_MAX || a < INT_MIN){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    *num = (int) a; \n" +
                "}\n\n"
        );

        signBuffer.append("void read_float(float *num); \n");
        codeBuffer.append(
                "void read_float(float *num){ \n" +
                "    float a; \n" +
                "    char *buf = \"\"; \n" +
                "    read_string(&buf); \n" +
                "    errno = 0; \n" +
                "    char *endptr; \n" +
                "    a = strtof(buf, &endptr); \n" +
                "    if (errno == ERANGE){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    if (endptr == buf){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    if (*endptr && *endptr != '\\n'){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    *num = (float) a; \n" +
                "} \n\n"
        );

        signBuffer.append("void read_bool(bool *b); \n");
        codeBuffer.append(
                "void read_bool(bool *b){ \n" +
                "    char *buf = \"\"; \n" +
                "    read_string(&buf); \n" +
                "    if(strcmp(buf, \"true\") == 0){ \n" +
                "        *b = true; \n" +
                "        return; \n" +
                "    } \n" +
                "    if(strcmp(buf, \"false\") == 0){ \n" +
                "        *b = false; \n" +
                "        return; \n" +
                "    } \n" +
                "    fprintf(stderr, \"[!] Invalid Input\\n\");" +
                "} \n\n"
        );

        signBuffer.append("void read_char(char *c); \n");
        codeBuffer.append(
                "void read_char(char *c){ \n" +
                "    char *buf = \"\"; \n" +
                "    read_string(&buf); \n" +
                "    if(strlen(buf) != 1){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    *c = buf[0]; \n" +
                "} \n\n"
        );

        signBuffer.append("void read_string(char **string);\n");
        codeBuffer.append(
                "void read_string(char **string){ \n" +
                "    int ch; \n" +
                "    char buffer[4096]; \n" +
                "    if (fgets(buffer, 4096, stdin) == NULL){ \n" +
                "        fprintf(stderr, \"[!] Invalid Input\\n\"); \n" +
                "        return; \n" +
                "    } \n" +
                "    if(strstr(buffer, \"\\n\") == NULL){ \n" +
                "        while((ch = getchar()) != '\\n' && ch != EOF); \n" +
                "        buffer[4095] = '\\0'; \n" +
                "    } else { \n" +
                "        buffer[strcspn(buffer, \"\\n\")] = '\\0'; \n" +
                "    } \n" +
                "    *string = strndup(buffer, strlen(buffer)); \n" +
                "} \n\n"
        );

        signBuffer.append("char* convert_" + Type.INT + "_to_" + Type.STRING + "(int num); \n");
        codeBuffer.append(
                "char* convert_" + Type.INT + "_to_" + Type.STRING + "(int num){ \n" +
                "    char* str = (char *) malloc(sizeof(char) * 20); \n" +
                "    snprintf(str, sizeof str, \"%d\", num); \n" +
                "    return str; \n" +
                "} \n\n"
        );

        signBuffer.append("char* convert_" + Type.REAL + "_to_" + Type.STRING + "(float num); \n");
        codeBuffer.append(
                "char* convert_" + Type.REAL + "_to_" + Type.STRING + "(float num){ \n" +
                        "    char* str = (char *) malloc(sizeof(char) * 20); \n" +
                        "    snprintf(str, sizeof str, \"%f\", num); \n" +
                        "    return str; \n" +
                        "} \n\n"
        );

        signBuffer.append("char* convert_" + Type.CHAR + "_to_" + Type.STRING + "(char c); \n");
        codeBuffer.append(
                "char* convert_" + Type.CHAR + "_to_" + Type.STRING + "(char c){ \n" +
                        "    char* str = (char *) malloc(sizeof(char) * 2); \n" +
                        "    str[0] = c; \n" +
                        "    str[1] = '\\0'; \n" +
                        "    return str; \n" +
                        "} \n\n"
        );

        signBuffer.append("char* convert_" + Type.BOOL + "_to_" + Type.STRING + "(bool b); \n");
        codeBuffer.append(
                "char* convert_" + Type.BOOL + "_to_" + Type.STRING + "(bool b){ \n" +
                        "    if(b) { \n" +
                        "        return \"true\"; \n" +
                        "    } else { \n" +
                        "        return \"false\"; \n" +
                        "    } \n" +
                        "} \n\n"
        );

    }

    public StringBuilder getCodeBuffer() {
        return codeBuffer;
    }

    @Override
    public Object visit(Node node) {
        return null;
    }

    @Override
    public Object visit(BodyOp bodyOp) {
        genVarDeclOpListCode(bodyOp.getVarDeclOpList());
        for(Stat stat : bodyOp.getStatList()) {
            stat.accept(this);
        }

        return null;
    }

    @Override
    public Object visit(FunDeclOp funDeclOp) {
        StringBuilder oldBuffer = workingBuffer;

        // genero la firma della funzione
        workingBuffer = signBuffer;
        genSign(funDeclOp);
        workingBuffer.append(";\n");

        // genero la definizione della funzione
        workingBuffer = codeBuffer;
        genSign(funDeclOp);
        workingBuffer.append("{\n");
        funDeclOp.getBodyOp().accept(this);
        workingBuffer.append("}\n");

        workingBuffer = oldBuffer;
        return null;
    }

    @Override
    public Object visit(ParDeclOp parDeclOp) {
        String delim = "";
        for(Id idObj : parDeclOp.getIdList()) {
            workingBuffer.append(delim);
            parDeclOp.getTypeOp().accept(this);
            if(parDeclOp.isOut()) {
                workingBuffer.append("*");
            }
            workingBuffer.append(" ");
            genIdCode(idObj, false);
            delim = ", ";
        }

        return null;
    }

    @Override
    public Object visit(ProgramOp programOp) {
        StringBuilder oldBuffer = workingBuffer;

        workingBuffer = globalBuffer;
        genVarDeclOpListCode(programOp.getVarDeclOpList());

        workingBuffer = codeBuffer;
        for(FunDeclOp funDeclOp : programOp.getFunDeclOpList()) {
            if(funDeclOp.isMain()) {
                genMainCode(funDeclOp);
            }
            funDeclOp.accept(this);
            workingBuffer.append("\n");
        }

        // merge dei buffer in workingBuffer
        codeBuffer.insert(0, "\n");
        codeBuffer.insert(0, globalBuffer);
        codeBuffer.insert(0, signBuffer);
        codeBuffer.insert(0, headBuffer);

        workingBuffer = oldBuffer;
        return null;
    }

    @Override
    public Object visit(VarDeclOp varDeclOp) {
        // se devo dichiarare variabili globali
        if(workingBuffer == globalBuffer) {
            for (IdAndExpr idAndExpr : varDeclOp.getIdAndExprList()) {
                workingBuffer.append(getCType(idAndExpr.getId().getType()));
                workingBuffer.append(" ");
                genAssignCode(idAndExpr.getId(), idAndExpr.getExpr());
                workingBuffer.append("\n");
            }
            return null;
        }

        // se devo dichiarare variabili in un body
        if(workingBuffer == codeBuffer) {
            StringBuilder oldBuffer = workingBuffer;

            // prima genero tutte le dichiarazioni
            workingBuffer = varDeclBuffer;
            for (IdAndExpr idAndExpr : varDeclOp.getIdAndExprList()) {
                workingBuffer.append(getCType(idAndExpr.getId().getType()));
                workingBuffer.append(" ");
                genIdCode(idAndExpr.getId(), true);
                workingBuffer.append(";");
                workingBuffer.append("\n");
            }
            workingBuffer.append("\n");

            // ora assegno i valori a tutte le variabili
            workingBuffer = varAssignBuffer;
            for (IdAndExpr idAndExpr : varDeclOp.getIdAndExprList()) {
                if (idAndExpr.getExpr() != null) {
                    genAssignCode(idAndExpr.getId(), idAndExpr.getExpr());
                    workingBuffer.append("\n");
                }
            }

            // ripristino il working buffer
            workingBuffer = oldBuffer;
            return null;
        }

        return null;
    }

    @Override
    public Object visit(TypeOp typeOp) {
        String type = getCType(typeOp.getType());
        if(type != null)
            workingBuffer.append(type);

        return null;
    }

    @Override
    public Object visit(Stat stat) {
        return null;
    }

    @Override
    public Object visit(AssignOp assignOp) {
        ArrayList<Id> idList = assignOp.getIdList();
        ArrayList<Expr> exprList = assignOp.getExprList();

        for(int i = 0; i < idList.size(); i++) {
            genAssignCode(idList.get(i), exprList.get(i));
            workingBuffer.append("\n");
        }

        return null;
    }

    @Override
    public Object visit(ForOp forOp) {
        Id idObj = forOp.getId();
        String relop = "";
        String increment = "";

        if(Integer.parseInt(forOp.getFromValue().getAttribute()) <= Integer.parseInt(forOp.getToValue().getAttribute())) {
            relop = "<=";
            increment = "++";
        } else {
            relop = ">=";
            increment = "--";
        }

        workingBuffer.append("for(");

        // inizializzazione
        workingBuffer.append(getCType(idObj.getType()) + " ");
        genIdCode(idObj, false);
        workingBuffer.append(" = ");
        forOp.getFromValue().accept(this);
        workingBuffer.append("; ");

        // condizione
        genIdCode(idObj, false);
        workingBuffer.append(" " + relop + " ");
        forOp.getToValue().accept(this);
        workingBuffer.append("; ");

        // incremento
        genIdCode(idObj, false);
        workingBuffer.append(increment);

        workingBuffer.append("){\n");
        forOp.getBodyOp().accept(this);
        workingBuffer.append("}");

        workingBuffer.append("\n");
        return null;
    }

    @Override
    public Object visit(IfOp ifOp) {
        workingBuffer.append("if(");
        genExprCode(ifOp.getExpr());
        workingBuffer.append(")");

        workingBuffer.append("{\n");
        ifOp.getBodyOp().accept(this);
        workingBuffer.append("}");

        BodyOp bodyOpElse = ifOp.getBodyOpElse();
        if(bodyOpElse != null) {
            workingBuffer.append(" else {\n");
            bodyOpElse.accept(this);
            workingBuffer.append("}");
        }

        workingBuffer.append("\n");
        return null;
    }

    @Override
    public Object visit(ReadOp readOp) {
        genWriteCode(readOp.getExpr());

        for(Id idObj : readOp.getIdList()) {
            switch(idObj.getType()) {
                case Type.INT -> workingBuffer.append("read_int(");
                case Type.REAL -> workingBuffer.append("read_float(");
                case Type.BOOL -> workingBuffer.append("read_bool(");
                case Type.CHAR -> workingBuffer.append("read_char(");
                case Type.STRING -> workingBuffer.append("read_string(");
                default -> throw new RuntimeException("Errore: read non supportata per questo tipo");
            }
            workingBuffer.append("&");
            genIdCode(idObj, true);
            workingBuffer.append(");\n");
        }

        return null;
    }

    @Override
    public Object visit(ReturnOp returnOp) {
        if(returnOp.getExpr() == null) {
            workingBuffer.append("return;\n");
            return null;
        }

        workingBuffer.append("return ");
        genExprCode(returnOp.getExpr());
        workingBuffer.append(";");

        workingBuffer.append("\n");
        return null;
    }

    @Override
    public Object visit(WhileOp whileOp) {
        workingBuffer.append("while(");
        genExprCode(whileOp.getExpr());
        workingBuffer.append("){\n");
        whileOp.getBodyOp().accept(this);
        workingBuffer.append("}");

        workingBuffer.append("\n");
        return null;
    }

    @Override
    public Object visit(WriteOp writeOp) {
        for(Expr expr : writeOp.getExprList()) {
            genWriteCode(expr);
        }

        if(writeOp.isLn()) {
            workingBuffer.append("printf(\"\\n\");");
            workingBuffer.append("\n");
        }

        return null;
    }

    @Override
    public Object visit(FunCallOpStat funCallOpStat) {
        Id idObj = funCallOpStat.getId();
        FunEnvEntry funEnvEntry = (FunEnvEntry) funCallOpStat.getEnv().lookup(idObj.getKind(), idObj.getAttribute());
        ArrayList<ParamType> paramTypeList = funEnvEntry.getParamTypeList();
        ArrayList<Expr> exprList = funCallOpStat.getExprList();

        workingBuffer.append(idObj.getAttribute());
        workingBuffer.append("(");

        // genero il codice per i parametri della funzione
        String delim = "";
        for(int i = 0; i < exprList.size(); i++) {
            workingBuffer.append(delim);
            // se il parametro va passato per riferimento, anteponi un "&"
            if(paramTypeList.get(i).isOut()) {
                workingBuffer.append("&");
            }
            genExprCode(exprList.get(i));
            delim = ", ";
        }

        workingBuffer.append(");");
        workingBuffer.append("\n");

        return null;
    }

    @Override
    public Object visit(Expr expr) {
        return null;
    }

    @Override
    public Object visit(FunCallOpExpr funCallOpExpr) {
        Id idObj = funCallOpExpr.getId();
        FunEnvEntry funEnvEntry = (FunEnvEntry) funCallOpExpr.getEnv().lookup(idObj.getKind(), idObj.getAttribute());
        ArrayList<ParamType> paramTypeList = funEnvEntry.getParamTypeList();
        ArrayList<Expr> exprList = funCallOpExpr.getExprList();

        workingBuffer.append(idObj.getAttribute());
        workingBuffer.append("(");

        // genero il codice per i parametri della funzione
        String delim = "";
        for(int i = 0; i < exprList.size(); i++) {
            workingBuffer.append(delim);
            // se il parametro va passato per riferimento, anteponi un "&"
            if(paramTypeList.get(i).isOut()) {
                workingBuffer.append("&");
            }
            genExprCode(exprList.get(i));
            delim = ", ";
        }

        workingBuffer.append(")");
        workingBuffer.append("\n");

        return null;
    }

    @Override
    public Object visit(Id id) {
        workingBuffer.append(id.getAttribute());

        return null;
    }

    @Override
    public Object visit(BinaryExpr binaryExpr) {
        return null;
    }

    @Override
    public Object visit(AddOp addOp) {
        genExprCode(addOp.getLeft());
        workingBuffer.append("+");
        genExprCode(addOp.getRight());

        return null;
    }

    @Override
    public Object visit(AndOp andOp) {
        genExprCode(andOp.getLeft());
        workingBuffer.append("&&");
        genExprCode(andOp.getRight());

        return null;
    }

    @Override
    public Object visit(DiffOp diffOp) {
        genExprCode(diffOp.getLeft());
        workingBuffer.append("-");
        genExprCode(diffOp.getRight());

        return null;
    }

    @Override
    public Object visit(DivOp divOp) {
        genExprCode(divOp.getLeft());
        workingBuffer.append("/");
        genExprCode(divOp.getRight());

        return null;
    }

    @Override
    public Object visit(EQOp eqOp) {
        if(eqOp.getLeft().getType().equals(Type.STRING) && eqOp.getRight().getType().equals(Type.STRING)) {
            workingBuffer.append("strcmp(");
            genExprCode(eqOp.getLeft());
            workingBuffer.append(",");
            genExprCode(eqOp.getRight());
            workingBuffer.append(")");
            workingBuffer.append(" == 0");

            return null;
        }

        genExprCode(eqOp.getLeft());
        workingBuffer.append("==");
        genExprCode(eqOp.getRight());

        return null;
    }

    @Override
    public Object visit(GEOp geOp) {
        if(geOp.getLeft().getType().equals(Type.STRING) && geOp.getRight().getType().equals(Type.STRING)) {
            workingBuffer.append("strcmp(");
            genExprCode(geOp.getLeft());
            workingBuffer.append(",");
            genExprCode(geOp.getRight());
            workingBuffer.append(")");
            workingBuffer.append(" >= 0");

            return null;
        }

        genExprCode(geOp.getLeft());
        workingBuffer.append(">=");
        genExprCode(geOp.getRight());

        return null;
    }

    @Override
    public Object visit(GTOp gtOp) {
        if(gtOp.getLeft().getType().equals(Type.STRING) && gtOp.getRight().getType().equals(Type.STRING)) {
            workingBuffer.append("strcmp(");
            genExprCode(gtOp.getLeft());
            workingBuffer.append(",");
            genExprCode(gtOp.getRight());
            workingBuffer.append(")");
            workingBuffer.append(" > 0");

            return null;
        }

        genExprCode(gtOp.getLeft());
        workingBuffer.append(">");
        genExprCode(gtOp.getRight());

        return null;
    }

    @Override
    public Object visit(LEOp leOp) {
        if(leOp.getLeft().getType().equals(Type.STRING) && leOp.getRight().getType().equals(Type.STRING)) {
            workingBuffer.append("strcmp(");
            genExprCode(leOp.getLeft());
            workingBuffer.append(",");
            genExprCode(leOp.getRight());
            workingBuffer.append(")");
            workingBuffer.append(" <= 0");

            return null;
        }

        genExprCode(leOp.getLeft());
        workingBuffer.append("<=");
        genExprCode(leOp.getRight());

        return null;
    }

    @Override
    public Object visit(LTOp ltOp) {
        if(ltOp.getLeft().getType().equals(Type.STRING) && ltOp.getRight().getType().equals(Type.STRING)) {
            workingBuffer.append("strcmp(");
            genExprCode(ltOp.getLeft());
            workingBuffer.append(",");
            genExprCode(ltOp.getRight());
            workingBuffer.append(")");
            workingBuffer.append(" < 0");

            return null;
        }

        genExprCode(ltOp.getLeft());
        workingBuffer.append("<");
        genExprCode(ltOp.getRight());

        return null;
    }

    @Override
    public Object visit(MulOp mulOp) {
        genExprCode(mulOp.getLeft());
        workingBuffer.append("*");
        genExprCode(mulOp.getRight());

        return null;
    }

    @Override
    public Object visit(NEOp neOp) {
        if(neOp.getLeft().getType().equals(Type.STRING) && neOp.getRight().getType().equals(Type.STRING)) {
            workingBuffer.append("strcmp(");
            genExprCode(neOp.getLeft());
            workingBuffer.append(",");
            genExprCode(neOp.getRight());
            workingBuffer.append(")");
            workingBuffer.append(" != 0");

            return null;
        }

        genExprCode(neOp.getLeft());
        workingBuffer.append("!=");
        genExprCode(neOp.getRight());

        return null;
    }

    @Override
    public Object visit(OrOp orOp) {
        genExprCode(orOp.getLeft());
        workingBuffer.append("||");
        genExprCode(orOp.getRight());

        return null;
    }

    @Override
    public Object visit(PowOp powOp) {
        workingBuffer.append("(" + getCType(powOp.getType()) + ")");
        workingBuffer.append("pow(");
        workingBuffer.append("(double)");
        genExprCode(powOp.getLeft());
        workingBuffer.append(",");
        workingBuffer.append("(double)");
        genExprCode(powOp.getRight());
        workingBuffer.append(")");

        return null;
    }

    @Override
    public Object visit(StrCatOp strCatOp) {
        // apro la funzione concat_strings
        workingBuffer.append("concat_strings(");

        // passo il primo parametro alla funzione, se NON è una stringa, lo converto in stringa
        String leftType = strCatOp.getLeft().getType();
        if(!leftType.equals(Type.STRING)) {
            workingBuffer.append("convert_" + leftType + "_to_" + Type.STRING + "(");
        }
        genExprCode(strCatOp.getLeft());
        if(!leftType.equals(Type.STRING)) {
            workingBuffer.append(")");
        }
        workingBuffer.append(", ");

        // passo il secondo parametro alla funzione, se NON è una stringa, lo converto in stringa
        String rightType = strCatOp.getRight().getType();
        if(!rightType.equals(Type.STRING)) {
            workingBuffer.append("convert_" + rightType + "_to_" + Type.STRING + "(");
        }
        genExprCode(strCatOp.getRight());
        if(!rightType.equals(Type.STRING)) {
            workingBuffer.append(")");
        }

        // chiudo la funzione concat_strings
        workingBuffer.append(")");
        return null;
    }

    @Override
    public Object visit(Cons cons) {
        return null;
    }

    @Override
    public Object visit(CharCons charCons) {
        workingBuffer.append("'");
        workingBuffer.append(charCons.getAttribute());
        workingBuffer.append("'");

        return null;
    }

    @Override
    public Object visit(FalseCons falseCons) {
        workingBuffer.append("false");

        return null;
    }

    @Override
    public Object visit(IntegerCons integerCons) {
        workingBuffer.append(integerCons.getAttribute());

        return null;
    }

    @Override
    public Object visit(RealCons realCons) {
        workingBuffer.append(realCons.getAttribute());

        return null;
    }

    @Override
    public Object visit(StringCons stringCons) {
        String escaped = escapeString(stringCons.getAttribute());
        workingBuffer.append("\"" + escaped + "\"");

        return null;
    }

    @Override
    public Object visit(TrueCons trueCons) {
        workingBuffer.append("true");

        return null;
    }

    @Override
    public Object visit(UnaryExpr unaryExpr) {
        return null;
    }

    @Override
    public Object visit(NotOp notOp) {
        workingBuffer.append("!");
        genExprCode(notOp.getExpr());

        return null;
    }

    @Override
    public Object visit(ParOp parOp) {
        workingBuffer.append("(");
        genExprCode(parOp.getExpr());
        workingBuffer.append(")");

        return null;
    }

    @Override
    public Object visit(UminusOp uminusOp) {
        workingBuffer.append("(");
        workingBuffer.append("-");
        genExprCode(uminusOp.getExpr());
        workingBuffer.append(")");

        return null;
    }

    /* Private Methods */

    private String getCType(String type) {
        switch(type) {
            case Type.INT : return "int";
            case Type.REAL : return "float";
            case Type.CHAR : return "char";
            case Type.STRING : return "char*";
            case Type.BOOL : return "bool";
            case Type.VOID : return "void";
            default: return null;
        }
    }

    private String getCFormatSpecifier(String type) {
        switch(type) {
            case Type.INT : return "%d";
            case Type.REAL : return "%f";
            case Type.CHAR : return "%c";
            case Type.STRING : return "%s";
            default: return null;
        }
    }

    private void genMainCode(FunDeclOp funDeclOp) {
        funDeclOp.getTypeOp().accept(this);
        workingBuffer.append(" main(){\n");

        // genero tutte le variabili
        for(ParDeclOp parDeclOp : funDeclOp.getParDeclOpList()) {
            for(Id idObj : parDeclOp.getIdList()) {
                workingBuffer.append(getCType(idObj.getType()));
                workingBuffer.append(" ");
                genIdCode(idObj, false);
                workingBuffer.append(";");
                workingBuffer.append("\n");
            }
        }
        workingBuffer.append("\n");

        // chiedo gli input all'utente per ogni variabile
        for(ParDeclOp parDeclOp : funDeclOp.getParDeclOpList()) {
            for(Id idObj : parDeclOp.getIdList()) {
                workingBuffer.append("printf(\"%s\", \"Inserisci un " + idObj.getType() + ": \");");
                workingBuffer.append("\n");
                    switch(idObj.getType()) {
                        case Type.INT -> workingBuffer.append("read_int(");
                        case Type.REAL -> workingBuffer.append("read_float(");
                        case Type.BOOL -> workingBuffer.append("read_bool(");
                        case Type.CHAR -> workingBuffer.append("read_char(");
                        case Type.STRING -> workingBuffer.append("read_string(");
                        default -> throw new RuntimeException("Errore: read non supportata per questo tipo");
                    }
                    workingBuffer.append("&");
                    genIdCode(idObj, false);
                    workingBuffer.append(");\n");
            }
        }
        workingBuffer.append("\n");

        // invoco la funzione passandogli i parametri e stampando il risultato in output (quando la funzione non è void)
        String returnType = funDeclOp.getTypeOp().getType();

        // stampo la prima parte della printf
        if(!returnType.equals(Type.VOID)) {
            workingBuffer.append("printf(");
            if(returnType.equals(Type.BOOL)){
                workingBuffer.append("\"" + getCFormatSpecifier(Type.STRING) + "\"");
            } else {
                workingBuffer.append("\"" + getCFormatSpecifier(returnType) + "\"");
            }
            workingBuffer.append(", ");
        }

        // chiamo la funzione
        genIdCode(funDeclOp.getId(), false);
        workingBuffer.append("(");
        String delim = "";
        for(ParDeclOp parDeclOp : funDeclOp.getParDeclOpList()) {
            for(Id idObj : parDeclOp.getIdList()) {
                workingBuffer.append(delim);
                if(parDeclOp.isOut()) {
                    workingBuffer.append("&");
                }
                genIdCode(idObj, false);
                delim = ",";
            }
        }
        workingBuffer.append(")");

        // stampo la parte restante della printf
        if(!returnType.equals(Type.VOID)) {
            if(returnType.equals(Type.BOOL)) {
                workingBuffer.append(" ? \"true\" : \"false\"");
            }
            workingBuffer.append(")");
        }

        workingBuffer.append(";\n");

        workingBuffer.append("}\n\n");
    }

    private void genSign(FunDeclOp funDeclOp) {
        funDeclOp.getTypeOp().accept(this);
        workingBuffer.append(" ");
        genIdCode(funDeclOp.getId(), false);

        workingBuffer.append("(");
        String delim = "";
        for (ParDeclOp parDeclOp : funDeclOp.getParDeclOpList()) {
            workingBuffer.append(delim);
            parDeclOp.accept(this);
            delim = ", ";
        }

        workingBuffer.append(")");
    }

    private void genExprCode(Expr expr) {
        if(expr == null)
            return;

        if(expr instanceof Id id)
            genIdCode(id, true);
        else
            expr.accept(this);
    }

    private void genIdCode(Id id, boolean ALLOW_DEREFERENCE) {
        // se è l'id di una funzione
        if(id.getKind().equals(Kind.FUN)) {
            id.accept(this);
            return;
        }

        // se è l'id di una variabile
        VarEnvEntry varEnvEntry = (VarEnvEntry) id.getEnv().lookup(id.getKind(), id.getAttribute());
        if(ALLOW_DEREFERENCE && varEnvEntry.isOut()) {
            workingBuffer.append("*");
        }
        id.accept(this);
    }

    private void genAssignCode(Id id, Expr expr) {
        if(!id.getType().equals(Type.STRING) && expr == null)
            return;

        // assegno alla variabile una stringa vuota
        if(id.getType().equals(Type.STRING) && expr == null) {
            genIdCode(id, true);
            workingBuffer.append(" = ");
            workingBuffer.append("\"\"");
            workingBuffer.append(";");
            return;
        }

        genIdCode(id, true);
        workingBuffer.append(" = ");
        genExprCode(expr);
        workingBuffer.append(";");
    }

    private void genVarDeclOpListCode(ArrayList<VarDeclOp> varDeclOpList) {
        for(VarDeclOp varDeclOp : varDeclOpList) {
            varDeclOp.accept(this);
        }
        varAssignBuffer.append("\n");

        // faccio il merge dei buffer per includere il codice dei VarDeclOp
        workingBuffer.append(varDeclBuffer);
        workingBuffer.append(varAssignBuffer);

        // svuoto i buffer, così che in futuro non contengano codice vecchio
        varDeclBuffer.setLength(0);
        varAssignBuffer.setLength(0);
    }

    private void genWriteCode(Expr expr) {
        if(expr == null)
            return;

        String type = expr.getType();
        workingBuffer.append("printf(");
        if(type.equals(Type.BOOL)){
            workingBuffer.append("\"" + getCFormatSpecifier(Type.STRING) + "\", ");
            genExprCode(expr);
            workingBuffer.append(" ? \"true\" : \"false\"");
        } else {
            workingBuffer.append("\"" + getCFormatSpecifier(type) + "\", ");
            genExprCode(expr);
        }
        workingBuffer.append(");");
        workingBuffer.append("\n");
    }

    private String escapeString(String toSanitize) {
        String escaped = toSanitize;

        escaped = escaped.replace("\\", "\\\\");
        escaped = escaped.replace("\n", "\\n");
        escaped = escaped.replace("\t", "\\t");
        escaped = escaped.replace("\r", "\\r");
        escaped = escaped.replace("\"", "\\\"");

        return escaped;
    }
}
