package parser.ast;

public class debugText implements Statement {

    final String text;

    public debugText(String text) {
        this.text = text;
    }

    @Override
    public String build() {
        System.out.println("Warning: used debugText!");
        return text;
    }

    @Override
    public int getR() {
        System.out.println("Warning: unsuported operation adrGet for debugText!");
        return 0;
    }

    @Override
    public void setR(int adrResult) {
        System.out.println("Warning: unsuported operation adrSet for debugText!");
    }

}
