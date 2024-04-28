package ast.expression;

import ast.Type;

public interface Expression {
    Type getType();
    Expression optimize();
    ConstantExpression evaluate();
}
