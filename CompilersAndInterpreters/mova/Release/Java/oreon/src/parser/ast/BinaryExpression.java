package parser.ast;

public class BinaryExpression implements Statement {

    private int adrResult;
    private final String op;
    private final Statement S1;
    private final Statement S2;
    private String mod;

    public BinaryExpression(int adrResult, String op, Statement S1, Statement S2) {
        this.adrResult = adrResult;
        this.op = op;
        this.S1 = S1;
        this.S2 = S2;
    }

    @Override
    public String build() {
        String result = "";
        mod = "";
        if (S1 instanceof ConstantExpression) {
            mod += "c";
            result += S1.build();
            result += S2.build();
        } else {
            mod += "v";
        }
        if (S2 instanceof ConstantExpression) {
            mod += "c";
        } else {
            mod += "v";
        }
        result += S1.build();
        result += S2.build();
        switch (op) {
            case "+":
                result += "add" + mod + " " + adrResult + " " + S1.getR() + " " + S2.getR() + "\n";
                break;
            case "-":
                result += "sub" + mod + " " + adrResult + " " + S1.getR() + " " + S2.getR() + "\n";
                break;
            case "*":
                result += "mul" + mod + " " + adrResult + " " + S1.getR() + " " + S2.getR() + "\n";
                break;
            case "/":
                result += "del" + mod + " " + adrResult + " " + S1.getR() + " " + S2.getR() + "\n";
                break;
            case "%":
                result += "delost" + mod + " " + adrResult + " " + S1.getR() + " " + S2.getR() + "\n";
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
