package net.infinitecoder.jplugin.netbeans.editor;

public class CodeBeautifier {

    public static String beautify(String input) {
        return new CodeBeautifier(input).beautify();
    }

    public final int INDENT = 4;

    private final String input;
    private final int length;

    private String beautifiedCode;

    private int pos;
    private int indentLevel;

    public CodeBeautifier(String input) {
        this.input = input;
        length = input.length();
        indentLevel = 0;
    }

    public String beautify() {
        beautifiedCode = "";
        int brackets = 0;
        while (pos < length) {
            final char current = peek(0);
            if (Character.isLetterOrDigit(current)) {
                skipWordOrNumber();
                skipSpaces();
                if ("()[].,;".indexOf(peek(0)) == -1 && !input.substring(pos).startsWith("++") && !input.substring(pos).startsWith("--")) {
                    beautifiedCode += ' ';
                }
            } else if ("[.".indexOf(current) != -1) {
                pos++;
                beautifiedCode += current;
            } else if (")".indexOf(current) != -1 && "{".indexOf(peek(1)) != -1) {
                pos++;
                brackets--;
                beautifiedCode += current;
                beautifiedCode += ' ';
            } else if (current == '(') {
                brackets++;
                skipOperator();
            } else if (current == ')') {
                brackets--;
                skipOperator();
                if (";".indexOf(peek(0)) == -1) {
                    beautifiedCode += ' ';
                }
            } else if (current == '{') {
                pos++;
                if (!beautifiedCode.endsWith("(")) {
                    beautifiedCode += current;
                    indentLevel += INDENT;
                    newLineStrict();
                } else {
                    beautifiedCode += current;
                }
            } else if (current == '}') {
                pos++;
                indentLevel -= INDENT;
                newLineStrict();
                beautifiedCode += current;
                skipSpaces();
                if (!input.substring(pos).startsWith("else") && ";)".indexOf(peek(0)) == -1) {
                    newLineStrict();
                }
            } else if (input.substring(pos).startsWith("++")) {
                skipOperator();
            } else if (input.substring(pos).startsWith("--")) {
                skipOperator();
            } else if (current == ';') {
                pos++;
                beautifiedCode += current;
                beautifiedCode += ' ';
                if (brackets == 0) {
                    newLineStrict();
                }
            } else if (!Character.isLetterOrDigit(current) && " \n\r\t\0".indexOf(current) == -1) {
                skipOperator();
                beautifiedCode += ' ';
            } else if (current == '\'') {
                skipChar();
                beautifiedCode += ' ';
            } else if (current == '\"') {
                skipString();
                beautifiedCode += ' ';
            } else if (" \n\r\t".indexOf(current) == -1) {
                beautifiedCode += current;
                pos++;
            } else {
                pos++;
            }
        }
        return beautifiedCode;
    }

    private void skipString() {
        beautifiedCode += peek(0);
        pos++;
        while (peek(0) != '\"') {
            beautifiedCode += peek(0);
            pos++;
            if (peek(-1) == '\\') {
                beautifiedCode += peek(0);
                pos++;
            }
        }
        beautifiedCode += peek(0);
        pos++;
    }

    private void skipChar() {
        beautifiedCode += peek(0);
        pos++;
        while (peek(0) != '\'') {
            beautifiedCode += peek(0);
            pos++;
            if (peek(-1) == '\\') {
                beautifiedCode += peek(0);
                pos++;
            }
        }
        beautifiedCode += peek(0);
        pos++;
    }

    private void skipWordOrNumber() {
        while (Character.isLetterOrDigit(peek(0))) {
            beautifiedCode += peek(0);
            pos++;
        }
    }

    private void skipOperator() {
        while (!Character.isLetterOrDigit(peek(0)) && "{}; \n\r\t\0".indexOf(peek(0)) == -1) {
            beautifiedCode += peek(0);
            pos++;
        }
    }

    private void newLineStrict() {
        beautifiedCode += System.lineSeparator();
        indent();
    }

    private void indent() {
        indent(indentLevel);
    }

    private void indent(int count) {
        for (int i = 0; i < count; i++) {
            beautifiedCode += ' ';
        }
    }

    private void skipTo(String text) {
        int index = input.indexOf(text, pos);
        if (index == -1) {
            index = length - 1;
        } else {
            index += text.length();
        }
        skipTo(index);
    }

    private void skipTo(int position) {
        beautifiedCode += input.substring(pos, position);
        pos += (position - pos);
    }

    private char last() {
        return beautifiedCode.charAt(beautifiedCode.length() - 1);
    }

    private char next() {
        pos++;
        return peek(0);
    }

    private char peek(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= length) return '\0';
        return input.charAt(position);
    }

    private void skipSpaces() {
        while (" \n\r\t".indexOf(peek(0)) != -1 && pos < input.length()) {
            pos++;
        }
    }
}
