package net.infinitecoder.jplugin;

import java.util.Objects;

public class Token {
    public TokenType type;
    public String value;
    public int line;

    public Token() {
    }

    public Token(TokenType type, String value, int line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    @Override
    public String toString() {
        return "\nType: " + type + ", value: " + value;
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