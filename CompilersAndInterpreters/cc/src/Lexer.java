import java.io.*;
import java.nio.charset.StandardCharsets;

public class Lexer {
    private final InputStreamReader file;
    private final StringBuilder buffer = new StringBuilder();
    private Token tokenBuffer = null;

    public Lexer(String filepath) {
        try {
            file = new InputStreamReader(new FileInputStream(filepath), StandardCharsets.UTF_8);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * ReadPart
     */

    private void fillBuffer(int length) {
        try {
            while (buffer.length() < length) {
                int character = file.read();
                if (character == -1) break;
                buffer.append((char) character);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String peek(int length) {
        fillBuffer(length);
        return buffer.substring(0, Math.min(buffer.length(), length));
    }

    private String read(int length) {
        fillBuffer(length);
        String result = buffer.substring(0, Math.min(buffer.length(), length));
        buffer.delete(0, Math.min(buffer.length(), length));
        return result;
    }

    private char peek() {
        String s = peek(1);
        if (s.isEmpty()) return '\0';
        return s.charAt(0);
    }

    private char read() {
        String s = read(1);
        if (s.isEmpty()) return '\0';
        return s.charAt(0);
    }

    /*
     * Token
     */
    private Token parseToken() {
        while (true) {
            while (Character.isWhitespace(peek())) read();
            if (peek(2).equals("//")) {
                while (true) {
                    if ("\n\0".indexOf(read()) >= 0) break;
                }
                continue;
            }
            break;
        }

        if (peek() == '\0') return new Token("", TokenType.EOF);

        if (Character.isLetter(peek()) || peek() == '_') {
            Token token = new Token("", TokenType.WORD);
            do token.value += read(); while (Character.isLetterOrDigit(peek()));
            return token;
        } else if (Character.isDigit(peek())) {
            Token token = new Token("", TokenType.NUMBER);
            do token.value += read(); while (Character.isDigit(peek()));
            return token;
        } else {
            String[] operators = {
                    ">>=", "<<=",
                    "+=", "-=", "*=", "/=", "%=", "&=", "^=", "|=",
                    "<=", ">=", "==", "!=",
                    "<<", ">>", "&&", "||",
                    "++", "--",
                    ">", "<",
                    "(", ")", "[", "]", "{", "}",
                    "&", "|", "^", "~", "!",
                    "+", "-", "/", "*", "%",
                    ".", ",", "?", ":", ";", "=",
                    "@"
            };
            for (String operator : operators) {
                if (peek(operator.length()).equals(operator)) {
                    return new Token(read(operator.length()), TokenType.OPERATOR);
                }
            }
        }
        throw new RuntimeException("Undefined token!");
    }

    public Token peekToken() {
        if (tokenBuffer == null) tokenBuffer = parseToken();
        return tokenBuffer;
    }

    public Token nextToken() {
        Token token;
        if (tokenBuffer == null) token = parseToken();
        else {
            token = tokenBuffer;
            tokenBuffer = null;
        }
        return token;
    }

    public boolean match(String value, TokenType type) {
        if (peekToken().value.equals(value) && peekToken().type == type) {
            nextToken();
            return true;
        }
        return false;
    }

    public boolean match(TokenType type) {
        if (peekToken().type == type) {
            nextToken();
            return true;
        }
        return false;
    }

    public boolean lookMatch(String value, TokenType type) {
        return peekToken().value.equals(value) && peekToken().type == type;
    }

    public boolean lookMatch(TokenType type) {
        return peekToken().type == type;
    }

    public String consume(String value, TokenType type) {
        Token token = nextToken();
        if (token.type != type || !token.value.equals(value)) {
            throw new RuntimeException(String.format("Expected '%s' (%s), got %s!", value, type, token));
        }
        return token.value;
    }

    public String consume(TokenType type) {
        Token token = nextToken();
        if (token.type != type) throw new RuntimeException(String.format("Expected %s, got %s!", type, token));
        return token.value;
    }
}

enum TokenType {
    OPERATOR,
    NUMBER,
    WORD,
    EOF
}

class Token {
    String value;
    TokenType type;

    public Token(String value, TokenType type) {
        this.value = value;
        this.type = type;
    }

    @Override
    public String toString() {
        return String.format("'%s' (%s)", value, type);
    }
}