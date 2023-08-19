package net.infinitecoder.jplugin.lib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.statements.*;

public class ClassesClass {

    public Map<String, Statement> classes;
    private final List<String> sNames;
    private final List<Statement> sBodys;

    public ClassesClass() {
        classes = new HashMap<>();
        sNames = new ArrayList<>();
        sBodys = new ArrayList<>();
    }

    public void put(String name, Statement body) {
        if (classes.containsKey(name)) {
            storeclass(name);
        }
        classes.put(name, body);
    }

    public Statement get(String name) {
        if (activeVariables.defTypes.containsKey(name)) {
            name = activeVariables.defTypes.get(name);
        }
        if(name.contains("::")) {
            String firstclass = name.substring(0, name.indexOf("::"));
            BlockStatement bs = (BlockStatement) get(firstclass);
            for(Statement st : bs.statements) {
                if(st instanceof ClassRegisterStatement) {
                    st.execute();
                    Statement body = get(name.substring(name.indexOf("::") + 2));
                    remove(((ClassRegisterStatement) st).name);
                    return body;
                }
            }
        }
        if (!classes.containsKey(name)) {
            throw new RuntimeException("Unknown class: " + name);
        }
        return classes.get(name);
    }

    public void storeclass(String name) {
        sNames.add(name);
        sBodys.add(classes.get(name));
    }

    public void restoreVariable(String name) {
        int index = sNames.lastIndexOf(name);
        sNames.remove(name);
        classes.put(name, sBodys.remove(index));
    }

    public void remove(String name) {
        if (activeVariables.defTypes.containsKey(name)) {
            classes.remove(activeVariables.defTypes.get(name));
        } else {
            classes.remove(name);
        }
        if (sNames.contains(name)) {
            restoreVariable(name);
        }
    }
}
