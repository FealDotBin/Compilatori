package generated;

import java_cup.runtime.*;
import java.util.HashMap;

%%

%public
%class Lexer
%cupsym Token
%cup
%line
%column

%{
        StringBuffer string = new StringBuffer();

        /* Funzioni per la costruzione di symbols */
        private Symbol symbol(int typeOp) {
          return new Symbol(typeOp);
        }
        private Symbol symbol(int typeOp, Object value) {
          return new Symbol(typeOp, value);
        }

        /* Gestione della Symbol Table */
        HashMap<String, Symbol> symbolTable = new HashMap<>();

        private Symbol installID(String lessema) {
            Symbol symbol = symbol(Token.ID, lessema);
            if(!symbolTable.containsKey(lessema)) {
                symbolTable.put(lessema, symbol);
            }
            return symbol;
        }
%}

Digit = [0-9]
Digits = {Digit}+
Delim = [ \t\r\n]
Delims = {Delim}+
InputCharacter = [^\r\n]
LineTerminator = \r|\n|\r\n

Identifier = [$_A-Za-z][$_A-Za-z0-9]*
IntegerConstant = {Digits}
RealConstant = {Digits}\.{Digits}

/* comments */
Comment = {LineComment} | {BlockComment}

LineComment = "||" {InputCharacter}* {LineTerminator}?
BlockComment = "|*" [^|]* "|"

/* states */
%state STRING
%state CHAR_START
%state CHAR_END

%%

/* keywords */
<YYINITIAL> "start:"         { return symbol(Token.MAIN); }
<YYINITIAL> "var"            { return symbol(Token.VAR); }
<YYINITIAL> "integer"        { return symbol(Token.INT); }
<YYINITIAL> "float"          { return symbol(Token.REAL); }
<YYINITIAL> "string"         { return symbol(Token.STRING); }
<YYINITIAL> "boolean"        { return symbol(Token.BOOL); }
<YYINITIAL> "char"           { return symbol(Token.CHAR); }
<YYINITIAL> "void"           { return symbol(Token.VOID); }
<YYINITIAL> "def"            { return symbol(Token.DEF); }
<YYINITIAL> "out"            { return symbol(Token.OUT); }
<YYINITIAL> "for"            { return symbol(Token.FOR); }
<YYINITIAL> "if"             { return symbol(Token.IF); }
<YYINITIAL> "then"           { return symbol(Token.THEN); }
<YYINITIAL> "else"           { return symbol(Token.ELSE); }
<YYINITIAL> "while"          { return symbol(Token.WHILE); }
<YYINITIAL> "to"             { return symbol(Token.TO); }
<YYINITIAL> "loop"           { return symbol(Token.LOOP); }
<YYINITIAL> "return"         { return symbol(Token.RETURN); }
<YYINITIAL> "true"           { return symbol(Token.TRUE); }
<YYINITIAL> "false"          { return symbol(Token.FALSE); }
<YYINITIAL> "and"            { return symbol(Token.AND); }
<YYINITIAL> "or"             { return symbol(Token.OR); }
<YYINITIAL> "not"            { return symbol(Token.NOT); }

<YYINITIAL> {

    /* identifiers */
    {Identifier}             { return installID(yytext()); }

    /* literals */
    {IntegerConstant}        { return symbol(Token.INTEGER_CONST, yytext()); }
    {RealConstant}           { return symbol(Token.REAL_CONST, yytext()); }
    \"                       { string.setLength(0); yybegin(STRING); }
    \'                       { string.setLength(0); yybegin(CHAR_START); }

    /* separators */
    ";"                      { return symbol(Token.SEMI); }
    ","                      { return symbol(Token.COMMA); }
    "|"                      { return symbol(Token.PIPE); }
    "("                      { return symbol(Token.LPAR); }
    ")"                      { return symbol(Token.RPAR); }
    "{"                      { return symbol(Token.LBRACK); }
    "}"                      { return symbol(Token.RBRACK); }
    ":"                      { return symbol(Token.COLON); }

    /* assign and operators */
    "<--"                    { return symbol(Token.READ); }
    "-->"                    { return symbol(Token.WRITE); }
    "-->!"                   { return symbol(Token.WRITELN); }
    "<<"                     { return symbol(Token.ASSIGN); }
    "+"                      { return symbol(Token.PLUS); }
    "-"                      { return symbol(Token.MINUS); }
    "*"                      { return symbol(Token.TIMES); }
    "/"                      { return symbol(Token.DIV); }
    "^"                      { return symbol(Token.POW); }
    "&"                      { return symbol(Token.STR_CONCAT); }
    "="                      { return symbol(Token.EQ); }
    "<>"                     { return symbol(Token.NE); }
    "!="                     { return symbol(Token.NE); }
    "<"                      { return symbol(Token.LT); }
    "<="                     { return symbol(Token.LE); }
    ">"                      { return symbol(Token.GT); }
    ">="                     { return symbol(Token.GE); }

    /* comments */
    {Comment}                { /* ignore */ }

    /* delimitators */
    {Delims}                 { /* ignore */ }
}

<STRING> {
    \"                       { yybegin(YYINITIAL);
                                return symbol(Token.STRING_CONST,
                                string.toString()); }
    [^\n\r\"\\]+             { string.append(yytext()); }
    \\t                      { string.append('\t'); }
    \\n                      { string.append('\n'); }
    \\r                      { string.append('\r'); }
    \\\"                     { string.append('\"'); }
    \\\\                     { string.append('\\'); }
}

<CHAR_START> {
    [^\n\r\t\'\\]            { yybegin(CHAR_END); string.append(yycharat(0)); }
    \\n                      { yybegin(CHAR_END); string.append("\n"); }
    \\r                      { yybegin(CHAR_END); string.append("\r"); }
    \\t                      { yybegin(CHAR_END); string.append("\t"); }
    \\\'                     { yybegin(CHAR_END); string.append("\'"); }
    \\\\                     { yybegin(CHAR_END); string.append("\\"); }
}

<CHAR_END> {
    \'                       { yybegin(YYINITIAL); return symbol(Token.CHAR_CONST, string.toString()); }
}

/* error fallback */
[^]                          { throw new Error("Illegal character <" + yytext() + ">" +
                            " at line " + yyline + ", column " + yycolumn); }