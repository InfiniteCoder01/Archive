package ast.expression;

import ast.Type;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class BlockExpression implements Expression {
    public List<Expression> body;
    public List<ReturnExpression> returns;
    public Type type;

    public BlockExpression() {
        body = new ArrayList<>();
        returns = new LinkedList<>();
    }

    public void add(Expression expression) {
        body.add(expression);
    }

    public void findType() {
        type = null;
        for (ReturnExpression expression : returns) {
            if (type == null) type = expression.expression.getType();
            else type = Type.max(type, expression.expression.getType());
        }
    }

    @Override
    public Expression optimize() {
        for (int i = 0; i < body.size(); i++) {
            body.set(i, body.get(i).optimize());
            if (body.get(i) instanceof ReturnExpression) {
                while (body.size() > i + 1) body.remove(i + 1); // There might be leak, because I'm removing statement but not from returns
            }
        }
        if (body.size() == 1) {
            if (type == null) return body.get(0);
            if (body.get(0) instanceof ReturnExpression) return ((ReturnExpression) body.get(0)).expression;
        }
        return this;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public ConstantExpression evaluate() {
        try {
            for (Expression statement : body) {
                statement.evaluate();
            }
        } catch (RuntimeException ex) {
            if(ex.getMessage().)
        }
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder(String.format("@%s {\n", type));
        for (Expression statement : body) {
            for (String line : statement.toString().split("\n")) {
                string.append("  ").append(line).append('\n');
            }
        }
        string.append("}");
        return string.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;

        BlockExpression that = (BlockExpression) other;

        if (!Objects.equals(body, that.body)) return false;
        if (!Objects.equals(returns, that.returns)) return false;
        return Objects.equals(type, that.type);
    }
}
