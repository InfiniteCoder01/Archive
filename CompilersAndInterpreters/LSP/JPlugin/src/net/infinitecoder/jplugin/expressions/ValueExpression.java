package net.infinitecoder.jplugin.expressions;

import net.infinitecoder.jplugin.values.Value;

public class ValueExpression implements Expression {
    private final Value value;

    public ValueExpression(Value value) {
        this.value = value;
    }
    
    @Override
    public Value eval() {
        return value;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
