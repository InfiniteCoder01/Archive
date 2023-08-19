package net.infinitecoder.jplugin.parser.expressions;

import net.infinitecoder.jplugin.parser.values.Value;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

public class VariableExpression implements Expression {
    public final String name;

    public VariableExpression(String name) {
        this.name = name;
    }
    
    @Override
    public Value eval() {
        return activeVariables.get(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression clone() {
        return new VariableExpression(name.substring(0));
    }
}
