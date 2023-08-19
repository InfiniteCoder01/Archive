package net.infinitecoder.jplugin.expressions;

import net.infinitecoder.jplugin.values.Value;

public class BinaryExpression implements Expression {
    private final Expression a, b;
    private final String operator;

    public BinaryExpression(Expression a, Expression b, String operator) {
        this.a = a;
        this.b = b;
        this.operator = operator;
    }
    
    @Override
    public Value eval() {
        return a.eval().operator(operator, b.eval());
    }

    @Override
    public String toString() {
        return "(" + a + " " + operator + " " + b + ")";
    }
}
