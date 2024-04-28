package net.infinitecoder.jplugin.parser.expressions;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.statements.BlockStatement;
import net.infinitecoder.jplugin.parser.statements.ReturnStatement;
import net.infinitecoder.jplugin.parser.values.ObjectValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class ClassFunctionExpression implements Expression {

    private final String objectname, name;
    private final List<Expression> args;

    public ClassFunctionExpression(String classname, String name, List<Expression> args) {
        this.objectname = classname;
        this.name = name;
        this.args = args;
    }

    @Override
    public Value eval() {
        try {
            List<Expression> finalArgs = new ArrayList<>();
            for(Expression arg : args) {
                finalArgs.add(new ValueExpression(arg.eval()));
            }
            BlockStatement body = ((ObjectValue) activeVariables.get(objectname)).body;
            body.statements.add(new ReturnStatement(new FunctionCallExpression(name, finalArgs)));
            body.execute();
        } catch (ReturnStatement rs) {
            BlockStatement body = ((ObjectValue) activeVariables.get(objectname)).body;
            body.statements.remove(body.statements.size() - 1);
            return rs.val;
        }
        BlockStatement body = ((ObjectValue) activeVariables.get(objectname)).body;
        body.statements.remove(body.statements.size() - 1);
        return null;
    }

    @Override
    public String toString() {
        return objectname + "." + name + args + "";
    }

    @Override
    public Expression clone() {
        List<Expression> newArgs = new ArrayList<>();
        for (Expression arg : args) {
            newArgs.add(arg.clone());
        }
        return new ClassFunctionExpression(objectname.substring(0), name.substring(0), newArgs);
    }
}
