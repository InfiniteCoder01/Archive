package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.infinitecoder.jplugin.JPlugin.activeClasses;
import static net.infinitecoder.jplugin.JPlugin.activeFunctions;
import static net.infinitecoder.jplugin.JPlugin.activeVariables;
import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.expressions.ValueExpression;
import net.infinitecoder.jplugin.parser.values.Value;

public class BlockStatement implements Statement {

    public final List<Statement> statements;
    public final List<String> localVars;
    public final List<String> localFunctions;
    public final List<String> localclasses;
    public Map<String, Value> localVarsData;
    public boolean isStatic = false;

    public BlockStatement() {
        this.statements = new ArrayList<>();
        this.localclasses = new ArrayList<>();
        this.localFunctions = new ArrayList<>();
        this.localVars = new ArrayList<>();
        this.localVarsData = null;
    }

    public BlockStatement(List<Statement> statements) {
        this.localFunctions = new ArrayList<>();
        this.localclasses = new ArrayList<>();
        this.statements = statements;
        this.localVars = new ArrayList<>();
        this.localVarsData = null;
    }

    public BlockStatement(List<Statement> statements, List<String> localVars, List<String> localclasses, List<String> localFunctions, Map<String, Value> localVarsData, boolean isStatic) {
        this.localFunctions = localFunctions;
        this.localVarsData = localVarsData;
        this.localclasses = localclasses;
        this.statements = statements;
        this.localVars = localVars;
        this.isStatic = isStatic;
    }

    @Override
    public void execute() {
        try {
            for (Statement st : statements) {
                if (st instanceof ObjectCreationStatement && isStatic && localVarsData != null) {
                    new ObjectCreationStatement(
                            ((ObjectCreationStatement) st).type1,
                            ((ObjectCreationStatement) st).names,
                            getValues(((ObjectCreationStatement) st).names)
                    ).execute();
                } else {
                    st.execute();
                }
            }
        } catch (Exception | Error e) {
            storeData();
            throw e;
        }
        storeData();
    }

    public boolean add(Statement e) {
        if(e == null) {
            return false;
        }
        if (e instanceof ObjectCreationStatement) {
            localVars.addAll(((ObjectCreationStatement) e).names);
        }
        if (e instanceof ClassRegisterStatement) {
            localclasses.add(((ClassRegisterStatement) e).name);
        }
        if (e instanceof FunctionRegisterStatement) {
            localFunctions.add(((FunctionRegisterStatement) e).name);
        }
        return statements.add(e);
    }

    public int size() {
        return statements.size();
    }

    @Override
    public String toString() {
        String str = "{\n";
        for (Statement st : statements) {
            str += st.toString() + "\n";
        }
        str += "}";
        return str;
    }

    private void storeData() {
        localVarsData = new HashMap<>();
        for (String var : localVars) {
            localVarsData.put(var, activeVariables.remove(var));
        }
        for (String localclass : localclasses) {
            activeClasses.remove(localclass);
        }
        for (String function : localFunctions) {
            activeFunctions.remove(function);
        }
    }

    @Override
    public Statement clone() {
        List<Statement> newStatements = new ArrayList<>();
        for (Statement statement : statements) {
            newStatements.add(statement.clone());
        }
        List<String> newLocalVars = new ArrayList<>();
        for (String var : localVars) {
            newLocalVars.add(var.substring(0));
        }
        List<String> newLocalFunctions = new ArrayList<>();
        for (String fnc : localFunctions) {
            newLocalFunctions.add(fnc.substring(0));
        }
        Map<String, Value> newLocalVarsData = new HashMap<>();
        if (localVarsData == null) {
            newLocalVarsData = null;
        } else {
            for (String var : localVars) {
                if (localVarsData.containsKey(var)) {
                    newLocalVarsData.put(var, localVarsData.get(var).clone());
                }
            }
        }
        List<String> newLocalclasses = new ArrayList<>();
        for (String localclass : localclasses) {
            newLocalclasses.add(localclass.substring(0));
        }
        return new BlockStatement(newStatements, newLocalVars, newLocalclasses, newLocalFunctions, newLocalVarsData, isStatic);
    }

    private List<Expression> getValues(List<String> names) {
        List<Expression> values = new ArrayList<>();
        for(String name : names) {
            values.add(new ValueExpression(localVarsData.get(name)));
        }
        return values;
    }
}
