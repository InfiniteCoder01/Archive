package net.infinitecoder.jplugin;

import java.util.ArrayList;
import java.util.List;

public class Lexer {

    private int index = 0;
    public final String code;
    public List<Token> tokens;
    public List<TokenizingError> errors;

    public Lexer(String code) {
        this.code = code + "\n ";
    }

    public void tokenize() {
        tokens = new ArrayList<>();
        errors = new ArrayList<>();
        while (index < code.length()) {
            if (code.substring(index).startsWith("//")) {
                while (code.charAt(index) != '\n') {
                    index++;
                    if (code.charAt(index) == '\\') {
                        while (code.charAt(index) != '\n') {
                            index++;
                        }
                    }
                }
            } else if (code.substring(index).startsWith("/*")) {
                int startingIndex = index;
                index += 2;
                while (index < code.length() && !code.substring(index).startsWith("*/")) {
                    index++;
                }
                if (index == code.length()) {
                    errors.add(new TokenizingError(code, startingIndex, "Parsing error: unclosed multi-line comment"));
                    index -= 2;
                }
                index += 2;
            } else if (Character.isDigit(code.charAt(index))) {
                parseNumber();
            } else if (code.charAt(index) == '"') {
                parseString();
            } else if (code.charAt(index) == '\'') {
                parseChar();
            } else if (isOperator()) {
            } else if (Character.isLetter(code.charAt(index)) || code.charAt(index) == '_') {
                //words
                int line = code.substring(0, index + 1).split("\n").length,
                        col = code.substring(0, index + 1).split("\n")[line - 1].length();
                String word = "";
                while (Character.isLetterOrDigit(code.charAt(index)) || code.charAt(index) == '_') {
                    word += code.charAt(index);
                    index++;
                }
                tokens.add(new Token(TokenType.WORD, word, line, col));
            } else {
                index++;//whitespaces
            }
        }
        int line = code.substring(0, Math.min(code.length() - 1, index + 1)).split("\n").length,
                col = code.substring(0, Math.min(code.length() - 1, index + 1)).split("\n")[line - 1].length();
        tokens.add(new Token(TokenType.EOF, "EOF", line, col));
    }

    private void parseNumber() {
        int line = code.substring(0, index + 1).split("\n").length,
                col = code.substring(0, index + 1).split("\n")[line - 1].length();
        String n = "";
        if (code.substring(index).startsWith("0x")) {
            n += "0x";
            index += 2;
        } else if (code.substring(index).startsWith("0b")) {
            n += "0b";
            index += 2;
        } else if (code.substring(index).startsWith("-")) {
            n += "-";
            index += 1;
        }
        while (Character.isLetterOrDigit(code.charAt(index)) || code.charAt(index) == '.') {
            n += code.charAt(index);
            index++;
        }
        if (n.startsWith("0x")) {
            n = Long.parseLong(n.substring(2), 16) + "";
        } else if (n.startsWith("0b")) {
            n = Long.parseLong(n.substring(2), 2) + "";
        } else if (n.startsWith("0") && n.length() > 1) {
            n = Long.parseLong(n.substring(1), 8) + "";
        }
        tokens.add(new Token(TokenType.NUMBER, n, line, col));
    }

    private void parseString() {
        int line = code.substring(0, index + 1).split("\n").length,
                col = code.substring(0, index + 1).split("\n")[line - 1].length();
        String s = "";
        index++;
        while (code.charAt(index) != '"') {
            if (code.charAt(index) == '\\') {
                index++;
                switch (code.charAt(index)) {
                    case 't':
                        s += '\t';
                        break;
                    case 'n':
                        s += '\n';
                        break;
                    case 'b':
                        s += '\b';
                        break;
                    case 'f':
                        s += '\f';
                        break;
                    case 'r':
                        s += '\r';
                        break;
                    case '\\':
                        s += '\\';
                        break;
                    case '\'':
                        s += '\'';
                        break;
                    case '\"':
                        s += '\"';
                        break;
                }
            } else {
                s += code.charAt(index);
            }
            index++;
        }
        index++;
        tokens.add(new Token(TokenType.STRING, s, line, col));
    }

    private void parseChar() {
        int line = code.substring(0, index + 1).split("\n").length,
                col = code.substring(0, index + 1).split("\n")[line - 1].length();
        String s = "";
        index++;
        while (code.charAt(index) != '\'') {
            if (code.charAt(index) == '\\') {
                index++;
                switch (code.charAt(index)) {
                    case 't':
                        s += '\t';
                        break;
                    case 'n':
                        s += '\n';
                        break;
                    case 'b':
                        s += '\b';
                        break;
                    case 'f':
                        s += '\f';
                        break;
                    case 'r':
                        s += '\r';
                        break;
                    case '\\':
                        s += '\\';
                        break;
                    case '\'':
                        s += '\'';
                        break;
                    case '\"':
                        s += '\"';
                        break;
                }
            } else {
                s += code.charAt(index);
            }
            index++;
        }
        index++;
        tokens.add(new Token(TokenType.CHAR, s, line, col));
    }

    private boolean isOperator() {
        int line = code.substring(0, index + 1).split("\n").length,
                col = code.substring(0, index + 1).split("\n")[line - 1].length();
        for (String op : Operators.operators) {
            if (code.substring(index).startsWith(op)) {
                tokens.add(new Token(TokenType.OPERATOR, op, line, col));
                index += op.length();
                return true;
            }
        }
        return false;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<TokenizingError> getErrors() {
        return errors;
    }

    public List<Token> getTokens() {
        return tokens;
    }
}
