package net.infinitecoder.jplugin.parser.expressions;

import net.infinitecoder.jplugin.parser.values.Value;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

public class CastExpression implements Expression {

    private final Expression expression;
    private final String type;

    public CastExpression(Expression expression, String type) {
        this.expression = expression;
        this.type = type;
    }

    @Override
    public Value eval() {
        if (activeVariables.defTypes.containsKey(type)) {
            return expression.eval().cast(activeVariables.defTypes.get(type));
        }
        return expression.eval().cast(type);
    }

    @Override
    public String toString() {
        return "((" + type + ") " + expression + ")";
    }

    @Override
    public Expression clone() {
        return new CastExpression(expression.clone(), type.substring(0));
    }
}
