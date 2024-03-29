package net.infinitecoder.jplugin.parser.expressions;

import net.infinitecoder.jplugin.parser.values.Value;

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

    @Override
    public Expression clone() {
        return new ValueExpression(value.clone());
    }
}
