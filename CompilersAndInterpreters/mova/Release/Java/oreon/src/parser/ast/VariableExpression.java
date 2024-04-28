package parser.ast;
public class VariableExpression implements Statement{

    final int constant;

    public VariableExpression(int constant) {
        this.constant = constant;
    }
    
    @Override
    public String build() {
        return "";
    }

    @Override
    public int getR() {
        return constant;
    }

    @Override
    public void setR(int adrResult) {
        System.out.println("Warning: unsuported operation adrSet for variableExpression!");
    }
    
}
