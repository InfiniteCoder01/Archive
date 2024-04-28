package net.infinitecoder.jplugin.parser.values;

import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

public class FloatValue implements Value {

    public float value;

    public FloatValue(float value) {
        this.value = value;
    }

    @Override
    public Value cast(String type) {
        switch(type) {
            case "long":
                return new LongValue((long)value);
            case "int":
                return new IntValue((int)value);
            case "short":
                return new ShortValue((short)value);
            case "double":
                return new DoubleValue(value);
            case "float":
                return new FloatValue(value);
            case "bool":
                return new BoolValue((byte)value);
            case "char":
                return new CharValue((byte)value);
            case "String":
                return new StringValue(value + "");
            case "void":
                return this;
        }
        List<Byte> bytes = activeVariables.getBytes(this);
        return activeVariables.fromBytes(type, bytes);
//        throw new classCastException("Can not cast " + value + " to " + type + "!");
    }

    @Override
    public Value operator(String operator, Value other) {
        if (other == null) {
            switch (operator) {
                case "++":
                    value++;
                    return new FloatValue(value);
                case "--":
                    value--;
                    return new FloatValue(value);
                case " ++":
                    return new FloatValue(value++);
                case " --":
                    return new FloatValue(value--);
            }
        }
        if (other instanceof DoubleValue || other instanceof FloatValue || other instanceof LongValue || other instanceof IntValue || other instanceof CharValue || other instanceof BoolValue) {
            double otherValue = ((DoubleValue)other.cast("double")).value;
            switch (operator) {
                case "+":
                    return new DoubleValue(value + otherValue).cast(activeVariables.greatest(activeVariables.typeof(this), activeVariables.typeof(other)));
                case "-":
                    return new DoubleValue(value - otherValue).cast(activeVariables.greatest(activeVariables.typeof(this), activeVariables.typeof(other)));
                case "*":
                    return new DoubleValue(value * otherValue).cast(activeVariables.greatest(activeVariables.typeof(this), activeVariables.typeof(other)));
                case "/":
                    return new DoubleValue(value / otherValue).cast(activeVariables.greatest(activeVariables.typeof(this), activeVariables.typeof(other)));
                case "%":
                    return new DoubleValue(value % otherValue).cast(activeVariables.greatest(activeVariables.typeof(this), activeVariables.typeof(other)));
                case ">":
                    return new BoolValue((byte) ((value > otherValue) ? 1 : 0));
                case "<":
                    return new BoolValue((byte) ((value < otherValue) ? 1 : 0));
                case "==":
                    return new BoolValue((byte) ((value == otherValue) ? 1 : 0));
                case "!=":
                    return new BoolValue((byte) ((value != otherValue) ? 1 : 0));
                case ">=":
                    return new BoolValue((byte) ((value >= otherValue) ? 1 : 0));
                case "<=":
                    return new BoolValue((byte) ((value <= otherValue) ? 1 : 0));
            }
        }
        if (other instanceof StringValue) {
            String otherValue = ((StringValue)other).value;
            switch (operator) {
                case "+":
                    return new StringValue(value + otherValue);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value + "";
    }

    @Override
    public Value clone() {
        return new FloatValue(value);
    }
}
