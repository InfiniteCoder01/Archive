package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeClasses;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.expressions.ClassConstructorExpression;
import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.expressions.ValueExpression;

public class ObjectCreationStatement implements Statement {

    public final String type1;
    public final List<String> names;
    public final List<Expression> values;

    public ObjectCreationStatement(String type, List<String> names, List<Expression> values) {
        this.type1 = type;
        this.names = names;
        this.values = values;
    }

    @Override
    public void execute() {
        String type = type1;
        if (activeVariables.defTypes.containsKey(type)) {
            type = activeVariables.defTypes.get(type);
        }
        if (activeClasses.classes.containsKey(type) || type.contains("::")) {
            for (int i = 0; i < values.size(); i++) {
                if(values.get(i) instanceof ValueExpression) {
                    values.set(i, new ClassConstructorExpression(type, new ArrayList<>()));
                }
            }
        }
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            Expression value = values.get(i);
            activeVariables.put(name, type, value.eval());
        }
    }

    @Override
    public String toString() {
        return type1 + " " + names + " = " + values;
    }

    @Override
    public Statement clone() {
        List<String> newNames = new ArrayList<>();
        for (String name : names) {
            newNames.add(name.substring(0));
        }
        List<Expression> newValues = new ArrayList<>();
        for (Expression value : values) {
            newValues.add(value.clone());
        }
        return new ObjectCreationStatement(type1.substring(0), newNames, newValues);
    }
}
