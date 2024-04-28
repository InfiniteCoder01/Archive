package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.Value;

@SuppressWarnings("serial")
public class ReturnStatement extends RuntimeException implements Statement {

    public final Expression ex;
    public Value val;

    public ReturnStatement(Expression ex) {
        this.ex = ex;
    }

    public ReturnStatement(Expression ex, Value val) {
        this.ex = ex;
        this.val = val;
    }

    @Override
    public void execute() {
        val = ex.eval();
        throw this;
    }

    @Override
    public String toString() {
        return "return " + ex;
    }

    @Override
    public Statement clone() {
        if (val == null) {
            return new ReturnStatement(ex.clone(), null);
        }
        return new ReturnStatement(ex.clone(), val.clone());
    }
}
