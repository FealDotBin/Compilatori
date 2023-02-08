package main.tester;

import java_cup.runtime.Symbol;
import main.generated.Lexer;
import main.generated.Parser;

import java.io.FileReader;
import java.io.Reader;

public class ParserTester {

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

            /* Parser Tester */
            Parser parser = new Parser(lexer);
            parser.debug_parse();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
