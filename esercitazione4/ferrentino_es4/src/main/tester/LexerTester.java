package main.tester;

import java_cup.runtime.Symbol;
import main.generated.Lexer;
import main.generated.Token;

import java.io.FileReader;
import java.io.Reader;

public class LexerTester {

    public static void main(String[] args) {
        if (args.length == 0){
            System.err.println("Nessun file specificato!");
            return;
        }
        String filePath = args[0];

        /* Crea un reader sul file di input */
        try {
            Reader in;
            in = new FileReader(filePath);
            Lexer lexer = new Lexer(in);
            Symbol sym;

            /* Lexer Tester */
            for (sym = lexer.next_token(); sym.sym != Token.EOF; sym = lexer.next_token())
                System.out.println("<" + Token.terminalNames[sym.sym] + ">");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
