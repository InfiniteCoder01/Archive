package ast.expression;

import ast.Type;

import java.util.Objects;

public class ReturnExpression implements Expression {
    public BlockExpression returnBlock;
    public Expression expression;

    public ReturnExpression(BlockExpression returnBlock, Expression expression) {
        this.returnBlock = returnBlock;
        this.expression = expression;
        returnBlock.returns.add(this);
    }

    @Override
    public Expression optimize() {
        expression = expression.optimize();
        return this;
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public String toString() {
        return String.format("return %s", expression);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        ReturnExpression that = (ReturnExpression) other;

        if (!Objects.equals(returnBlock, that.returnBlock)) return false;
        return Objects.equals(expression, that.expression);
    }
}
