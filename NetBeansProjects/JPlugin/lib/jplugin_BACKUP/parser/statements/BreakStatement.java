package net.infinitecoder.jplugin.parser.statements;

@SuppressWarnings("serial")
public class BreakStatement extends RuntimeException implements Statement {

    public BreakStatement() {
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
        return new BreakStatement();
    }
}
