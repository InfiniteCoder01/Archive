package parser.ast;

public class doWhileStatement implements Statement {

    private final Statement expression;
    private final Statement whileStatement;
    private final int numWh;

    public doWhileStatement(Statement expression, Statement whileStatement, int numWh) {
        this.expression = expression;
        this.whileStatement = whileStatement;
        this.numWh = numWh;
    }

    @Override
    public String build() {
        String res = "met whileMN" + numWh + "\n";
        res += whileStatement.build();
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
            res += "jni" + mod + " " + expression.getR() + " ifMNWH" + numWh + "\n";
        }
        res += "goto whileMN" + numWh + "\n";
        res += "met ifMNWH" + numWh + "\n";
        return res;
    }

    @Override
    public int getR() {
        System.out.println("Warning: unsuported operation adrGet for whileStatement!");
        return 0;
    }

    @Override
    public void setR(int adrResult) {
        System.out.println("Warning: unsuported operation adrSet for whileStatement!");
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
                res += String.format("jneq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                break;
            case "!=":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jeq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                break;
            case "<":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jnlt%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                break;
            case ">":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jnrt%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                break;
            case "<=":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jnlt%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                res += String.format("jneq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                break;
            case ">=":
                res += lexpr.S1.build();
                res += lexpr.S2.build();
                res += String.format("jnrt%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                res += String.format("jneq%s %s %s ifMN%d", mod, lexpr.S1.getR(), lexpr.S2.getR(), numWh);
                break;
            case "||":
                if (lexpr.S1 instanceof LogicalExpression) {
                    LogicalExpression s1 = (LogicalExpression) lexpr.S1;
                    res += switchOp(s1);
                } else {
                    res += lexpr.S1.build();
                    res += String.format("jni%s %s ifMN%d", mod, lexpr.S1.getR(), numWh);
                }
                if (lexpr.S2 instanceof LogicalExpression) {
                    LogicalExpression s2 = (LogicalExpression) lexpr.S2;
                    res += switchOp(s2);
                } else {
                    res += lexpr.S2.build();
                    res += String.format("jni%s %s ifMN%d", mod, lexpr.S2.getR(), numWh);
                }
                break;
            default:
                break;
        }
        return res;
    }

}
