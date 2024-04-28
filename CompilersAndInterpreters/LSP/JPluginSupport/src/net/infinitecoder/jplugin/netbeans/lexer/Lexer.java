package net.infinitecoder.jplugin.netbeans.lexer;

import java.util.Arrays;
import java.util.List;
import net.infinitecoder.jplugin.Operators;

public class Lexer {

    public static final List<String> keywords = Arrays.asList(new String[]{
        "auto", "bool", "break", "case", "catch", "char", "class", "continue",
        "default", "do", "double", "else", "enum", "false", "float", "for", "if",
        "int", "long", "new", "operator", "return", "sizeof", "struct", "switch",
        "throw", "true", "try", "typedef", "void", "while"
    });
    public final String code;
    public int index = 0;

    public Lexer(String code) {
        this.code = code;
    }

    public Token parseToken() {
        Token t;
        if (Character.isDigit(code.charAt(index))) {
            return tokeizeNumber();
        } else if (Character.isLetter(code.charAt(index))) {
            return tokeizeWord();
        } else if (code.charAt(index) == '\'') {
            return tokeizeChar();
        } else if (code.charAt(index) == '\"') {
            return tokeizeString();
        } else if (code.substring(index).startsWith("//")) {
            return tokenizeComment();
        } else if (code.substring(index).startsWith("/*")) {
            return tokenizeMultilineComment();
        } else if ((t = isOperator()) != null) {
            return t;
        }
        index++;
        return new Token(TokenType.WHITESPACE, " ", index - 1);
    }

    private Token tokeizeNumber() {
        String str = "";
        int pos = index;
        while (Character.isLetterOrDigit(code.charAt(index))) {
            str += code.charAt(index);
            index++;
        }
        return new Token(TokenType.NUMBER, str, pos);
    }

    private Token tokeizeWord() {
        String str = "";
        int pos = index;
        while (index < code.length() && Character.isLetterOrDigit(code.charAt(index))) {
            str += code.charAt(index);
            index++;
        }
        return new Token(keywords.contains(str) ? TokenType.KEYWORD : TokenType.WORD, str, pos);
    }

    private Token tokeizeChar() {
        String str = "\'";
        int pos = index;
        index++;
        while (code.charAt(index) != '\'' && (index < (code.length() - 1))) {
            if (code.charAt(index) == '\\') {
                index++;
                str += "\\";
            }
            str += code.charAt(index);
            index++;
        }
        str += "\'";
        index++;
        return new Token(TokenType.CHAR, str, pos);
    }

    private Token tokeizeString() {
        String str = "\"";
        int pos = index;
        index++;
        while (code.charAt(index) != '\"' && (index < (code.length() - 1))) {
            if (code.charAt(index) == '\\') {
                index++;
                str += "\\";
            }
            str += code.charAt(index);
            index++;
        }
        str += "\"";
        index++;
        return new Token(TokenType.STRING, str, pos);
    }

    private Token isOperator() {
        for (String op : Operators.operators) {
            if (code.substring(index).startsWith(op)) {
                Token t = new Token(TokenType.OPERATOR, op, index);
                index += op.length();
                return t;
            }
        }
        return null;
    }

    private Token tokenizeComment() {
        String str = "";
        int pos = index;
        while (index < code.length() && code.charAt(index) != '\n') {
            str += code.charAt(index);
            index++;
        }
        return new Token(TokenType.COMMENT, str, pos);
    }

    private Token tokenizeMultilineComment() {
        int pos = index;
        index += 2;
        while (index < code.length() && !code.substring(index).startsWith("*/")) {
            index++;
        }
        if (index < code.length()) {
            index += 3;
        }
        return new Token(TokenType.COMMENT, code.substring(pos, index), pos);
    }
}
