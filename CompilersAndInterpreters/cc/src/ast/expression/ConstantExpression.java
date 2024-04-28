package ast.expression;

import ast.Type;

import java.util.Objects;

public class ConstantExpression implements Expression {
    public String constant;
    public Type type;

    public ConstantExpression(String constant, Type type) {
        this.constant = constant;
        this.type = type;
    }

    @Override
    public Expression optimize() {
        return this;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return String.format("@%s %s", type, constant);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ConstantExpression that = (ConstantExpression) other;

        if (!Objects.equals(constant, that.constant)) return false;
        return Objects.equals(type, that.type);
    }
}
