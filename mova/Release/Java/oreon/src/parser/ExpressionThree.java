package parser;

import parser.ast.LogicalExpression;
import parser.ast.debugText;
import java.util.List;
import parser.ast.*;
import parser.lexer.Token;
import parser.lexer.TokenType;

public class ExpressionThree {

    private final List<Token> tokens;
    private final String lib = "..\\libs";
    private final int size;
    public int pos;
    public int ValOut;
    private int nVal;
    List<String> vars;

    public ExpressionThree(List<Token> tokens, int size, int pos, int ValOut, List<String> vars) {
        this.tokens = tokens;
        this.size = size;
        this.pos = pos;
        this.ValOut = ValOut;
        this.vars = vars;
    }

    public Statement expression(int ValOut) {
        return aditive(ValOut);
    }

    private Statement aditive(int ValOut) {
        Statement result = multiplicative(ValOut);
        while (true) {
            if (match(TokenType.PLUS)) {
                result = new BinaryExpression(ValOut, "+", result, multiplicative(ValOut));
                continue;
            }
            if (match(TokenType.MINUS)) {
                result = new BinaryExpression(ValOut, "-", result, multiplicative(ValOut));
                continue;
            }
            break;
        }
        if (result.getR() != ValOut && result instanceof BinaryExpression) {
            result.setR(ValOut);
        }
        return result;
    }

    private Statement multiplicative(int ValOut) {
        Statement result = logical(ValOut);

        while (true) {
            if (match(TokenType.STAR)) {
                result = new BinaryExpression(ValOut + nVal, "*", result, logical(ValOut));
                nVal++;
                continue;
            }
            if (match(TokenType.SLASH)) {
                result = new BinaryExpression(ValOut + nVal, "/", result, logical(ValOut));
                nVal++;
                continue;
            }
            if (match(TokenType.PERCENT)) {
                result = new BinaryExpression(ValOut + nVal, "%", result, logical(ValOut));
                nVal++;
                continue;
            }
            break;
        }
        return result;
    }

    private Statement logical(int ValOut) {
        Statement result = objectCreation(ValOut);

        while (true) {
            if (match(TokenType.EQEQ)) {
                result = new LogicalExpression(ValOut + nVal, "==", result, objectCreation(ValOut));
                nVal++;
                continue;
            }
            if (match(TokenType.EXCLEQ)) {
                result = new LogicalExpression(ValOut + nVal, "!=", result, objectCreation(ValOut));
                nVal++;
                continue;
            }
            if (match(TokenType.LT)) {
                result = new LogicalExpression(ValOut + nVal, "<", result, objectCreation(ValOut));
                nVal++;
                continue;
            }
            if (match(TokenType.GT)) {
                result = new LogicalExpression(ValOut + nVal, ">", result, objectCreation(ValOut));
                nVal++;
                continue;
            }
            if (match(TokenType.LTEQ)) {
                result = new LogicalExpression(ValOut + nVal, "<=", result, objectCreation(ValOut));
                nVal++;
                continue;
            }
            if (match(TokenType.GTEQ)) {
                result = new LogicalExpression(ValOut + nVal, ">=", result, objectCreation(ValOut));
                nVal++;
                continue;
            }
            break;
        }
        return result;
    }

    private Statement objectCreation(int ValOut) {
//        Statement result1;
//        if (match(TokenType.NEW)) {
//            final String className = consume(TokenType.WORD).getValue();
//            final List<Statement> args = new ArrayList<>();
//            consume(TokenType.LPAREN);
//            while (!match(TokenType.RPAREN)) {
//                args.add(expression());
//                match(TokenType.COMMA);
//            }
//            result1 = 
//            result.add(result1);
//            return result1;
//        }

        return unary(ValOut);
    }

    private Statement unary(int ValOut) {
        Statement result;
        if (match(TokenType.PLUSPLUS)) {
            Statement prm = primary(ValOut);
            result = new BinaryExpression(ValOut + nVal, "+", prm, new ConstantExpression(1));
            return result;
        }
        if (match(TokenType.MINUSMINUS)) {
            result = new BinaryExpression(ValOut + nVal, "-", primary(ValOut), new ConstantExpression(1));
            nVal++;
            return result;
        }
        if (match(TokenType.MINUS)) {
            result = new ConstantExpression(-primary(ValOut).getR());
            nVal++;
            return result;
        }
        if (match(TokenType.EXCL)) {
            return new UnaryExpression(ValOut + nVal, "!", primary(ValOut));
        }
        if (match(TokenType.TILDE)) {
            return new UnaryExpression(ValOut + nVal, "~", primary(ValOut));
        }
        if (match(TokenType.PLUS)) {
            return primary(ValOut);
        }
        return primary(ValOut);
    }

    private Statement primary(int ValOut) {
        Statement result = null;
        if (match(TokenType.LPAREN)) {
            result = expression(ValOut + nVal);
            nVal++;
            consume(TokenType.RPAREN);
            return result;
        }
        if (get(0).getType() == TokenType.TEXT) {
            //result = new ConstantExpression("\"" + get(0).getValue() + "\"");
            System.out.println("Warning: unsuported operation textExpression!");
            return new debugText("\"" + consume(TokenType.TEXT).getValue() + "\"");
        } else if (get(0).getType() != TokenType.WORD) {
            if (get(0).getType() == TokenType.NUMBER) {
                result = new ConstantExpression(Integer.parseInt(get(0).getValue()));
            }
        } else {
            if (vars.contains(get(0).getValue())) {
                result = new VariableExpression(vars.indexOf(get(0).getValue()));
            } else {
                throw new RuntimeException("Error : variable " + get(0).getValue() + " doesn't exsist!");
            }
        }
        consume(get(0).getType());
        match(TokenType.PLUSPLUS);
        match(TokenType.MINUSMINUS);
        return result;
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

    Token get(int relativePosition) {
        final int position = pos + relativePosition;
        if (position >= size) {
            return new Token(TokenType.EOF);
        }
        return tokens.get(position);
    }
}
