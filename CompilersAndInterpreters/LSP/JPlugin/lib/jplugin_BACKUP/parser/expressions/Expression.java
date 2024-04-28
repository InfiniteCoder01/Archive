package net.infinitecoder.jplugin.parser.expressions;

import net.infinitecoder.jplugin.parser.values.Value;

public interface Expression {
    public Value eval();
    public Expression clone();
}
