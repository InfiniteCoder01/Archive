package net.infinitecoder.jplugin.parser.values;

import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;

public class StringValue implements Value {

    public String value;

    public StringValue(String value) {
        this.value = value;
    }

    @Override
    public Value cast(String type) {
        switch(type) {
            case "String":
                return new StringValue(value);
            case "long":
                return new LongValue(Long.parseLong(value));
            case "int":
                return new IntValue(Integer.parseInt(value));
            case "short":
                return new ShortValue(Short.parseShort(value));
            case "double":
                return new DoubleValue(Double.parseDouble(value));
            case "float":
                return new FloatValue(Float.parseFloat(value));
            case "bool":
                return new BoolValue(Byte.parseByte(value));
            case "char":
                return new CharValue(Byte.parseByte(value));
            case "void":
                return this;
        }
        List<Byte> bytes = activeVariables.getBytes(this);
        return activeVariables.fromBytes(type, bytes);
//        throw new classCastException("Can not cast " + value + " to " + type + "!");
    }

    @Override
    public Value operator(String operator, Value other) {
        if (other instanceof DoubleValue || other instanceof FloatValue || other instanceof LongValue || other instanceof IntValue || other instanceof BoolValue) {
            double otherValue = ((DoubleValue)other.cast("double")).value;
            switch (operator) {
                case "+":
                    return new StringValue(value + otherValue);
                case "[]":
                    return new CharValue((byte) value.charAt((int)otherValue));
            }
        }
        if(other instanceof CharValue) {
            switch (operator) {
                case "+":
                    return new StringValue(value + (char)((CharValue) other).value);
            }
        }
        if (other instanceof StringValue) {
            String otherValue = ((StringValue)other).value;
            switch (operator) {
                case "+":
                    return new StringValue(value + otherValue);
                case "==":
                    return new BoolValue((byte) (value.equals(otherValue) ? 1 : 0));
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return value;
//        return "\"" + value + "\"";
    }

    @Override
    public Value clone() {
        return new StringValue(value.substring(0));
    }
}
