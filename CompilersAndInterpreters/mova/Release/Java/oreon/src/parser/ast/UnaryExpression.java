package parser.ast;

public class UnaryExpression implements Statement {

    private int adrResult;
    private final String op;
    private final Statement S;
    private String mod;

    public UnaryExpression(int adrResult, String op, Statement S) {
        this.adrResult = adrResult;
        this.op = op;
        this.S = S;
    }

    @Override
    public String build() {
        String result = "";
        mod = "";
        if (S instanceof ConstantExpression) {
            mod += "c";
        } else {
            mod += "v";
        }
        switch (op) {
            case "!":
                result += S.build();
                result += "not" + mod + " " + adrResult + " " + S.getR() + "\n";
                break;
            case "~":
                result += S.build();
                result += "bitnot" + mod + " " + adrResult + " " + S.getR() + "\n";
                break;
            default:
        }
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

}
