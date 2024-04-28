package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.expressions.ValueExpression;

public class TryCatchStatement implements Statement {

    public final Statement tryStatement;
    public final List<Statement> catches;
    public final List<ObjectCreationStatement> catchValues;

    public TryCatchStatement(Statement tryStatement, List<Statement> catches, List<ObjectCreationStatement> catchValues) {
        this.tryStatement = tryStatement;
        this.catches = catches;
        this.catchValues = catchValues;
    }
    
    public TryCatchStatement(Statement tryStatement) {
        this.tryStatement = tryStatement;
        this.catches = new ArrayList<>();
        this.catchValues = new ArrayList<>();
    }

    @Override
    public void execute() {
        try {
            tryStatement.execute();
        } catch (ThrowException te) {
            for (int i = 0; i < catches.size(); i++) {
                if (activeVariables.typeof(catchValues.get(i).type1).equals(activeVariables.typeof(te.val))) {
                    BlockStatement bs = new BlockStatement();
                    ObjectCreationStatement ocs = catchValues.get(i);
                    ocs.values.set(0,new ValueExpression(te.val));
                    bs.add(ocs);
                    bs.add(catches.get(i));
                    bs.execute();
                    return;
                }
            }
            throw te;
        }
    }

    @Override
    public String toString() {
        return "try " + tryStatement + "...";
    }

    public void add(ObjectCreationStatement ocs, Statement st) {
        catchValues.add(ocs);
        catches.add(st);
    }

    @Override
    public Statement clone() {
        List<Statement> newCatches = new ArrayList<>();
        for (Statement st : catches) {
            newCatches.add(st.clone());
        }
        List<ObjectCreationStatement> newCatchValues = new ArrayList<>();
        for (Statement value : catchValues) {
            newCatchValues.add((ObjectCreationStatement) value.clone());
        }
        return new TryCatchStatement(tryStatement.clone(), newCatches, newCatchValues);
    }
}
