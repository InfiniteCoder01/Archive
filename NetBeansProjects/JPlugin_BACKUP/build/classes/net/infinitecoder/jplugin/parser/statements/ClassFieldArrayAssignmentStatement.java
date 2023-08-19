package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.ArrayValue;
import net.infinitecoder.jplugin.parser.values.IntValue;
import net.infinitecoder.jplugin.parser.values.ObjectValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class ClassFieldArrayAssignmentStatement implements Statement {

    private final List<Expression> indexes;
    private final String objectname, name;
    public final Expression ex;

    public ClassFieldArrayAssignmentStatement(List<Expression> indexes, String objectname, String name, Expression ex) {
        this.indexes = indexes;
        this.objectname = objectname;
        this.name = name;
        this.ex = ex;
    }

    @Override
    public void execute() {
        Value value = ((ObjectValue) activeVariables.get(objectname)).body.localVarsData.get(name);
        for (int i = 0; i < indexes.size() - 1; i++) {
            value = value.operator("[]", indexes.get(i).eval());
        }
        int index = ((IntValue) indexes.get(indexes.size() - 1).eval().cast("int")).value;
        ((ArrayValue) value).values[index] = ex.eval().cast(activeVariables.typeof(((ArrayValue) value).values[index]));
    }

    @Override
    public String toString() {
        return objectname + "." + name + indexes + " = " + ex;
    }

    @Override
    public Statement clone() {
        List<Expression> newIndexes = new ArrayList<>();
        for (Expression index : indexes) {
            newIndexes.add(index.clone());
        }
        return new ClassFieldArrayAssignmentStatement(newIndexes, objectname.substring(0), name.substring(0), ex.clone());
    }
}
