package ast.expression;

import ast.Type;

import java.util.Objects;

public class BinaryExpression implements Expression {
    public String operator;
    public Expression lhs, rhs;
    public Type type;

    public BinaryExpression(String operator, Expression lhs, Expression rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
        this.type = Type.max(lhs.getType(), rhs.getType());
    }

    @Override
    public Expression optimize() {
        lhs = lhs.optimize();
        rhs = rhs.optimize();
        if (lhs instanceof ConstantExpression && rhs instanceof ConstantExpression) {
            // TODO: evaluate
        }
        return this;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("@%s (%s %s %s)", type, lhs, operator, rhs);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        BinaryExpression that = (BinaryExpression) other;

        if (!Objects.equals(operator, that.operator)) return false;
        if (!Objects.equals(lhs, that.lhs)) return false;
        if (!Objects.equals(rhs, that.rhs)) return false;
        return Objects.equals(type, that.type);
    }
}
