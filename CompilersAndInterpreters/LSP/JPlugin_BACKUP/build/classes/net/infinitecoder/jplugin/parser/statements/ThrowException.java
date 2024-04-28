package net.infinitecoder.jplugin.parser.statements;

import java.io.PrintStream;
import net.infinitecoder.jplugin.parser.values.StringValue;
import net.infinitecoder.jplugin.parser.values.Value;

@SuppressWarnings("serial")
public class ThrowException extends RuntimeException {
    public final Value val;

    public ThrowException(Value val) {
        super(val.operator("+", new StringValue("")) + "");
        this.val = val;
    }

    @Override
    public void printStackTrace(PrintStream s) {
        s.println(": " + getMessage());
    }
}
