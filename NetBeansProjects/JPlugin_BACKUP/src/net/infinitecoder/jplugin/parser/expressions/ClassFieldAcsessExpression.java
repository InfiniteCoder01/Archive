package net.infinitecoder.jplugin.parser.expressions;

import net.infinitecoder.jplugin.parser.values.ObjectValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class ClassFieldAcsessExpression implements Expression {
    private final Expression objectname;
    private final String name;

    public ClassFieldAcsessExpression(Expression classname, String name) {
        this.objectname = classname;
        this.name = name;
    }
    
    @Override
    public Value eval() {
        return ((ObjectValue)objectname.eval()).body.localVarsData.get(name);
//        return ((ObjectValue)activeVariables.get(objectname)).body.localVarsData.get(name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Expression clone() {
        return new ClassFieldAcsessExpression(objectname.clone(), name.substring(0));
    }
}
