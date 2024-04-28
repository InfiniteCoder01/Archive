package net.infinitecoder.jplugin;

import java.util.Objects;

public class Token {
    public TokenType type;
    public String value;
    public int line, col;

    public Token() {
    }

    public Token(TokenType type, String value, int line, int col) {
        this.type = type;
        this.value = value;
        this.line = line;
        this.col = col;
    }

    @Override
    public String toString() {
        return "\n" + line + ":" + col + " Type: " + type + ", value: " + value;
    }

    public TokenType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public int getLine() {
        return line;
    }

    public int getCol() {
        return col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Token other = (Token) obj;
        if (!Objects.equals(this.value, other.value)) return false;
        return this.type == other.type;
    }

    @Override
    public int hashCode() {
        return 0;
    }
    
}