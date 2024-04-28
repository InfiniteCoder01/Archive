package parser.lexer;

import java.util.ArrayList;
import java.util.List;

public class Tokenizer {
    private static final String OPERATOR_CHARS = "+-*/%()[]{}=<>!&|.,~";
    private static boolean isHexNumber(char current) {
        return Character.isDigit(current)
                || ('a' <= current && current <= 'f')
                || ('A' <= current && current <= 'F');
    }

    private final String code;
    private List<Token> tokens;
    private final int length;
    private int pos;

    public Tokenizer(String code) {
        this.tokens = new ArrayList<>();
        this.code = code;
        this.length = code.length();
    }

    public List<Token> tokenize() {
        while (pos < length) {
            final char current = peek(0);
            if (Character.isDigit(current)) {
                tokenizeNumber();
            } else if ((Character.isLetter(current) || (current == '_') || (current == '$'))) {
                tokenizeWord();
            } else if (current == '"') {
                tokenizeText();
            } else if (current == '#') {
                next();
                tokenizeHexNumber(1);
            } else if (OPERATOR_CHARS.indexOf(current) != -1) {
                tokenizeOperator();
            } else {
                next();
            }
        }
        tokens.add(new Token(TokenType.EOF));
        return tokens;
    }

    private void tokenizeNumber() {
        StringBuilder buff = new StringBuilder();
        while (Character.isDigit(peek(0))) {
            buff.append(peek(0));
            pos++;
        }
        addToken(TokenType.NUMBER, buff.toString());
    }

    private void tokenizeHexNumber(int skipped) {
        StringBuilder buff = new StringBuilder();
        char current = peek(0);
        while (isHexNumber(current) || (current == '_')) {
            if (current != '_') {
                buff.append(current);
            }
            current = next();
        }
        final int length = buff.length();
        if (length > 0) {
            addToken(TokenType.HEX_NUMBER, buff.toString());
        }
    }


    private void tokenizeWord() {
        StringBuilder buff = new StringBuilder();
        buff.append(peek(0));
        char current = next();
        while (true) {
            if (!(Character.isLetterOrDigit(current) || (current == '_') || (current == '$'))) {
                break;
            }
            buff.append(current);
            current = next();
        }

        final String word = buff.toString();
        if (Tokens.KEYWORDS.containsKey(word)) {
            addToken(Tokens.KEYWORDS.get(word));
        } else {
            addToken(TokenType.WORD, word);
        }
    }

    private void tokenizeText() {
        next();// skip "
        StringBuilder buff = new StringBuilder();
        char current = peek(0);
        while (true) {
            if (current == '\\') {
                current = next();
                switch (current) {
                    case '"':
                        current = next();
                        buff.append('"');
                        continue;
                    case '0':
                        current = next();
                        buff.append('\0');
                        continue;
                    case 'b':
                        current = next();
                        buff.append('\b');
                        continue;
                    case 'f':
                        current = next();
                        buff.append('\f');
                        continue;
                    case 'n':
                        current = next();
                        buff.append('\n');
                        continue;
                    case 'r':
                        current = next();
                        buff.append('\r');
                        continue;
                    case 't':
                        current = next();
                        buff.append('\t');
                        continue;
                    case 'u': // http://docs.oracle.com/javase/specs/jls/se8/html/jls-3.html#jls-3.3
                        int rollbackPosition = pos;
                        while (current == 'u') {
                            current = next();
                        }
                        int escapedValue = 0;
                        for (int i = 12; i >= 0 && escapedValue != -1; i -= 4) {
                            if (isHexNumber(current)) {
                                escapedValue |= (Character.digit(current, 16) << i);
                            } else {
                                escapedValue = -1;
                            }
                            current = next();
                        }
                        if (escapedValue >= 0) {
                            buff.append((char) escapedValue);
                        } else {
                            // rollback
                            buff.append("\\u");
                            pos = rollbackPosition;
                        }
                        continue;
                }
                buff.append('\\');
                continue;
            }
            if (current == '"') {
                break;
            }
            if (current == '\0') {
                throw new RuntimeException("Reached end of file while parsing text string.");
            }
            buff.append(current);
            current = next();
        }
        next(); // skip closing "

        addToken(TokenType.TEXT, buff.toString());
    }

    private void tokenizeOperator() {
        StringBuilder buff = new StringBuilder();
        char current = peek(0);
        if (current == '/') {
            if (peek(1) == '/') {
                next();
                next();
                tokenizeComment();
                return;
            } else if (peek(1) == '*') {
                next();
                next();
                tokenizeMultilineComment();
                return;
            }
        }
        while (true) {
            final String text = buff.toString();
            if (!text.isEmpty() && !Tokens.OPERATORS.containsKey(text + current)) {
                addToken(Tokens.OPERATORS.get(text));
                return;
            }
            buff.append(current);
            current = next();
        }
    }

    private void tokenizeComment() {
        char current = peek(0);
        while ("\r\n\0".indexOf(current) == -1) {
            current = next();
        }
    }

    private void tokenizeMultilineComment() {
        char current = peek(0);
        while (true) {
            if (current == '*' && peek(1) == '/') {
                break;
            }
            if (current == '\0') {
                return;
            }
            current = next();
        }
        next(); // *
        next(); // /
    }

    private char next() {
        pos++;
        return peek(0);
    }

    private char peek(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= length) {
            return '\0';
        }
        return code.charAt(position);
    }

    private void addToken(TokenType type) {
        addToken(type, "");
    }

    private void addToken(TokenType type, String value) {
        tokens.add(new Token(value, type));
    }

}
