package net.infinitecoder.jplugin.parser.statements;

public interface Statement {
    public void execute();
    public Statement clone();
}
