package net.infinitecoder.jplugin.parser.expressions;

import net.infinitecoder.jplugin.parser.values.Value;

public class PostfixExpression implements Expression {
    private final Expression expression;
    private final String operator;

    public PostfixExpression(Expression expression, String operator) {
        this.expression = expression;
        this.operator = operator;
    }
    
    @Override
    public Value eval() {
        return expression.eval().operator(" " + operator, null);
    }

    @Override
    public String toString() {
        return "(" + expression + operator + ")";
    }

    @Override
    public Expression clone() {
        return new PostfixExpression(expression.clone(), operator.substring(0));
    }
}
