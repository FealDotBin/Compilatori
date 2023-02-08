package main.tester;

import main.Visitor.XMLVisitor;
import main.generated.Lexer;
import main.generated.Parser;
import main.syntaxtree.node.ProgramOp;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.Reader;

public class SyntaxTreeTester {

    public static void main(String[] args) {
        if (args.length == 0){
            System.err.println("Nessun file specificato!");
            return;
        }
        String filePath = args[0];

        try {
            /* Crea un reader sul file di input */
            Reader in;
            in = new FileReader(filePath);
            Lexer lexer = new Lexer(in);

            /* SyntaxTree Tester */
            Parser parser = new Parser(lexer);
            ProgramOp programOp = (ProgramOp) parser.parse().value;

            XMLVisitor xmlvisitor = new XMLVisitor();
            Document document = (Document) programOp.accept(xmlvisitor);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(System.getProperty("user.dir") + "\\albero_sintattico.xml"));
            transformer.transform(domSource, streamResult);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
