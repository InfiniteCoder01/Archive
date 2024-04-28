package parser.ast;

public class LogicalExpression implements Statement {

    private int adrResult;
    final String op;
    final Statement S1;
    final Statement S2;
    private String mod;

    public LogicalExpression(int adrResult, String op, Statement S1, Statement S2) {
        this.adrResult = adrResult;
        this.op = op;
        this.S1 = S1;
        this.S2 = S2;
    }

    @Override
    public String build() {
        String res = "";
        mod = "";
        if (S1 instanceof ConstantExpression) {
            mod += "c";
        } else {
            mod += "v";
        }
        if (S2 instanceof ConstantExpression) {
            mod += "c";
        } else {
            mod += "v";
        }
        switch (op) {
            case "==":
                res += S1.build();
                res += S2.build();
                res += String.format("eqals%s %d %s %s", mod, adrResult, S1.getR(), S2.getR());
                break;
            case "!=":
                res += S1.build();
                res += S2.build();
                res += String.format("neqals%s %d %s %s", mod, adrResult, S1.getR(), S2.getR());
                break;
            case "<":
                res += S1.build();
                res += S2.build();
                res += String.format("lt%s %d %s %s", mod, adrResult, S1.getR(), S2.getR());
                break;
            case ">":
                res += S1.build();
                res += S2.build();
                res += String.format("gt%s %d %s %s", mod, adrResult, S1.getR(), S2.getR());
                break;
            case "<=":
                res += S1.build();
                res += S2.build();
                res += String.format("lt%s %d %s %s\n", mod, adrResult, S1.getR(), S2.getR());
                res += String.format("ieqalsmv%s %d %s %s", mod, adrResult, S1.getR(), S2.getR());
                break;
            case ">=":
                res += S1.build();
                res += S2.build();
                res += String.format("gt%s %d %s %s\n", mod, adrResult, S1.getR(), S2.getR());
                res += String.format("ieqalsmv%s %d %s %s", mod, adrResult, S1.getR(), S2.getR());
                break;
            default:
                break;
        }
        return res;
    }

    @Override
    public int getR() {
        return adrResult;
    }

    @Override
    public void setR(int adrResult) {
        this.adrResult = adrResult;
    }

}
