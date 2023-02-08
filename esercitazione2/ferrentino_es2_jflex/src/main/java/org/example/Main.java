package main.java.org.example;
import java_cup.runtime.Symbol;

import java.io.*;
import java.util.HashMap;

public class Main {

    private static HashMap<Integer, String> tokenNameMap;

    public static void main(String[] args) {

        if(args.length != 1)
            System.err.println("Nessun file specificato");

        String filePath = args[0];

        /* Crea un reader sul file di input */
        Reader in;
        try {
            in = new FileReader(filePath);
        } catch (FileNotFoundException e) {
            System.err.println("File not found!!");
            throw new RuntimeException(e);
        }

        /* Crea il Lexer ed inizializza tokenNameMap */
        Lexer lexer = new Lexer(in);
        Symbol symbol;
        initializeTokenNameMap();

        /* Richiede al Lexer tutti i simboli e li stampa in output */
        try {
            while ((symbol = lexer.next_token()).sym != Token.EOF ) {
                printSymbol(symbol);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void printSymbol(Symbol symbol) {
        /* Ottiene il nome del token come stringa */
        String symbolName;
        if((symbolName = tokenNameMap.get(symbol.sym)) == null)
            symbolName = Integer.toString(symbol.sym);

        /* Stampa il simbolo nel formato <Nome,Valore> */
        if (symbol.value == null)
            System.out.println("<" + symbolName + ">");
        else
            System.out.println("<" + symbolName + "," + symbol.value + ">");
    }

    /* Inizializza la tokenNameMap, che memorizza l'associazione tra
     * il valore intero del nome del token e la sua rappresentazione come stringa
     */
    private static void initializeTokenNameMap() {
        tokenNameMap = new HashMap<>();

        tokenNameMap.put(0, "EOF");
        tokenNameMap.put(1, "NUMBER");
        tokenNameMap.put(2, "ID");
        tokenNameMap.put(3, "IF");
        tokenNameMap.put(4, "THEN");
        tokenNameMap.put(5, "ELSE");
        tokenNameMap.put(6, "WHILE");
        tokenNameMap.put(7, "INT");
        tokenNameMap.put(8, "FLOAT");
        tokenNameMap.put(9, "LPAR");
        tokenNameMap.put(10, "RPAR");
        tokenNameMap.put(11, "LBRACK");
        tokenNameMap.put(12, "RBRACK");
        tokenNameMap.put(13, "COMMA");
        tokenNameMap.put(14, "SEMI");
        tokenNameMap.put(15, "ASSIGN");
        tokenNameMap.put(16, "RELOP");
    }
}
