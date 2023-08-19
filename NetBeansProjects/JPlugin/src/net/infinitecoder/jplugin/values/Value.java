package net.infinitecoder.jplugin.values;

public interface Value {
    public Value cast(String type);
    public Value operator(String operator, Value other);
}