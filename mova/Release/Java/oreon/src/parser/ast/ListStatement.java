package parser.ast;

import java.util.ArrayList;
import java.util.List;

public class ListStatement implements Statement{
    private List<Statement> statements;

    public ListStatement() {
        this.statements = new ArrayList<>();
    }
    
    public void add(Statement statement){
        statements.add(statement);
    }
    
    public Statement get(int pos){
        return statements.get(pos);
    }
    
    public int size(){
        return statements.size();
    }
    
    @Override
    public String build() {
        final StringBuilder result = new StringBuilder();
        statements.forEach((Statement statement) -> {
            result.append(statement.build());
        });
        return result.toString();
    }

    @Override
    public int getR() {
        System.out.println("Warning: unsuported operation adrGet for listStatement!");
        return 0;
    }

    @Override
    public void setR(int adrResult) {
        System.out.println("Warning: unsuported operation adrSet for listStatement!");
    }

    @Override
    public String toString() {
        return "ListStatement{" + "statements=" + statements + '}';
    }
    
}
