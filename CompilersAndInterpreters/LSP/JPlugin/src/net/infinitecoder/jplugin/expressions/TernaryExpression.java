package net.infinitecoder.jplugin.expressions;

import net.infinitecoder.jplugin.values.Value;

public class TernaryExpression implements Expression {
    private final Expression expr, trueCase, falseCase;

    public TernaryExpression(Expression expr, Expression trueCase, Expression falseCase) {
        this.expr = expr;
        this.trueCase = trueCase;
        this.falseCase = falseCase;
    }
    
    @Override
    public Value eval() {
        return expr.eval().toString().equals("0") ? falseCase.eval() : trueCase.eval();
    }

    @Override
    public String toString() {
        return "(" + expr + " ? " + trueCase + " : " + falseCase + ")";
    }
}
