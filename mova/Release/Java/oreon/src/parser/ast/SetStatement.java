package parser.ast;

import parser.ast.Statement;

public class SetStatement implements Statement {

    private int adrResult;
    private final Statement statement;

    public SetStatement(int ValOut, Statement statement) {
        adrResult = ValOut;
        this.statement = statement;
    }

    @Override
    public String build() {
        String result = "";
        String mod = "";
        if (statement instanceof ConstantExpression) {
            mod += "c";
        } else {
            mod += "v";
        }
        result += statement.build();
        result += "mov" + mod + " " + adrResult + " " + statement.getR() + "\n";
        return result;
    }

    @Override
    public int getR() {
        return adrResult;
    }

    @Override
    public void setR(int adrResult) {
        this.adrResult = adrResult;
    }

    public Statement getStatement() {
        return statement;
    }

    @Override
    public String toString() {
        return "set " + adrResult + " " + statement;
    }

}
