package net.infinitecoder.jplugin.parser.values;

public interface Value {
    public Value cast(String type);
    public Value operator(String operator, Value other);
    public Value clone();
}
