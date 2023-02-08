package main.java;

import visitor.*;
import generated.Lexer;
import generated.Parser;
import syntaxtree.node.ProgramOp;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.nio.file.Paths;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        if (args.length != 1 ){
            System.err.println("Numero di parametri incorretto.");
            System.err.println("Usage: Main <path_to_src_file>");
            System.exit(1);
        }

        // file di input
        String srcFilePath = Paths.get(args[0]).toString().replace("\\", "/"); // normalizzo il separatore come "/"
        String srcFilePathTokenized[] = srcFilePath.split("\\/"); // separo il path del file di input con "/"
        String srcFileNameTokenized[] = srcFilePathTokenized[srcFilePathTokenized.length-1].split("\\."); // separo il nome del file con "."
        String srcFileName = srcFileNameTokenized[0];
        String srcFileExt = srcFileNameTokenized[1];

        // file di output
        String dstDirectoryPath = "test_files" + File.separator + "c_out";
        String dstFilePath = dstDirectoryPath + File.separator + srcFileName + ".c";

        try {
            // Crea un reader sul file di input
            Reader in;
            in = new FileReader(srcFilePath);

            // Creo Lexer e Parser ed effettuo il parsing del file di input
            Lexer lexer = new Lexer(in);
            Parser parser = new Parser(lexer);
            ProgramOp programOp = (ProgramOp) parser.parse().value;

            // IdSetKindVisitor
            IdSetKindVisitor idSetKindVisitor = new IdSetKindVisitor();
            programOp.accept(idSetKindVisitor);

            // EnvVisitor
            EnvVisitor envVisitor = new EnvVisitor();
            programOp.accept(envVisitor);

            // TypeCheckVisitor Tester
            TypeCheckVisitor typeCheckVisitor = new TypeCheckVisitor();
            programOp.accept(typeCheckVisitor);

            // XMLVisitor
            XMLVisitor xmlvisitor = new XMLVisitor();
            Document document = (Document) programOp.accept(xmlvisitor);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(".\\albero_sintattico.xml"));
            transformer.transform(domSource, streamResult);

            // CGenVisitor
            CGenVisitor cGenVisitor = new CGenVisitor();
            programOp.accept(cGenVisitor);

            try {
                // Apro il file di destinazione. Se la directory padre non esiste, la creo.
                File dstDirectory = new File(dstDirectoryPath);
                if(! dstDirectory.exists()) {
                    dstDirectory.mkdir();
                }
                File dstFile = new File(dstFilePath);

                // Scrivo nel file di destinazione
                FileOutputStream file = new FileOutputStream(dstFile);
                PrintStream Output = new PrintStream(file);
                Output.println(cGenVisitor.getCodeBuffer());
            } catch (IOException e) {
                System.out.println("Errore: " + e);
                System.exit(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
