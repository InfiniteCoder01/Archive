package net.infinitecoder.jplugin.parser.values;

import java.util.Arrays;

public class ArrayValue implements Value {

    public final Value[] values;

    public ArrayValue(int size) {
        this.values = new Value[size];
    }

    public ArrayValue(Value[] values) {
        this.values = values;
    }

    @Override
    public Value cast(String type) {
        Value[] casted = new Value[values.length];
        for (int i = 0; i < values.length; i++) {
            casted[i] = values[i].cast(type);
        }
        return new ArrayValue(casted);
    }

    @Override
    public Value operator(String operator, Value other) {
        if (other instanceof DoubleValue || other instanceof FloatValue || other instanceof LongValue || other instanceof IntValue || other instanceof BoolValue) {
            double otherValue = ((DoubleValue) other.cast("double")).value;
            switch (operator) {
                case "[]":
                    return values[(int) otherValue];
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return Arrays.toString(values);
    }

    @Override
    public Value clone() {
        Value[] newValues = new Value[values.length];
        for (int i = 0; i < values.length; i++) {
            newValues[i] = values[i].clone();
        }
        return new ArrayValue(newValues);
    }
}
