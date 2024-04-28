package net.infinitecoder.jplugin;

public class ParsingError implements JPluginError {
    private final Token token;
    private final String what;

    public ParsingError(Token token, String what) {
        this.token = token;
        this.what = what;
    }

    @Override
    public String toString() {
        return what + " at line " + token.getLine() + ", column " + token.getCol() + "!";
    }
}
