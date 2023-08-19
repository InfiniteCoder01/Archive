package net.infinitecoder.jplugin.values;

public class ByteValue implements Value {

    public byte value;

    public ByteValue(byte value) {
        this.value = value;
    }

    @Override
    public Value operator(String operator, Value other) {
        if (other == null) {
            switch (operator) {
                case " ++":
                    return new ByteValue(value++);
                case "++":
                    return new ByteValue(++value);
                case " --":
                    return new ByteValue(value--);
                case "--":
                    return new ByteValue(--value);
                case "!":
                    return new ByteValue(value == 0 ? (byte)1 : (byte)0);
                case "~":
                    return new ByteValue((byte) ~value);
            }
        } else {
            if (other instanceof ByteValue) {
                byte a = value, b = ((ByteValue) other).value;
                switch (operator) {
                    case "+":
                        return new ByteValue((byte) (a + b));
                    case "-":
                        return new ByteValue((byte) (a - b));
                    case "*":
                        return new ByteValue((byte) (a * b));
                    case "/":
                        return new ByteValue((byte) (a / b));
                    case "%":
                        return new ByteValue((byte) (a % b));
                    case "&&":
                        return new ByteValue((byte) ((a != 0 && b != 0) ? 1 : 0));
                }
            }
        }
        return null;
    }

    @Override
    public Value cast(String type) {
        return this;
    }

    @Override
    public String toString() {
        return value + "";
    }
}
