package parser;

import parser.ast.ImportStatement;
import java.util.ArrayList;
import parser.ast.*;
import parser.lexer.Token;
import parser.lexer.TokenType;
import java.util.List;

class CodeParser {

    private final List<Token> tokens;
    private final String lib = "..\\libs";
    private final int size;
    private final ListStatement result = new ListStatement();
    private int pos;
    private int nif = 0;
    private int numWh = 0;
    private List<String> vars;

    public CodeParser(List<Token> tokens) {
        this.tokens = tokens;
        this.size = tokens.size();
        vars = new ArrayList<>();
    }

    public ListStatement Parse() {
        while (!match(TokenType.EOF)) {
            Statement statement = statement();
            if (statement != null) {
                result.add(statement);
            }
        }
        return result;
    }

    private Statement statement() {
        if (match(TokenType.BYTE)) {
            vars.add(consume(TokenType.WORD).getValue());
            if (match(TokenType.EQ)) {
                Statement result = expression(vars.indexOf(get(-2).getValue()));
                if (result instanceof ConstantExpression) {
                    result = new SetStatement(vars.indexOf(get(-3).getValue()), result);
                }
                return result;
            }
        }
        if (match(TokenType.IMPORT)) {
            return new ImportStatement(consume(TokenType.TEXT));
        }
        if (match(TokenType.DEBUG)) {
            consume(TokenType.LPAREN);
            if (!vars.contains("__debug_expression__")) {
                vars.add("__debug_expression__");
            }
            Statement expr = expression(vars.indexOf("__debug_expression__"));
            consume(TokenType.RPAREN);
            if (expr instanceof debugText) {
                return new DebugStatement(expr.build());
            }
            if (expr instanceof SetStatement) {
                Statement expr1 = ((SetStatement) expr).getStatement();
                if (expr1 instanceof ConstantExpression) {
                    return new DebugStatement(((ConstantExpression) expr1).getR() + "");
                }
            }
            if (expr instanceof ConstantExpression) {
                return new DebugStatement(((ConstantExpression) expr).getR() + "");
            }
            return new DebugStatement(expr);
        }
        if (match(TokenType.IF)) {
            consume(TokenType.LPAREN);
            if (!vars.contains("if" + nts(nif))) {
                vars.add("if" + nts(nif));
            }
            Statement expr = expression(vars.indexOf("if" + nts(nif)));
            consume(TokenType.RPAREN);
            Statement ifStatement;
            Statement elseStatement = null;
            ifStatement = statementOrBlock();
            if (match(TokenType.ELSE)) {
                elseStatement = statementOrBlock();
            }
            nif++;
            return new IfStatement(expr, ifStatement, elseStatement, nif);
        }
        if (match(TokenType.SWITCH)) {
            throw new UnsupportedOperationException("Switch is not supported at this version!");
        }
        if (match(TokenType.WHILE)) {
            consume(TokenType.LPAREN);
            if (!vars.contains("while" + nts(numWh))) {
                vars.add("while" + nts(numWh));
            }
            Statement expr = expression(vars.indexOf("while" + nts(numWh)));
            numWh++;
            consume(TokenType.RPAREN);
            Statement whileStatement;
            whileStatement = statementOrBlock();
            nif++;
            return new WhileStatement(expr, whileStatement, numWh);
        }
        if (match(TokenType.DO)) {
            Statement whileStatement;
            whileStatement = statementOrBlock();
            consume(TokenType.WHILE);
            consume(TokenType.LPAREN);
            if (!vars.contains("while" + nts(numWh))) {
                vars.add("while" + nts(numWh));
            }
            Statement expr = expression(vars.indexOf("while" + nts(numWh)));
            numWh++;
            consume(TokenType.RPAREN);
            nif++;
            return new doWhileStatement(expr, whileStatement, numWh);
        }
        return assignmentStatement();
    }

    private Statement assignmentStatement() {
        if (match(TokenType.WORD) && match(TokenType.EQ)) {
            Statement result = expression(vars.indexOf(get(-2).getValue()));
            if (result instanceof ConstantExpression) {
                result = new SetStatement(vars.indexOf(get(-3).getValue()), result);
            }
            return result;
        }
        return null;
    }

    Token consume(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) {
            //throw new RuntimeException("Token " + current + " doesn't match " + type);
            throw new RuntimeException("Error : met " + current + " and expected " + type);
        }
        pos++;
        return current;
    }

    boolean match(TokenType type) {
        final Token current = get(0);
        if (type != current.getType()) {
            return false;
        }
        pos++;
        return true;
    }

    boolean lookMatch(int pos, TokenType type) {
        return get(pos).getType() == type;
    }

    Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) {
            return new Token(TokenType.EOF);
        }
        return tokens.get(position);
    }

    String nts(int n) {
        if (n == 0) {
            return "";
        }
        return n + "";
    }

    private Statement ParseList() {
        ListStatement result = new ListStatement();
        while (!match(TokenType.RBRACE)) {
            result.add(statement());
        }
        return result;
    }

    private Statement statementOrBlock() {
        if (match(TokenType.LBRACE)) {
            return ParseList();
        }
        return statement();
    }

    private Statement expression(int ValOut) {
        ExpressionThree exprt = new ExpressionThree(tokens, size, pos, ValOut, vars);
        Statement expr = exprt.expression(ValOut);
        pos = exprt.pos;
        vars = exprt.vars;
        return expr;
    }

}
