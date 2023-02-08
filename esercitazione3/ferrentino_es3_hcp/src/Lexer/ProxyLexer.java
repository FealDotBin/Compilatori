package Lexer;

import Token.Token;

import java.util.ArrayList;

/**
 * Questa classe funge da Proxy per il ConcreteLexer.
 * Ogni qualvolta viene richiesto un token al Proxy, questo lo richiederà a sua volta al ConcreteLexer per poi
 * salvarlo in cache e restituirlo.
 * Nel caso in cui venga richiesto un token già letto in precedenza, il Proxy provvederà a prenderlo dalla sua cache.
 */
public class ProxyLexer implements Lexer {

    ConcreteLexer lexer;
    private ArrayList<Token> cache;
    private int position;

    /**
     * Inizializza la cache ed istanzia un nuovo ConcreteLexer col file di input specificato.
     * @param filePath E' il percorso del file sorgente.
     */
    public ProxyLexer(String filePath){
        lexer = new ConcreteLexer(filePath);
        cache = new ArrayList<>();
        position = 0;
    }

    /**
     * Dà in output il prossimo Token, salvato in una cache.
     * Se il puntatore punta all'ultimo elemento della cache, viene richiesto un nuovo Token al ConcreteLexer, viene
     * salvato all'interno della cache e viene in fine restituito.
     * Qualora si dovesse portare indietro il puntatore alla cache, verrà restituito il Token puntato dal puntatore.
     * @return Il prossimo Token.
     * @throws Exception Nel caso in cui il puntatore alla cache supera la dimensione della cache.
     */
    public Token nextToken() throws Exception {
        Token token;

        if (position < cache.size()) { // se ho dei token in cache da restituire
            token = cache.get(position++);
        } else if (position == cache.size()){ // se non ho token in cache da restituire
            token = lexer.nextToken();
            cache.add(token);
            position++;
        } else {
            throw new RuntimeException("Errore, inconsistenza nella cache del Lexer. Impossibile restituire il token.");
        }
        return token;
    }

    /**
     * Restituisce la posizione del puntatore della cache.
     * @return La posizione del puntatore della cache.
     */
    public long getPosition() {
        return position;
    }

    /**
     * Imposta la posizione del puntatore della cache al valore fornito in input.
     * @param position La posizione a cui impostare il puntatore.
     */
    public void setPosition(long position) {
        this.position = Math.toIntExact(position);
    }
}
