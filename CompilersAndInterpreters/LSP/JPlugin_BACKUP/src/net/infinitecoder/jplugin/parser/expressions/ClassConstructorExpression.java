package net.infinitecoder.jplugin.parser.expressions;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeClasses;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.statements.BlockStatement;
import net.infinitecoder.jplugin.parser.statements.ExpressionStatement;
import net.infinitecoder.jplugin.parser.values.*;

public class ClassConstructorExpression implements Expression {

    private final String name1;
    private final List<Expression> args;

    public ClassConstructorExpression(String name, List<Expression> args) {
        this.name1 = name;
        this.args = args;
    }

    @Override
    public Value eval() {
        String name = name1;
        if (activeVariables.defTypes.containsKey(name)) {
            name = activeVariables.defTypes.get(name);
        }
        BlockStatement bs = (BlockStatement) activeClasses.get(name).clone();
        bs.isStatic = true;
        if (!name.contains("::")) {
            bs.add(new ExpressionStatement(new FunctionCallExpression(name, args)));
        } else {
            bs.add(new ExpressionStatement(new FunctionCallExpression(name.substring(name.lastIndexOf("::") + 2), args)));
        }
        try {
            bs.execute();
        } catch (NoSuchMethodError nsme) {
            bs.statements.remove(bs.statements.size() - 1);
            bs.execute();
            bs.add(new ExpressionStatement(new FunctionCallExpression(name, args)));
        }
        bs.statements.remove(bs.statements.size() - 1);
        return new ObjectValue(bs, name);
    }

    @Override
    public String toString() {
        return name1 + args + "";
    }

    @Override
    public Expression clone() {
        List<Expression> newArgs = new ArrayList<>();
        for (Expression arg : args) {
            newArgs.add(arg.clone());
        }
        return new ClassConstructorExpression(name1.substring(0), newArgs);
    }
}
