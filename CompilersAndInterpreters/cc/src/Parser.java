import ast.*;
import ast.expression.*;

import java.util.Stack;

public class Parser {
    private final Lexer lexer;
    private final Stack<BlockExpression> returnBlocks;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        returnBlocks = new Stack<>();
    }

    // Parsing
    private Type parseType() {
        return new Type(lexer.consume(TokenType.WORD)); // TODO: check for invalid types
    }

    private FunctionExpression parseFunction() { // TODO: function arguments
        lexer.consume("(", TokenType.OPERATOR);
        lexer.consume(")", TokenType.OPERATOR);
        return new FunctionExpression(parseExpression());
    }

    private Expression parseLiteral() {
        if (lexer.match("(", TokenType.OPERATOR)) {
            Expression expression = parseExpression();
            lexer.consume(")", TokenType.OPERATOR);
            return expression;
        }
        if (lexer.match("{", TokenType.OPERATOR)) {
            BlockExpression block = new BlockExpression();
            returnBlocks.push(block);
            while (!lexer.match("}", TokenType.OPERATOR)) {
                Expression statement = parseStatement();
                block.add(statement);
                if (!(statement instanceof BlockExpression)) lexer.consume(";", TokenType.OPERATOR);
            }
            returnBlocks.pop();
            block.findType();
            return block;
        } else if (lexer.match("fn", TokenType.WORD)) return parseFunction();
        if (lexer.peekToken().type == TokenType.NUMBER) {
            String value = lexer.consume(TokenType.NUMBER);
            return new ConstantExpression(value, new Type("i32")); // TODO: better type deduction
        }
        throw new RuntimeException(String.format("Unknown expression: %s!", lexer.peekToken()));
    }

    private Expression parseUnary(int precedence) { // TODO: precedence in [1;3) (https://en.cppreference.com/w/c/language/operator_precedence)
        if (precedence == 0) return parseLiteral();
        if (precedence == 2) {
            if (lexer.match("@", TokenType.OPERATOR)) return new CastExpression(parseType(), parseUnary(precedence));
        }
        Expression lhs = parseUnary(precedence - 1);
        return lhs;
    }

    private Expression parseExpression(int precedence) {
        if (precedence < 3) return parseUnary(precedence);
        Expression lhs = parseExpression(precedence - 1);
        if (precedence == 13) {
            while (lexer.match("?", TokenType.OPERATOR)) {
                Expression trueBranch = parseExpression(precedence - 1);
                lexer.consume(":", TokenType.OPERATOR);
                Expression falseBranch = parseExpression(precedence - 1);
                lhs = new TernaryExpression(lhs, trueBranch, falseBranch);
            }
            return lhs;
        }

        // Binary
        String[][] binaryOperators = {
                {"*", "/", "%"}, // 3
                {"+", "-"}, // 4
                {"<<", ">>"}, // 5
                {"<", ">", "<=", ">="}, // 6
                {"==", "!="}, // 7
                {"&"}, // 8
                {"^"}, // 9
                {"|"}, // 10
                {"&&"}, // 11
                {"||"}, // 12
                {}, // 13
                {"=", "+=", "-=", "*=", "/=", "%=", "<<=", ">>=", "&=", "^=", "|="}, // 14
                {","} // 15
        };

        while (true) {
            boolean operatorFound = false;
            for (String operator : binaryOperators[precedence - 3]) {
                if (lexer.match(operator, TokenType.OPERATOR)) {
                    lhs = new BinaryExpression(operator, lhs, parseExpression(precedence - 1));
                    operatorFound = true;
                    break;
                }
            }
            if (!operatorFound) break;
        }

        return lhs;
    }

    private Expression parseExpression() {
        return parseExpression(15);

    }

    private Expression parseStatement() {
        if (lexer.match("return", TokenType.WORD)) return new ReturnExpression(returnBlocks.peek(), parseExpression());
        return parseExpression();
    }

    private Variable parseVariable() {
        Type type = null; // Null here stands for auto, everywhere else null type means void
        if (!lexer.match("auto", TokenType.WORD)) type = parseType();
        Variable variable = new Variable(type, lexer.consume(TokenType.WORD));

        if (lexer.lookMatch("(", TokenType.OPERATOR)) variable.value = parseFunction();
        else if (lexer.match("=", TokenType.OPERATOR)) variable.value = parseExpression();
        lexer.consume(";", TokenType.OPERATOR);

        if (variable.type == null) {
            if (variable.value == null)
                throw new UnsupportedOperationException("Future type deduction is not supported yet."); // TODO: future types
            variable.type = variable.value.getType();
        } else if (variable.value != null) {
            if (!Type.castPossible(variable.value.getType(), variable.type)) {
                throw new RuntimeException(String.format("Can't assign value '%s' of type '%s' to variable '%s' of type '%s'.", variable.value, variable.value.getType(), variable.name, variable.type));
            }
        }

        return variable;
    }

    public Program parse() {
        Program program = new Program();
        while (true) {
            if (lexer.match(TokenType.EOF)) break;
            program.variables.add(parseVariable());
        }
        return program;
    }
}
