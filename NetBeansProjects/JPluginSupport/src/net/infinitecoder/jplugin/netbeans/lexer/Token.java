package net.infinitecoder.jplugin.netbeans.lexer;

public class Token {
    private final TokenType type;
    private final String value;
    private final int position;

    public Token(TokenType type, int position) {
        this.value = "";
        this.type = type;
        this.position = position;
    }
    
    public Token(TokenType type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getPosition() {
        return position;
    }
    
}
