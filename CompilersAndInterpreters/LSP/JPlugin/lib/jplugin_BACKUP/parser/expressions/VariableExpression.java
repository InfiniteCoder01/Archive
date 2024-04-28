package net.infinitecoder.jplugin.parser.expressions;

import javafx.util.Pair;
import net.infinitecoder.jplugin.JPlugin;
import net.infinitecoder.jplugin.parser.values.IntValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class VariableExpression implements Expression {

    public final Pair<String, Long> var;

    public VariableExpression(Pair<String, Long> var) {
        this.var = var;
    }

    @Override
    public Value eval() {
        return get();
    }

    @Override
    public String toString() {
        return name;
    }

    Value get() {
        return new IntValue(JPlugin.activeMemory.get(var.getValue()));
    }

    void set(Value v) {

    }
}
