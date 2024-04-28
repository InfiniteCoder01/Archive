package net.infinitecoder.jplugin.parser.statements;

import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.BoolValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class IfElseStatement implements Statement {

    public final Expression ex;
    public final Statement ifStatement, elseStatement;

    public IfElseStatement(Expression ex, Statement ifStatement, Statement elseStatement) {
        this.ex = ex;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
    }

    public IfElseStatement(Expression ex, Statement ifStatement) {
        this.ex = ex;
        this.ifStatement = ifStatement;
        this.elseStatement = null;
    }

    @Override
    public void execute() {
        BoolValue condition = (BoolValue) ex.eval().cast("bool");
        if(condition.value != 0) {
            ifStatement.execute();
        } else if (elseStatement != null) {
            elseStatement.execute();
        }
    }

    @Override
    public String toString() {
        return "if(" + ex + ") " + ifStatement + (elseStatement == null ? "" : (" else " + elseStatement));
    }

    @Override
    public Statement clone() {
        if (elseStatement == null) {
            return new IfElseStatement(ex.clone(), ifStatement.clone());
        } else {
            return new IfElseStatement(ex.clone(), ifStatement.clone(), elseStatement.clone());
        }
    }
}
