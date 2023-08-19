package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;

public class ExpressionStatement implements Statement {

    public final Expression ex;

    public ExpressionStatement(Expression ex) {
        this.ex = ex;
    }

    @Override
    public void execute() {
        ex.eval();
    }

    @Override
    public String toString() {
        return ex + "";
    }

    @Override
    public Statement clone() {
        return new ExpressionStatement(ex.clone());
    }
}
