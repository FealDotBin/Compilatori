/* JFlex per la generazione del Lexer per l'esercitazione 2 */
package main.java.org.example;

import java_cup.runtime.*;
import main.java.org.example.*;import java.util.HashMap;

%%

%class Lexer
%cupsym Token
%cup
%line
%column


%{
        /* Funzioni per la costruzione di symbols */
        private Symbol symbol(int type) {
          return new Symbol(type);
        }
        private Symbol symbol(int type, Object value) {
          return new Symbol(type, value);
        }

        /* Gestione della Symbol Table */
        HashMap<String, Symbol> symbolTable = new HashMap<>();

        private Symbol installID(String lessema) {
            if(symbolTable.containsKey(lessema))
                return symbolTable.get(lessema);
            else {
                Symbol symbol = symbol(Token.ID, lessema);
                symbolTable.put(lessema, symbol);
                return symbol;
            }
        }
%}

Digits = [:digit:]+
Identifier = [:jletter:] [:jletterdigit:]*
Number = {Digits} (\.{Digits})? (E[+-]?{Digits})?
Delim = [ \t\r\n]+

%%

/* keywords */
<YYINITIAL> "if"         { return symbol(Token.IF); }
<YYINITIAL> "then"       { return symbol(Token.THEN); }
<YYINITIAL> "else"       { return symbol(Token.ELSE); }
<YYINITIAL> "while"      { return symbol(Token.WHILE); }
<YYINITIAL> "int"        { return symbol(Token.INT); }
<YYINITIAL> "float"      { return symbol(Token.FLOAT); }

<YYINITIAL> {
    /* identifiers */
    {Identifier}         { return installID(yytext()); }

    /* literals */
    {Number}             { return symbol(Token.NUMBER, yytext()); }

    /* separators */
    "("                  { return symbol(Token.LPAR); }
    ")"                  { return symbol(Token.RPAR); }
    "{"                  { return symbol(Token.LBRACK); }
    "}"                  { return symbol(Token.RBRACK); }
    ","                  { return symbol(Token.COMMA); }
    ";"                  { return symbol(Token.SEMI); }

    /* assign and relops */
    "<--"                { return symbol(Token.ASSIGN); }
    "<"                  { return symbol(Token.RELOP, "LT"); }
    ">"                  { return symbol(Token.RELOP, "GT"); }
    "<="                 { return symbol(Token.RELOP, "LE"); }
    ">="                 { return symbol(Token.RELOP, "GE"); }
    "="                  { return symbol(Token.RELOP, "EQ"); }
    "<>"                 { return symbol(Token.RELOP, "NE"); }

    /* delimitators */
    {Delim}              { /* ignore */ }
}

/* error fallback */
[^]                      { throw new Error("Illegal character <" + yytext() + ">" +
                            " at line " + yyline + ", column " + yycolumn); }