package net.infinitecoder.jplugin.lib;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.expressions.*;
import net.infinitecoder.jplugin.parser.statements.*;
import net.infinitecoder.jplugin.parser.values.*;

public class FunctionsClass {

    public Map<String, List<List<String>>> arglists;
    public Map<String, List<Statement>> functions;
    public Map<String, List<String>> types;
    public List<String> sNames;
    public List<String> sTypes;
    public List<Statement> sBodys;
    public List<List<String>> sArglists;

    public FunctionsClass() {
        functions = new HashMap<>();
        arglists = new HashMap<>();
        types = new HashMap<>();
        sNames = new ArrayList<>();
        sTypes = new ArrayList<>();
        sBodys = new ArrayList<>();
        sArglists = new ArrayList<>();
    }

    public void put(String name, String type, JCode body) {
        put(name, type, body, Arrays.asList(body.args));
    }

    public void put(String name, String type, Statement body, List<String> arglist) {
        if (activeVariables.defTypes.containsKey(type)) {
            type = activeVariables.defTypes.get(type);
        }
        if (functions.containsKey(name)) {
            if (testforarglist(arglists.get(name), arglist) != -1) {
                storeFunction(name, testforarglist(arglists.get(name), arglist));
            }
            arglists.get(name).add(arglist);
            functions.get(name).add(body);
            types.get(name).add(type);
        } else {
            functions.put(name, new ArrayList<>());
            arglists.put(name, new ArrayList<>());
            types.put(name, new ArrayList<>());
            arglists.get(name).add(arglist);
            functions.get(name).add(body);
            types.get(name).add(type);
        }
    }

    public SimpleEntry<String, Statement> get(String name, List<Expression> args) {
        if (!functions.containsKey(name)) {
            throw new NoSuchMethodError("Can not find function " + name + "!");
        }
        List<Value> finalArgs = new ArrayList<>();
        List<String> testArglist = new ArrayList<>();
        BlockStatement bs = new BlockStatement();
        for (Expression arg : args) {
            Value v = arg.eval();
            finalArgs.add(v);
            testArglist.add(activeVariables.typeof(v));
        }
        int functionIndex = 0;
        for (List<String> arglist : arglists.get(name)) {
            if (arglist.size() == testArglist.size()) {
                for (int i = 0; i < arglist.size(); i++) {
                    String arg = arglist.get(i);
                    if (activeVariables.defTypes.containsKey(arg.split(" ")[0])) {
                        arg = activeVariables.defTypes.get(arg.split(" ")[0]) + " " + arg.split(" ")[1];
                    }
                    if (arg.startsWith(testArglist.get(i)) || arg.startsWith("void")) {
                        bs.add(new ObjectCreationStatement(arg.split(" ")[0], Arrays.asList(new String[]{arg.split(" ")[1]}), Arrays.asList(new Expression[]{new ValueExpression(finalArgs.get(i))})));
                    } else {
                        bs = new BlockStatement();
                        break;
                    }
                }
            }
            if (bs.size() == arglist.size() && arglist.size() == finalArgs.size()) {
                bs.add(functions.get(name).get(functionIndex));
                return new SimpleEntry<>(types.get(name).get(functionIndex), bs);
            }
            functionIndex++;
        }
        throw new RuntimeException("Function " + name + " with args " + args + " does not exists!");
    }

    private void storeFunction(String name, int idx) {
        sNames.add(name);
        sTypes.add(types.get(name).get(idx));
        sBodys.add(functions.get(name).get(idx));
        sArglists.add(arglists.get(name).get(idx));
        arglists.get(name).remove(idx);
        functions.get(name).remove(idx);
        types.get(name).remove(idx);
    }

    private void restoreFunction(String name) {
        int index = sNames.lastIndexOf(name);
        put(sNames.remove(index), sTypes.remove(index), sBodys.remove(index), sArglists.remove(index));
    }

    public void remove(String name) {
        try {
            int index = functions.get(name).size() - 1;
            arglists.get(name).remove(index);
            functions.get(name).remove(index);
            types.get(name).remove(index);
            restoreFunction(name);
        } catch (Exception ex) {
        }
    }

    private int testforarglist(List<List<String>> collection, List<String> arglist) {
        int listIndex = 0;
        for (List<String> testArglist : collection) {
            boolean f = true;
            for (int i = 0; i < Math.min(arglist.size(), testArglist.size()); i++) {
                String arg = arglist.get(i);
                if (!((arg.split(" ")[0]).startsWith(testArglist.get(i).split(" ")[0]))) {
                    f = false;
                    break;
                }
            }
            if (f) {
                return listIndex;
            }
            listIndex++;
        }
        return -1;
    }
}
