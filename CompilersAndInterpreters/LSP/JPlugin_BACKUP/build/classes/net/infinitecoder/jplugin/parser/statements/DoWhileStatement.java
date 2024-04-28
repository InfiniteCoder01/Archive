package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.BoolValue;

public class DoWhileStatement implements Statement {

    public final Expression ex;
    public final Statement statement;

    public DoWhileStatement(Expression ex, Statement statement) {
        this.ex = ex;
        this.statement = statement;
    }

    @Override
    public void execute() {
        do {
            statement.execute();
        } while (((BoolValue) ex.eval().cast("bool")).value != 0);
    }

    @Override
    public String toString() {
        return "do " + statement + " while(" + ex + ");";
    }

    @Override
    public Statement clone() {
        return new DoWhileStatement(ex.clone(), statement.clone());
    }
}
