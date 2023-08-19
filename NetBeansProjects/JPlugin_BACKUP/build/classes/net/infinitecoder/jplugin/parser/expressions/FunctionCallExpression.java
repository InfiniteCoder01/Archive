package net.infinitecoder.jplugin.parser.expressions;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.parser.statements.ReturnStatement;
import net.infinitecoder.jplugin.parser.values.Value;
import static net.infinitecoder.jplugin.JPlugin.activeFunctions;
import net.infinitecoder.jplugin.parser.statements.Statement;

public class FunctionCallExpression implements Expression {

    private final String name;
    private final List<Expression> args;

    public FunctionCallExpression(String name, List<Expression> args) {
        this.name = name;
        this.args = args;
    }

    @Override
    public Value eval() {
        SimpleEntry<String, Statement> function = activeFunctions.get(name, args);
        try {
            function.getValue().execute();
        } catch (ReturnStatement rs) {
            return rs.val.cast(function.getKey());
        }
        return null;
    }

    @Override
    public String toString() {
        return name + args + "";
    }

    @Override
    public Expression clone() {
        List<Expression> newArgs = new ArrayList<>();
        for (Expression arg : args) {
            newArgs.add(arg.clone());
        }
        return new FunctionCallExpression(name.substring(0), newArgs);
    }
}
