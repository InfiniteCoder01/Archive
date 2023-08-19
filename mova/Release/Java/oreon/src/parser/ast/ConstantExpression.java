package parser.ast;
public class ConstantExpression implements Statement{

    final int constant;

    public ConstantExpression(int constant) {
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
        System.out.println("Warning: unsuported operation adrSet for constantExpression!");
    }

    @Override
    public String toString() {
        return "constant : " + constant;
    }
    
}
