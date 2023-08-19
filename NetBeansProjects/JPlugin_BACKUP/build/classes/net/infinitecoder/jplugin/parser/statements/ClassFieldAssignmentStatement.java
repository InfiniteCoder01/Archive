package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.expressions.*;
import net.infinitecoder.jplugin.parser.values.ObjectValue;

public class ClassFieldAssignmentStatement implements Statement {
    private final List<String> objectname;
    private final String name;
    private final Expression value;

    public ClassFieldAssignmentStatement(List<String> objectname, String name, Expression value) {
        this.objectname = objectname;
        this.name = name;
        this.value = value;
    }

    @Override
    public void execute() {
        ObjectValue obj = (ObjectValue)activeVariables.get(objectname.get(0));
        for (int i = 1; i < objectname.size(); i++) {
            String objname = objectname.get(i);
            obj = (ObjectValue) obj.body.localVarsData.get(objname);
        }
        obj.body.localVarsData.put(name, value.eval());
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public Statement clone() {
        List<String> newObjname = new ArrayList<>();
        for (String obj : objectname) {
            newObjname.add(obj.substring(0));
        }
        return new ClassFieldAssignmentStatement(newObjname, name.substring(0), value.clone());
    }
}
