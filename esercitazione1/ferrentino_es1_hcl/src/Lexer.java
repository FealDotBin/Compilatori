import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class Lexer {

    private File input;
    private RandomAccessFile raf;
    private static HashMap<String, Token> stringTable;
    private int state;

    /**
     * Inizializza il Lexer con il file sorgente
     * @param filePath è il percorso del file sorgente
     */
    public Lexer(String filePath){
        stringTable = new HashMap<>();
        state = 0;

        stringTable.put("if", new Token("IF"));
        stringTable.put("then", new Token("THEN"));
        stringTable.put("else", new Token("ELSE"));
        stringTable.put("while", new Token("WHILE"));
        stringTable.put("int", new Token("INT"));
        stringTable.put("float", new Token("FLOAT"));

        initialize(filePath);
    }

    /** Si apre un RandomAccessFile sul file specificato.
     * @param filePath Il percorso del file da compilare.
     * @return True se il file esiste; False altrimenti.
     */
    public Boolean initialize(String filePath){
        try {
            input = new File(filePath);
            raf = new RandomAccessFile(input, "r");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Dà in output il prossimo Token, prodotto in seguito al parsing del file sorgente.
     * Il metodo utilizza degli automi a stati finiti per riconoscere i pattern associati ai diversi token.
     * @return Il token trovato.
     * @throws Exception In caso in cui si sia raggiunto l'EOF oppure il carattere contenuto nel sorgente
     * non è riconosciuto da nessun pattern.
     */
    public Token nextToken() throws Exception{

        // Ad ogni chiamata del Lexer (nextToken())
        // si resettano tutte le variabili utilizzate
        state = 0;
        String lessema = "";
        char c;

        while(true){
            // Legge un carattere da input e lancia eccezione quando incontra EOF per restituire null
            // per indicare che non ci sono più token
            c = (char) raf.readByte();

            // whitespaces
            switch(state){
                case 0:
                    if(c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                        state = 1;
                    } else
                        state = 9;
                    break;

                case 1:
                    if(c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                        state = 0;
                        retrack();
                        break;
                    }

                default: break;
            }

            // id
            switch (state){
                case 9:
                    if(Character.isLetter(c)) {
                        state = 10;
                        lessema += c;
                        // Nel caso in cui il file sia terminato ma ho letto qualcosa di valido
                        // devo lanciare il token (altrimenti perderei l'ultimo token, troncato per l'EOF)
                        if (raf.getFilePointer() == raf.length())
                            return installID(lessema);
                        break;
                    }
                    else {
                        state = 12;
                        break;
                    }

                case 10:
                    if(Character.isLetterOrDigit(c)){
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return installID(lessema);
                        break;
                    }
                    else{
                        state = 11;
                        retrack();
                        return installID(lessema);
                    }

                default: break;
            }

            // unsigned numbers
            switch(state){
                case 12:
                    if(Character.isDigit(c)){
                        state = 13;
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("NUMBER", lessema);
                        break;
                    }
                    else {
                        state = 30;
                        break;
                    }

                case 13:
                    if(Character.isDigit(c)){
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("NUMBER", lessema);
                        break;
                    }
                    else if(c == '.'){
                        state = 14;
                        lessema += c;
                        // se il file termina con un punto (.) devo fare un retract e rimuovere il punto (.) da lessema
                        if(raf.getFilePointer() == raf.length()) {
                            retrack();
                            lessema = lessema.substring(0, lessema.length() - 1);
                            return new Token("NUMBER", lessema);
                        }
                        break;
                    }
                    else if(c == 'E'){
                        state = 16;
                        lessema += c;
                        // se il file termina con il carattere E devo fare retract e rimuovere la E da lessema
                        if(raf.getFilePointer() == raf.length()){
                            retrack();
                            lessema = lessema.substring(0, lessema.length() - 1);
                            return new Token("NUMBER", lessema);
                        }
                        break;
                    }
                    else{
                        state = 20;
                        retrack();
                        return new Token("NUMBER", lessema);
                    }

                case 14:
                    if(Character.isDigit(c)){
                        state = 15;
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("NUMBER", lessema);
                        break;
                    }
                    else{
                        state = 21;
                        retrack();
                        retrack();
                        lessema = lessema.substring(0, lessema.length() - 1);
                        return new Token("NUMBER", lessema);
                    }

                case 15:
                    if(Character.isDigit(c)){
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("NUMBER", lessema);
                        break;
                    } else if(c == 'E'){
                        state = 16;
                        lessema += c;
                        // se il file termina con il carattere E devo fare retract e rimuovere la E da lessema
                        if(raf.getFilePointer() == raf.length()){
                            retrack();
                            lessema = lessema.substring(0, lessema.length() - 1);
                            return new Token("NUMBER", lessema);
                        }
                        break;
                    }
                    else{
                        state = 22;
                        retrack();
                        return new Token("NUMBER", lessema);
                    }

                case 16:
                    if(c == '+' || c == '-'){
                        state = 17;
                        lessema += c;
                        // se il file termina con E+ oppure E- devo fare un doppio retract e rimuoverli da lessema
                        if(raf.getFilePointer() == raf.length()){
                            retrack();
                            retrack();
                            lessema = lessema.substring(0, lessema.length() - 2);
                            return new Token("NUMBER", lessema);
                        }
                        break;
                    }
                    else if(Character.isDigit(c)){
                        state = 18;
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("NUMBER", lessema);
                        break;
                    }
                    else{
                        state = 23;
                        retrack();
                        retrack();
                        lessema = lessema.substring(0, lessema.length() - 1);
                        return new Token("NUMBER", lessema);
                    }

                case 17:
                    if(Character.isDigit(c)){
                        state = 18;
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("NUMBER", lessema);
                        break;
                    }
                    else{
                        state = 24;
                        retrack();
                        retrack();
                        retrack();
                        lessema = lessema.substring(0, lessema.length() - 2);
                        return new Token("NUMBER", lessema);
                    }

                case 18:
                    if(Character.isDigit(c)){
                        lessema += c;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("NUMBER", lessema);
                        break;
                    }
                    else{
                        state = 19;
                        retrack();
                        return new Token("NUMBER", lessema);
                    }

                default: break;
            }

            // Relop ed Assign
            switch(state){
                case 30:
                    if(c == '<'){
                        state = 31;
                        if(raf.getFilePointer() == raf.length())
                            return new Token("RELOP", "LT");
                        break;
                    }
                    else if(c == '=') {
                        state = 38;
                        return new Token("RELOP", "EQ");
                    }
                    else if(c == '>') {
                        state = 39;
                        if (raf.getFilePointer() == raf.length())
                            return new Token("RELOP", "GT");
                        break;
                    }
                    else{
                        state = 50;
                        break;
                    }

                case 31:
                    if(c == '='){
                        state = 32;
                        return new Token("RELOP", "LE");
                    }
                    else if(c == '>') {
                        state = 33;
                        return new Token("RELOP", "NE");
                    }
                    else if(c == '-'){
                        state = 34;
                        // se il file termina con <- devo fare un retract
                        if(raf.getFilePointer() == raf.length()){
                            retrack();
                            return new Token("RELOP", "LT");
                        }
                        break;
                    }
                    else{
                        state = 37;
                        retrack();
                        return new Token("RELOP", "LT");
                    }

                case 34:
                    if(c == '-'){
                        state = 35;
                        return new Token("ASSIGN");
                    }
                    else{
                        state = 36;
                        retrack();
                        retrack();
                        return new Token("RELOP", "LT");
                    }

                case 39:
                    if(c == '='){
                        state = 40;
                        return new Token("RELOP", "GE");
                    }
                    else {
                        state = 41;
                        retrack();
                        return new Token("RELOP", "GT");
                    }

                default: break;
            }

            // Separatori
            switch(state){
                case 50:
                    if(c == '('){
                        state = 51;
                        return new Token("LPAR");
                    }
                    else if (c == ')'){
                        state = 52;
                        return new Token("RPAR");
                    }
                    else if (c == '{'){
                        state = 53;
                        return new Token("LBRACK");
                    }
                    else if (c == '}'){
                        state = 54;
                        return new Token("RBRACK");
                    }
                    else if (c == ','){
                        state = 55;
                        return new Token("COMMA");
                    }
                    else if (c == ';'){
                        state = 56;
                        return new Token("SEMI");
                    }
                    else{
                        // Poiché il carattere non è stato riconosciuto da nessuno e non ci sono più automi da valutare,
                        // lancio eccezione.
                        throw new RuntimeException("Cannot resolve symbol '" + c + "'");
                    }

                default: break;
            }

        }
    }

    /**
     * Restituisce il token associato al lessema dato in input.
     * Se il token è già presente nella stringTable, viene restituito; altrimenti viene dapprima installato e poi dato
     * in output.
     * @param lessema Il lessema da inserire nella stringTable
     * @return Il token associato al lessema
     */
    private Token installID(String lessema){
        Token token;

        // utilizzo come chiave della hashmap il lessema
        if(stringTable.containsKey(lessema))
            return stringTable.get(lessema);
        else{
            token = new Token("ID", lessema);
            stringTable.put(lessema, token);
            return token;
        }
    }

    /**
     * Riporta il puntatore associato al file dato in input al Lexer al byte precedente.
     */
    private void retrack(){
        try {
            long filePointer = raf.getFilePointer(); // puntatore corrente associato al file
            raf.seek(filePointer - 1); // forzo il puntatore al byte precedente
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
