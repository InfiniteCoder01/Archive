package net.infinitecoder.jplugin.netbeans.editor;

public class CodeBeautifier_backup_my {

    public static String beautify(String input) {
        return new CodeBeautifier_backup_my(input).beautify();
    }

    public final int INDENT = 4;
    
    private final String input;
    private final int length;

    private final StringBuilder beautifiedCode;

    private int pos;
    private int indentLevel;

    public CodeBeautifier_backup_my(String input) {
        this.input = input;
        length = input.length();
        beautifiedCode = new StringBuilder();
        indentLevel = 0;
    }

    public String beautify() {
        while (pos < length) {
            final char current = peek(0);
            if (Character.isLetterOrDigit(current)) {
                skipWordOrNumber();
                if ("([>.".indexOf(peek(0)) == -1) {
                    beautifiedCode.append(' ');
                }
            } else if ("[(<.".indexOf(current) != -1) {
                beautifiedCode.append(current);
            } else if (current == '{') {
                beautifiedCode.append(current);
                indentLevel += INDENT;
                newLineStrict();
            } else if (current == '}') {
                indentLevel -= INDENT;
                newLineStrict();
                beautifiedCode.append(current);
                skipSpaces();
                if(!input.substring(pos).startsWith("else") && !input.substring(pos).startsWith(";")) {
                    newLineStrict();
                }
            } else if (!Character.isLetterOrDigit(current) && " \n\r\t".indexOf(current) == -1) {
                skipOperator();
                beautifiedCode.append(' ');
            } else if (current == '\'') {
                skipChar();
                beautifiedCode.append(' ');
            } else if (current == '\"') {
                skipString();
                beautifiedCode.append(' ');
            } else if(" \n\r\t".indexOf(current) != -1) {
                System.gc();
                beautifiedCode.append(current);
            }
        }
        return beautifiedCode.toString();
    }

    private void skipString() {
        beautifiedCode.append(peek(0));
        pos++;
        while (peek(0) != '\"') {
            beautifiedCode.append(peek(0));
            pos++;
            if (peek(-1) == '\\') {
                beautifiedCode.append(peek(0));
                pos++;
            }
        }
        beautifiedCode.append(peek(0));
        pos++;
    }

    private void skipChar() {
        beautifiedCode.append(peek(0));
        pos++;
        while (peek(0) != '\'') {
            beautifiedCode.append(peek(0));
            pos++;
            if (peek(-1) == '\\') {
                beautifiedCode.append(peek(0));
                pos++;
            }
        }
        beautifiedCode.append(peek(0));
        pos++;
    }

    private void skipWordOrNumber() {
        while (Character.isLetterOrDigit(peek(0))) {
            beautifiedCode.append(peek(0));
            pos++;
        }
    }

    private void skipOperator() {
        while (!Character.isLetterOrDigit(peek(0)) && " \n\r\t".indexOf(peek(0)) == -1) {
            beautifiedCode.append(peek(0));
            pos++;
        }
    }

    private void newLineStrict() {
        beautifiedCode.append(System.lineSeparator());
        indent();
    }

    private void indent() {
        indent(indentLevel);
    }

    private void indent(int count) {
        for (int i = 0; i < count; i++) {
            beautifiedCode.append(' ');
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
        beautifiedCode.append(input.substring(pos, position));
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
        while(" \n\r\t".indexOf(peek(0)) != -1) {
            pos++;
        }
    }
}
