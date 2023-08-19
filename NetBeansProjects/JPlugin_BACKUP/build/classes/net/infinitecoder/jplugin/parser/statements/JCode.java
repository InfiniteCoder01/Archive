package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.parser.expressions.ValueExpression;
import net.infinitecoder.jplugin.parser.values.Value;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

@SuppressWarnings("serial")
public abstract class JCode implements Statement {

    public final String[] args;

    public JCode(String args) {
        List<String> arglist = new ArrayList<>();
        if (!args.isEmpty()) {
            for (int i = 0; i < args.split(" ").length; i += 2) {
                arglist.add(args.split(" ")[i] + " " + args.split(" ")[i + 1]);

            }
        }
        this.args = arglist.toArray(new String[0]);
    }

    @Override
    public void execute() {
        List<Value> finalArgs = new ArrayList<>();
        for (String arg : args) {
            finalArgs.add(activeVariables.get(arg.split(" ")[1]));
        }
        new ReturnStatement(new ValueExpression(execute(finalArgs.toArray(new Value[0])))).execute();
    }

    public abstract Value execute(Value... args);

    @Override
    public String toString() {
        return "[Java native code]";
    }

    @Override
    public Statement clone() {
        return this;
    }
}
