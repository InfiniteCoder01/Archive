package net.infinitecoder.jplugin.parser.statements;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.parser.expressions.Expression;
import net.infinitecoder.jplugin.parser.values.BoolValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class SwitchStatement implements Statement {

    public final Expression ex;
    public final List<Statement> cases;
    public final List<Expression> caseValues;
    public Statement defaultCase;

    public SwitchStatement(Expression ex) {
        this.ex = ex;
        this.cases = new ArrayList<>();
        this.caseValues = new ArrayList<>();
        this.defaultCase = null;
    }

    public SwitchStatement(Expression ex, Statement defaultCase) {
        this.ex = ex;
        this.cases = new ArrayList<>();
        this.caseValues = new ArrayList<>();
        this.defaultCase = defaultCase;
    }

    public SwitchStatement(Expression ex, List<Statement> cases, List<Expression> caseValues, Statement defaultCase) {
        this.ex = ex;
        this.cases = cases;
        this.caseValues = caseValues;
        this.defaultCase = defaultCase;
    }

    @Override
    public void execute() {
        Value val = ex.eval();
        boolean f = false;
        for (int i = 0; i < caseValues.size(); i++) {
            if (((BoolValue)caseValues.get(i).eval().operator("==", val)).value != 0 || f) {
                try {
                    cases.get(i).execute();
                    f = true;
                } catch (BreakStatement bs) {
                    return;
                }
            }
        }
        if (defaultCase != null) {
            defaultCase.execute();
        }
    }

    @Override
    public String toString() {
        return "switch(" + ex + ") ...";
    }

    public void add(Expression ex, Statement st) {
        caseValues.add(ex);
        cases.add(st);
    }

    @Override
    public Statement clone() {
        List<Statement> newCases = new ArrayList<>();
        for (Statement st : cases) {
            newCases.add(st.clone());
        }
        List<Expression> newCaseValues = new ArrayList<>();
        for (Expression value : caseValues) {
            newCaseValues.add(value.clone());
        }
        if (defaultCase == null) {
            return new SwitchStatement(ex.clone(), newCases, newCaseValues, null);
        } else {
            return new SwitchStatement(ex.clone(), newCases, newCaseValues, defaultCase);
        }
    }
}
