package env;

import env.enventry.EnvEntry;

import java.util.HashMap;

public class Env {

    private HashMap<EnvKey, EnvEntry> symbolTable;
    private Env previous;

    public Env() {
        previous = null;
        symbolTable = new HashMap<>();
    }

    public Env(Env previous) {
        this.previous = previous;
        symbolTable = new HashMap<>();
    }

    // indica se l'elemento è presente oppure no nello scope
    public boolean scopeContains(String kind, String id) {
        EnvKey envKey = new EnvKey(kind, id);
        return symbolTable.containsKey(envKey);
    }

    // indica se l'elemento è presente oppure no in tutto il type env
    public boolean envContains(String kind, String id) {
        // se trovo l'elemento nello scope, restituisco true
        if(scopeContains(kind, id))
            return true;

        // altrimenti cerco nel type environment
        if(previous != null)
            return previous.envContains(kind, id);

        // nel caso in cui ho raggiunto la tabella globale, restituisco false
        return false;
    }

    // restituisce la prima entry, presente nel type env, associata alla chiave fornita.
    // la ricerca viene fatta bottom up.
    public EnvEntry lookup(String kind, String id) {
        // creo una chiave da cercare nella symbolTable
        EnvKey envKey = new EnvKey(kind, id);

        // se la trovo, restituisco la Entry associata
        if(symbolTable.containsKey(envKey))
            return symbolTable.get(envKey);

        // altrimenti cerco nel type environment
        if(previous != null)
            return previous.lookup(kind, id);

        // nel caso in cui ho raggiunto la tabella globale, lancio eccezione se non ho trovato la chiave
        throw new RuntimeException("Errore: Nel type env. non è presente nessuna variabile o funzione con questo id!");
    }

    // inserisce la entry nello scope
    public void put(String kind, String id, EnvEntry envEntry){
        EnvKey envKey = new EnvKey(kind, id);

        if(scopeContains(kind, id))
            throw new RuntimeException("Errore di dichiarazione multipla");

        symbolTable.put(envKey, envEntry);
    }
}
