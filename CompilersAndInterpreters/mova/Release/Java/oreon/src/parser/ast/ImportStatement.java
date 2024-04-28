package parser.ast;

import parser.lexer.Token;

public class ImportStatement implements Statement {

    final String file;
    
    public ImportStatement(Token text) {
        file = text.getValue();
    }

    @Override
    public String build() {
        return "import " + file;
    }

    @Override
    public int getR() {
        System.out.println("Warning: unsuported operation adrGet for importStatement!");
        return 0;
    }

    @Override
    public void setR(int adrResult) {
        System.out.println("Warning: unsuported operation adrSet for importStatement!");
    }
    
}
