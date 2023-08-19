package net.infinitecoder.jplugin.expressions;

import net.infinitecoder.jplugin.values.Value;

public class UnaryExpression implements Expression {
    private final Expression expression;
    private final String operator;

    public UnaryExpression(Expression expression, String operator) {
        this.expression = expression;
        this.operator = operator;
    }
    
    @Override
    public Value eval() {
        return expression.eval().operator(operator, null);
    }

    @Override
    public String toString() {
        return "(" + operator + expression + ")";
    }
}
