import ast.Program;

public class Main {
    public static void main(String[] args) {
        Parser parser = new Parser(new Lexer("test.rs"));
        Program program = parser.parse();
        program.optimize();
//        System.out.println(program);
        System.out.println(program.execute());
    }
}