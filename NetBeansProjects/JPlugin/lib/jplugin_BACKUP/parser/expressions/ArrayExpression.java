package net.infinitecoder.jplugin.parser.expressions;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.parser.values.ArrayValue;
import net.infinitecoder.jplugin.parser.values.IntValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class ArrayExpression implements Expression {

    private final List<Expression> expressions;

    public ArrayExpression(List<Expression> expressions) {
        this.expressions = expressions;
    }

    public ArrayExpression(List<Expression> sizes, int zero) {
        this.expressions = new ArrayList<>();
        for (int i = 0; i < ((IntValue) sizes.get(0).eval().cast("int")).value; i++) {
            if (sizes.size() == 1) {
                expressions.add(new ValueExpression(new IntValue(0)));
            } else {
                expressions.add(new ArrayExpression(sizes.subList(1, sizes.size()), zero));
            }
        }
    }

    @Override
    public Value eval() {
        Value[] values = new Value[expressions.size()];
        for (int i = 0; i < expressions.size(); i++) {
            values[i] = expressions.get(i).eval();
        }
        return new ArrayValue(values);
    }

    @Override
    public String toString() {
        return expressions + "";
    }

    @Override
    public Expression clone() {
        List<Expression> newExpressions = new ArrayList<>();
        for (Expression ex : expressions) {
            newExpressions.add(ex.clone());
        }
        return new ArrayExpression(newExpressions);
    }
}
