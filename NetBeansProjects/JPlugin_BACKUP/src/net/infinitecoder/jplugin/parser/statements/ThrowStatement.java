package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.Value;

public class ThrowStatement implements Statement {

    public final Expression ex;
    public Value val;

    public ThrowStatement(Expression ex) {
        this.ex = ex;
    }

    public ThrowStatement(Expression ex, Value val) {
        this.ex = ex;
        this.val = val;
    }

    @Override
    public void execute() {
        val = ex.eval();
        throw new ThrowException(val);
    }

    @Override
    public String toString() {
        return "throw " + ex;
    }

    @Override
    public Statement clone() {
        if (val == null) {
            return new ThrowStatement(ex.clone(), null);
        }
        return new ThrowStatement(ex.clone(), val.clone());
    }
}
