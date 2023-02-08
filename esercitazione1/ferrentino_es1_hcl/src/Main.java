public class Main {

    public static void main(String[] args) {

        if(args.length != 1)
            System.err.println("Nessun file specificato");

        String filePath = args[0];
        Lexer lexer = new Lexer(filePath);

        if (lexer.initialize(filePath)) {

            Token token;
            try {
                while ((token = lexer.nextToken()) != null) {
                    System.out.println(token);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else
            System.out.println("File not found!!");
    }
}
