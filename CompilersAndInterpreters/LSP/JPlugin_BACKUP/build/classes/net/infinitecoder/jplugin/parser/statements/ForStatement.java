package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.BoolValue;

public class ForStatement implements Statement {

    public final Expression ex;
    public final Statement statement;
    public final Statement tick;

    public ForStatement(Expression ex, Statement statement, Statement tick) {
        this.ex = ex;
        this.statement = statement;
        this.tick = tick;
    }

    @Override
    public void execute() {
        while (((BoolValue) ex.eval().cast("bool")).value != 0) {
            try {
                statement.execute();
            } catch (BreakStatement bs) {
                return;
            } catch (ContinueStatement cs) {
            }
            tick.execute();
        }
    }

    @Override
    public String toString() {
        return "while(" + ex + ") " + statement;
    }

    @Override
    public Statement clone() {
        return new ForStatement(ex.clone(), statement.clone(), tick.clone());
    }
}
