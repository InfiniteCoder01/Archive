package net.infinitecoder.jplugin.parser.statements;

@SuppressWarnings("serial")
public class ContinueStatement extends RuntimeException implements Statement {

    public ContinueStatement() {
    }

    @Override
    public void execute() {
        throw this;
    }

    @Override
    public String toString() {
        return "break";
    }

    @Override
    public Statement clone() {
        return new ContinueStatement();
    }
}
