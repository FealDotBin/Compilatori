package Lexer;

import Token.Token;

public interface Lexer {
    public Token nextToken() throws Exception;
    public long getPosition();
    public void setPosition(long position);
}
