package net.infinitecoder.jplugin.parser.expressions;

import static net.infinitecoder.jplugin.JPlugin.activeClasses;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.statements.BlockStatement;
import net.infinitecoder.jplugin.parser.statements.ObjectCreationStatement;
import net.infinitecoder.jplugin.parser.statements.Statement;
import net.infinitecoder.jplugin.parser.values.IntValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class SizeOfExpression implements Expression {

    private final Expression value;

    public SizeOfExpression(Expression value) {
        this.value = value;
    }

    @Override
    public Value eval() {
        String type = "";
        if(value instanceof VariableExpression) {
            type = ((VariableExpression)value).name;
        }
        if(activeVariables.defTypes.containsKey(type)) {
            type = activeVariables.defTypes.get(type);
        }
        if(activeVariables.typeof(type).equals("NON_TYPE")) {
            return new IntValue(activeVariables.getBytes(value.eval()).size());
        }
        switch(type) {
            case "long":
                return new IntValue(8);
            case "int":
                return new IntValue(4);
            case "short":
                return new IntValue(2);
            case "double":
                return new IntValue(8);
            case "float":
                return new IntValue(4);
            case "char":
                return new IntValue(1);
            case "bool":
                return new IntValue(1);
            case "String":
                throw new UnsupportedOperationException("Can not get size of type String!");
        }
        int size = 0;
        BlockStatement bs = (BlockStatement) activeClasses.get(type);
        for(Statement st : bs.statements) {
            if(st instanceof ObjectCreationStatement) {
                size += ((IntValue)new SizeOfExpression(new VariableExpression(((ObjectCreationStatement) st).type1)).eval()).value * ((ObjectCreationStatement) st).names.size();
            }
        }
        return new IntValue(size);
    }

    @Override
    public String toString() {
        return "sizeof(" + value + ")";
    }

    @Override
    public Expression clone() {
        return new SizeOfExpression(value.clone());
    }
}
