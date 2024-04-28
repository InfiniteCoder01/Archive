package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeFunctions;

public class FunctionRegisterStatement implements Statement {

    public final String type, name;
    public final List<String> arglist;
    public final Statement body;

    public FunctionRegisterStatement(String type, String name, List<String> arglist, Statement body) {
        this.type = type;
        this.name = name;
        this.arglist = arglist;
        this.body = body;
    }

    @Override
    public void execute() {
        activeFunctions.put(name, type, body, arglist);
    }

    @Override
    public String toString() {
        return type + " " + name + arglist + body;
    }

    @Override
    public Statement clone() {
        List<String> newArglist = new ArrayList<>();
        for (String arg : arglist) {
            newArglist.add(arg.substring(0));
        }
        return new FunctionRegisterStatement(type.substring(0), name.substring(0), newArglist, body.clone());
    }
}
