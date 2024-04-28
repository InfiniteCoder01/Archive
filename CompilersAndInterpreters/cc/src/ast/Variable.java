package ast;

import ast.Type;
import ast.expression.Expression;

public class Variable {
    public Type type;
    public String name;
    public Expression value;

    public Variable(Type type, String name) {
        this.type = type;
        this.name = name;
        this.value = null;
    }

    public void optimize() {
        if (value != null) value = value.optimize();
    }

    @Override
    public String toString() {
        return String.format("%s %s = %s;\n", type, name, value);
    }
}
