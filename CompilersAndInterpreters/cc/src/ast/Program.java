package ast;

import java.util.LinkedList;
import java.util.List;

public class Program {
    public List<Variable> variables = new LinkedList<>();

    public void optimize() {
        for (Variable variable : variables) variable.optimize();
    }

    public String execute() {
        for (Variable variable : variables) {
            if (variable.name.equals("main")) return variable.value.evaluate().constant;
        }
        throw new RuntimeException("Unable to execute program. No main entrypoint found. Consider defining main function");
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (Variable function : variables) {
            string.append(function);
        }
        return string.toString();
    }
}