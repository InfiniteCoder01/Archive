package net.infinitecoder.jplugin;

public class ParseError implements JPluginError {

    private final int line;
    private final Exception exception;

    public ParseError(int line, Exception exception) {
        this.line = line;
        this.exception = exception;
    }

    public int getLine() {
        return line;
    }

    public Exception getException() {
        return exception;
    }

    @Override
    public String toString() {
        return "Parsing error on line " + line + ": " + exception.getMessage();
    }
}
