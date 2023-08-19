package net.infinitecoder.jplugin.parser.values;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.expressions.FunctionCallExpression;
import net.infinitecoder.jplugin.parser.expressions.ValueExpression;
import net.infinitecoder.jplugin.parser.statements.BlockStatement;
import net.infinitecoder.jplugin.parser.statements.ReturnStatement;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

public class ObjectValue implements Value {

    public BlockStatement body;
    public final String classname;

    public ObjectValue(BlockStatement body, String name) {
        this.body = body;
        this.classname = name;
    }

    @Override
    public Value cast(String type) {
        if(type.equals("void")) {
            return this;
        }
        List<Byte> bytes = activeVariables.getBytes(this);
        return activeVariables.fromBytes(type, bytes);
    }

    @Override
    public Value operator(String operator, Value other) {
        List<Expression> args = new ArrayList<>();
        args.add(new ValueExpression(other));
        body.add(new ReturnStatement(new FunctionCallExpression("operator" + operator, args)));
        try {
            body.execute();
        } catch (ReturnStatement rs) {
            body.statements.remove(body.statements.size() - 1);
            return rs.val;
        } catch (NoSuchMethodError nsme) {
        }
        throw new RuntimeException("Error: class " + classname + " is not overriding operator " + operator + "!");
    }

    @Override
    public String toString() {
        return body + "";
    }

    @Override
    public Value clone() {
        return new ObjectValue((BlockStatement) body.clone(), classname.substring(0));
    }
}
