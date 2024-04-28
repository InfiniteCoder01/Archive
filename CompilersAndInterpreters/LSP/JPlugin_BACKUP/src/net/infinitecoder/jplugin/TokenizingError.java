package net.infinitecoder.jplugin;

public class TokenizingError implements JPluginError {
    public final int line;
    public final int column;
    public final String what;

    public TokenizingError(int line, int column, String what) {
        this.line = line;
        this.column = column;
        this.what = what;
    }
    
    public TokenizingError(String text, int pos, String what) {
        this.line = text.substring(0, pos + 1).split("\n").length;
        this.column = text.substring(0, pos + 1).split("\n")[line - 1].length();
        this.what = what;
    }

    @Override
    public String toString() {
        return what + " at line " + line + ", column " + column + "!";
    }
}
