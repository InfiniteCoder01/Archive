package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.ArrayValue;
import net.infinitecoder.jplugin.parser.values.IntValue;
import net.infinitecoder.jplugin.parser.values.Value;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

public class ArrayAssignmentStatement implements Statement {

    private final List<Expression> indexes;
    private final String name;
    public final Expression ex;

    public ArrayAssignmentStatement(List<Expression> indexes, String name, Expression ex) {
        this.indexes = indexes;
        this.name = name;
        this.ex = ex;
    }

    @Override
    public void execute() {
        Value value = activeVariables.get(name);
        for (int i = 0; i < indexes.size() - 1; i++) {
            value = value.operator("[]", indexes.get(i).eval());
        }//FIXME: array last index as operator, also in class
        int index = ((IntValue)indexes.get(indexes.size() - 1).eval().cast("int")).value;
        ((ArrayValue)value).values[index] = ex.eval().cast(activeVariables.typeof(((ArrayValue)value).values[index]));
    }

    @Override
    public String toString() {
        return name + indexes + " = " + ex;
    }

    @Override
    public Statement clone() {
        List<Expression> newIndexes = new ArrayList<>();
        for (Expression index : indexes) {
            newIndexes.add(index.clone());
        }
        return new ArrayAssignmentStatement(indexes, name.substring(0), ex.clone());
    }
}
