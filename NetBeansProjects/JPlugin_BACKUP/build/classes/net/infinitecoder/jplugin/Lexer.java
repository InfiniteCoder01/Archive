package net.infinitecoder.jplugin;

import java.util.ArrayList;
import java.util.List;

class Lexer {

    private int line = 0;
    private int index = 0;
    public final String code;
    public List<Token> tokens;

    public Lexer(String code) {
        this.code = code + "\n ";
    }

    List<Token> tokenize() {
        tokens = new ArrayList<>();
        while (index < code.length()) {
            line = code.substring(0, index).split("\r\n|\r|\n").length;
            if (code.substring(index).startsWith("//")) {
                while (code.charAt(index) != '\n') {
                    index++;
                    if (code.charAt(index) == '\\') {
                        while (code.charAt(index) != '\n') {
                            index++;
                        }
                    }
                }
            }
            if (code.substring(index).startsWith("/*")) {
                index += 2;
                while (!code.substring(index).startsWith("*/")) {
                    index++;
                }
                index += 2;
            }
            if (Character.isDigit(code.charAt(index))) {
                parseNumber();
            } else if (code.charAt(index) == '"') {
                parseString();
            } else if (code.charAt(index) == '\'') {
                parseChar();
            } else if (isOperator()) {
            } else if (Character.isLetter(code.charAt(index)) || code.charAt(index) == '_') {
                //keywords
                String keyword = "";
                while (Character.isLetterOrDigit(code.charAt(index)) || code.charAt(index) == '_') {
                    keyword += code.charAt(index);
                    index++;
                }
                tokens.add(new Token(TokenType.WORD, keyword, line));
            } else {
                index++;//whitespaces
            }
        }
        tokens.add(new Token(TokenType.EOF, "EOF", line));
        return tokens;
    }

    private void parseNumber() {
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
        tokens.add(new Token(TokenType.NUMBER, n, line));
    }

    private void parseString() {
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
        tokens.add(new Token(TokenType.STRING, s, line));
    }

    private void parseChar() {
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
        tokens.add(new Token(TokenType.CHAR, s, line));
    }

    private boolean isOperator() {
        for (String op : Operators.operators) {
            if (code.substring(index).startsWith(op)) {
                tokens.add(new Token(TokenType.OPERATOR, op, line));
                index += op.length();
                return true;
            }
        }
        return false;
    }
}