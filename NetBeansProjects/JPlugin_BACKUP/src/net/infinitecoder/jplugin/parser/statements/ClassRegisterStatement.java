package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeClasses;

public class ClassRegisterStatement implements Statement {

    public final String name;
    public final Statement body;
    public final List<String> superclasses;

    public ClassRegisterStatement(String name, Statement body, List<String> superclasses) {
        this.name = name;
        this.body = body;
        this.superclasses = superclasses;
    }

    @Override
    public void execute() {
        BlockStatement bs = (BlockStatement) body;
        for (String superclass : superclasses) {
            BlockStatement superBody = (BlockStatement) activeClasses.get(superclass);
            for (Statement st : superBody.statements) {
                if (!hasStatement(bs, st)) {
                    bs.add(st);
                }
            }
        }
        activeClasses.put(name, body);
    }

    @Override
    public String toString() {
        return "class " + name + body;
    }

    @Override
    public Statement clone() {
        List<String> supers = new ArrayList<>();
        for (String superclass : superclasses) {
            supers.add(superclass.substring(0));
        }
        return new ClassRegisterStatement(name.substring(0), body.clone(), supers);
    }

    private boolean hasStatement(BlockStatement bs, Statement st) {
        if (st instanceof FunctionRegisterStatement) {
            for (Statement in : bs.statements) {
                if (in instanceof FunctionRegisterStatement) {
                    if (((FunctionRegisterStatement) in).name.equals(((FunctionRegisterStatement) st).name)) {
                        if (((FunctionRegisterStatement) in).arglist.equals(((FunctionRegisterStatement) st).arglist)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
