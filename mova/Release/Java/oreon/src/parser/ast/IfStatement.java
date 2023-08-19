package parser.ast;

public class IfStatement implements Statement {

    private final Statement expression;
    private final Statement ifStatement;
    private final Statement elseStatement;
    private final int nif;

    public IfStatement(Statement expression, Statement ifStatement, Statement elseStatement, int nif) {
        this.expression = expression;
        this.ifStatement = ifStatement;
        this.elseStatement = elseStatement;
        this.nif = nif;
    }

    @Override
    public String build() {
        String res = "";
        if (expression instanceof LogicalExpression) {
            LogicalExpression lexpr = (LogicalExpression) expression;
            res += switchOp(lexpr);
        } else {
            String mod;
            if (expression instanceof ConstantExpression) {
                mod = "c";
            } else {
                mod = "v";
            }
            res += expression.build();
            res += "jni" + mod + " " + expression.getR() + " ifMN" + nif + "\n";
        }
        res += ifStatement.build();
        if (elseStatement != null) {
            res += "goto elseMN" + nif + "\n";
        }
        res += "met ifMN" + nif + "\n";
        if (elseStatement != null) {
            res += elseStatement.build();
            res += "met elseMN" + nif + "\n";
        }
        return res;
    }

    @Override
    public int getR() {
        System.out.println("Warning: unsuported operation adrGet for ifStatement!");
        return 0;
    }

    @Override
    public void setR(int adrResult) {
        System.out.println("Warning: unsuported operation adrSet for ifStatement!");
    }

    private String switchOp(LogicalExpression lexpr) {
        String mod;
        if (lexpr.S1 instanceof ConstantExpression) {
            mod = "c";
        } else {
            mod = "v";
        }
        if (lexpr.S2 instanceof ConstantExpression) {
            mod += "c";
        } else {
            mod += "v";
        }
        String res = "";
        switch (lexpr.op) {
            case "==":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jneq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                break;
            case "!=":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jeq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                break;
            case "<":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jnlt%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                break;
            case ">":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jngt%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                break;
            case "<=":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jnlt%s %s %s ifMN%d\n", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                res += String.format("jneq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                break;
            case ">=":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jngt%s %s %s ifMN%d\n", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                res += String.format("jneq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), nif);
                break;
            case "||":
                if (lexpr.S1 instanceof LogicalExpression) {
                    LogicalExpression s1 = (LogicalExpression) lexpr.S1;
                    res += switchOp(s1);
                } else {
                    res += lexpr.S1.build();
                    res += String.format("jni%s %s ifMN%d", mod, lexpr.S1.getR(), nif);
                }
                if (lexpr.S2 instanceof LogicalExpression) {
                    LogicalExpression s2 = (LogicalExpression) lexpr.S2;
                    res += switchOp(s2);
                } else {
                    res += lexpr.S2.build();
                    res += String.format("jni%s %s ifMN%d", mod, lexpr.S2.getR(), nif);
                }
                break;
            default:
                break;
        }
        return res + "\n";
    }

}
