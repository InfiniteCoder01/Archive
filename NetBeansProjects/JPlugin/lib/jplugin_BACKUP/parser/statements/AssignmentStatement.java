package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

public class AssignmentStatement implements Statement {

    private final String name;
    public final Expression ex;

    public AssignmentStatement(String name, Expression ex) {
        this.name = name;
        this.ex = ex;
    }

    @Override
    public void execute() {
        activeVariables.put(name, ex.eval());
    }

    @Override
    public String toString() {
        return name + " = " + ex;
    }

    @Override
    public Statement clone() {
        return new AssignmentStatement(name.substring(0), ex.clone());
    }
}
