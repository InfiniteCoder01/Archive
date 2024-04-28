package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.BoolValue;

public class WhileStatement implements Statement {

    public final Expression ex;
    public final Statement statement;

    public WhileStatement(Expression ex, Statement statement) {
        this.ex = ex;
        this.statement = statement;
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
        }
    }

    @Override
    public String toString() {
        return "while(" + ex + ") " + statement;
    }

    @Override
    public Statement clone() {
        return new WhileStatement(ex.clone(), statement.clone());
    }
}
