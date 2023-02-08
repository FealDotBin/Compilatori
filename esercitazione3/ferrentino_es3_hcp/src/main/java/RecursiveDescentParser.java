/**
 * Grammatica di riferimento
 *
 * S -> Program EOF
 * Program -> Stmt MoreStmt
 * MoreStmt -> ; Stmt MoreStmt | ε
 * Stmt -> IF Expr THEN Stmt StmtTail
 *          | ID ASSIGN Expr
 *          | WHILE Expr LOOP Stmt END LOOP
 * StmtTail -> END IF
 *          | ELSE Stmt END IF
 * Expr -> ID MoreExpr | NUMBER MoreExpr
 * MoreExpr -> RELOP IdOrNumber MoreExpr | ε
 * IdOrNumber -> ID | NUMBER
 */

package main.java;

import Lexer.*;
import Token.Token;

/**
 * Questa classe implementa un Parser a discesa ricorsiva, con backtrack e senza look-ahead.
 */
public class RecursiveDescentParser {
    private static Lexer lexer;

    public static void main(String args[]) {
        if (args.length != 1) {
            System.out.println("Errore, nessun file di input specificato.");
            System.exit(0);
        }
        String filePath = args[0];

        /* Inizializzo un nuovo lexer.
        * Posso scegliere di inizializzare un ConcreteLexer (senza cache)
        * oppure un ProxyLexer (con cache, più performante) */
        lexer = new ProxyLexer(filePath);

        try {
            boolean isValid = S();
            if ((isValid) && lexer.nextToken().getName().equals("EOF")) {
                System.out.println("Input is valid");
            } else {
                System.out.println("Syntax error");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static boolean S() throws Exception {
        long position = lexer.getPosition();

        /* Controllo se S -> Program EOF */
        if(Program() && lexer.nextToken().getName().equals("EOF")){
            return true;
        } else {
            lexer.setPosition(position);
            return false;
        }
    }

    private static boolean Program() throws Exception {
        long position = lexer.getPosition();

        /* Controllo se Program -> Stmt MoreStmt */
        if(Stmt() && MoreStmt()){
            return true;
        } else {
            lexer.setPosition(position);
            return false;
        }
    }

    private static boolean Stmt() throws Exception {
        long position = lexer.getPosition();

        /* Controllo se Stmt -> IF Expr THEN Stmt StmtTail */
        if(isIfStmt()){
            return true;
        } else lexer.setPosition(position);

        /* Controllo se Stmt -> ID ASSIGN Expr */
        if(isAssignStmt()) {
            return true;
        } else lexer.setPosition(position);

        /* Controllo se Stmt -> WHILE Expr LOOP Stmt END LOOP */
        if (isWhileStmt()) {
            return true;
        } else lexer.setPosition(position);

        return false;
    }

    private static boolean isIfStmt() throws Exception {
        return (lexer.nextToken().getName().equals("IF") &&
                Expr() == true &&
                lexer.nextToken().getName().equals("THEN") &&
                Stmt() == true &&
                StmtTail() == true
        );
    }

    private static boolean isAssignStmt() throws Exception {
        return (lexer.nextToken().getName().equals("ID") &&
                lexer.nextToken().getName().equals("ASSIGN") &&
                Expr() == true
        );
    }

    private static boolean isWhileStmt() throws Exception {
        return (lexer.nextToken().getName().equals("WHILE") &&
                Expr() == true &&
                lexer.nextToken().getName().equals("LOOP") &&
                Stmt() == true &&
                lexer.nextToken().getName().equals("END") &&
                lexer.nextToken().getName().equals("LOOP")
        );
    }

    private static boolean MoreStmt() throws Exception {
        long position = lexer.getPosition();

        /* Controllo se MoreStmt -> ε */
        if(!lexer.nextToken().getName().equals("SEMI")) {
            lexer.setPosition(position);
            return true;
        }

        /* Se arrivo in questo punto d'esecuzione, vuol dire che il Token era ";"
        * quindi controllo se MoreStmt -> ; Stmt MoreStmt */
        if(Stmt() && MoreStmt()){
            return true;
        } else {
            lexer.setPosition(position);
            return false;
        }
    }

    private static boolean StmtTail() throws Exception {
        long position = lexer.getPosition();

        /* Controllo se StmtTail -> END IF */
        if(isIfStmtTail()) {
            return true;
        } else lexer.setPosition(position);

        /* Controllo se StmtTail -> ELSE Stmt END IF */
        if(isElseStmtTail()) {
            return true;
        } else lexer.setPosition(position);

        return false;
    }

    private static boolean isIfStmtTail() throws Exception {
        return (lexer.nextToken().getName().equals("END") &&
                lexer.nextToken().getName().equals("IF")
        );
    }

    private static boolean isElseStmtTail() throws Exception {
        return (lexer.nextToken().getName().equals("ELSE") &&
                Stmt() == true &&
                lexer.nextToken().getName().equals("END") &&
                lexer.nextToken().getName().equals("IF")
        );
    }

    private static boolean Expr() throws Exception {
        long position = lexer.getPosition();

        /* Controlla se Expr -> ID MoreExpr */
        if (lexer.nextToken().getName().equals("ID") && MoreExpr()){
            return true;
        } else lexer.setPosition(position);

        /* Controlla se Expr -> NUMBER MoreExpr */
        if(lexer.nextToken().getName().equals("NUMBER") && MoreExpr()) {
            return true;
        } else lexer.setPosition(position);

        return false;
    }

    private static boolean MoreExpr() throws Exception {
        long position = lexer.getPosition();

        /* Controllo se MoreExpr -> ε */
        if (!lexer.nextToken().getName().equals("RELOP")){
            lexer.setPosition(position);
            return true;
        }

        /* Se arrivo in questo punto d'esecuzione, vuol dire che il Token era "RELOP"
         * quindi controllo se MoreExpr -> RELOP IdOrNumber MoreExpr */
        if(IdOrNumber() && MoreExpr()){
            return true;
        } else {
            lexer.setPosition(position);
            return false;
        }
    }

    private static boolean IdOrNumber() throws Exception {
        long position = lexer.getPosition();
        Token token = lexer.nextToken();

        if(token.getName().equals("ID") || token.getName().equals("NUMBER")) {
            return true;
        } else {
            lexer.setPosition(position);
            return false;
        }
    }

}
