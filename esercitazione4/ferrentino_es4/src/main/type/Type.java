package main.type;

public interface Type {
    /* Basic Types */
    public static final String INTEGER = "integer";
    public static final String BOOL = "bool";
    public static final String REAL = "real";
    public static final String STRING = "string";
    public static final String CHAR = "char";

    /* Var */
    public static final String VAR = "var";

    /* Void */
    public static final String VOID = "void";

    public static final String[] basicTypes = new String[] {
            INTEGER,
            BOOL,
            REAL,
            STRING,
            CHAR
    };
}
