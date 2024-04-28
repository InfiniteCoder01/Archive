package ast.expression;

import ast.Type;

import java.util.Objects;

public class TernaryExpression implements Expression {
    public Expression condition, trueBranch, falseBranch;
    public Type type;

    public TernaryExpression(Expression condition, Expression trueBranch, Expression falseBranch) {
        this.condition = condition;
        this.trueBranch = trueBranch;
        this.falseBranch = falseBranch;
        type = Type.max(this.trueBranch.getType(), this.falseBranch.getType());
    }

    @Override
    public Expression optimize() {
        condition = condition.optimize();
        trueBranch = trueBranch.optimize();
        falseBranch = falseBranch.optimize();
        if (trueBranch.equals(falseBranch)) return trueBranch;

        if(condition instanceof ConstantExpression) {
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
        return String.format("@%s (%s ? %s : %s)", type, condition, trueBranch, falseBranch);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        TernaryExpression that = (TernaryExpression) other;

        if (!Objects.equals(condition, that.condition)) return false;
        if (!Objects.equals(trueBranch, that.trueBranch)) return false;
        if (!Objects.equals(falseBranch, that.falseBranch)) return false;
        return Objects.equals(type, that.type);
    }
}
