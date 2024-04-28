package ast.expression;

import ast.Type;

import java.util.Objects;

public class CastExpression implements Expression {
    public Type type;
    public Expression expression;

    public CastExpression(Type type, Expression expression) {
        this.type = type;
        this.expression = expression;
        if (expression.getType() == null) {
            throw new RuntimeException(String.format("Can't cast @null '%s' to type '%s'!", expression, type));
        }
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        if (expression.getType().equals(type)) return expression;
        if (expression instanceof ConstantExpression)
            return new ConstantExpression(((ConstantExpression) expression).constant, type); // TODO: might not work
        return this;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("@%s %s", type, expression);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        CastExpression that = (CastExpression) other;

        if (!Objects.equals(type, that.type)) return false;
        return Objects.equals(expression, that.expression);
    }
}
