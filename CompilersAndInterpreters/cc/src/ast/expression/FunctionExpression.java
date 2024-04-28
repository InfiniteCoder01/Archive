package ast.expression;

import ast.Type;

import java.util.Objects;

public class FunctionExpression implements Expression {
    public Expression body;

    public FunctionExpression(Expression body) {
        this.body = body;
    }

    @Override
    public Expression optimize() {
        body = body.optimize();
        return this;
    }

    @Override
    public Type getType() {
        return new Type("fn");
    }

    @Override
    public String toString() {
        return String.format("fn () %s", body);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        FunctionExpression that = (FunctionExpression) other;

        return Objects.equals(body, that.body);
    }
}
