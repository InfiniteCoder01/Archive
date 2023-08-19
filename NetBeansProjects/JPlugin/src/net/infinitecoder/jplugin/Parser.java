package net.infinitecoder.jplugin;

import java.util.ArrayList;
import java.util.List;
import net.infinitecoder.jplugin.expressions.*;
import net.infinitecoder.jplugin.values.*;

public class Parser {

    private final List<ParsingError> errors;
    private final List<Token> tokens;
    private int index;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.errors = new ArrayList<>();
    }

    public void parse() {
        System.out.println(expression().eval());
    }

    /*_______   ______________ _____ _____ _____ _____ _____ _   _ 
    |  ___\ \ / /| ___ \ ___ \  ___/  ___/  ___|_   _|  _  | \ | |
    | |__  \ V / | |_/ / |_/ / |__ \ `--.\ `--.  | | | | | |  \| |
    |  __| /   \ |  __/|    /|  __| `--. \`--. \ | | | | | | . ` |
    | |___/ /^\ \| |   | |\ \| |___/\__/ /\__/ /_| |_\ \_/ / |\  |
    \____/\/   \/\_|   \_| \_\____/\____/\____/ \___/ \___/\_| \_/
     */
    public Expression expression() {
        return ternary();
    }

    private Expression ternary() {
        Expression ex = logical();
        if (match(TokenType.OPERATOR, "?")) {
            Expression trueCase = logical();
            consume(TokenType.OPERATOR, ":");
            Expression falseCase = logical();
            ex = new TernaryExpression(ex, trueCase, falseCase);
        }
        return ex;
    }

    private Expression logical() {
        Expression ex = bitwise();
        while (true) {
            if (match(TokenType.OPERATOR, "&&")) {
                ex = new BinaryExpression(ex, bitwise(), "&&");
                continue;
            }
            if (match(TokenType.OPERATOR, "||")) {
                ex = new BinaryExpression(ex, bitwise(), "||");
                continue;
            }
            break;
        }
        return ex;
    }

    private Expression bitwise() {
        Expression ex = comparison();
        while (true) {
            if (match(TokenType.OPERATOR, "&")) {
                ex = new BinaryExpression(ex, comparison(), "&");
                continue;
            }
            if (match(TokenType.OPERATOR, "|")) {
                ex = new BinaryExpression(ex, comparison(), "|");
                continue;
            }
            if (match(TokenType.OPERATOR, "^")) {
                ex = new BinaryExpression(ex, comparison(), "^");
                continue;
            }
            if (match(TokenType.OPERATOR, ">>")) {
                ex = new BinaryExpression(ex, comparison(), ">>");
                continue;
            }
            if (match(TokenType.OPERATOR, "<<")) {
                ex = new BinaryExpression(ex, comparison(), "<<");
                continue;
            }
            break;
        }
        return ex;
    }

    private Expression comparison() {
        Expression ex = aditive();
        while (true) {
            if (match(TokenType.OPERATOR, ">")) {
                ex = new BinaryExpression(ex, aditive(), ">");
                continue;
            }
            if (match(TokenType.OPERATOR, "<")) {
                ex = new BinaryExpression(ex, aditive(), "<");
                continue;
            }
            if (match(TokenType.OPERATOR, "==")) {
                ex = new BinaryExpression(ex, aditive(), "==");
                continue;
            }
            if (match(TokenType.OPERATOR, "!=")) {
                ex = new BinaryExpression(ex, aditive(), "!=");
                continue;
            }
            if (match(TokenType.OPERATOR, ">=")) {
                ex = new BinaryExpression(ex, aditive(), ">=");
                continue;
            }
            if (match(TokenType.OPERATOR, "<=")) {
                ex = new BinaryExpression(ex, aditive(), "<=");
                continue;
            }
            break;
        }
        return ex;
    }

    private Expression aditive() {
        Expression ex = multiplicatve();
        while (true) {
            if (match(TokenType.OPERATOR, "+")) {
                ex = new BinaryExpression(ex, multiplicatve(), "+");
                continue;
            }
            if (match(TokenType.OPERATOR, "-")) {
                ex = new BinaryExpression(ex, multiplicatve(), "-");
                continue;
            }
            break;
        }
        return ex;
    }

    private Expression multiplicatve() {
        Expression ex = unary();
        while (true) {
            if (match(TokenType.OPERATOR, "*")) {
                ex = new BinaryExpression(ex, unary(), "*");
                continue;
            }
            if (match(TokenType.OPERATOR, "/")) {
                ex = new BinaryExpression(ex, unary(), "/");
                continue;
            }
            if (match(TokenType.OPERATOR, "%")) {
                ex = new BinaryExpression(ex, unary(), "%");
                continue;
            }
            break;
        }
        return ex;
    }

    private Expression unary() {
        if (match(TokenType.OPERATOR, "+")) {
            return new BinaryExpression(new ValueExpression(new ByteValue((byte) 0)), postfix(), "+");
        }
        if (match(TokenType.OPERATOR, "-")) {
            return new BinaryExpression(new ValueExpression(new ByteValue((byte) 0)), postfix(), "-");
        }
        if (match(TokenType.OPERATOR, "++")) {
            return new UnaryExpression(postfix(), "++");
        }
        if (match(TokenType.OPERATOR, "--")) {
            return new UnaryExpression(postfix(), "--");
        }
        if (match(TokenType.OPERATOR, "!")) {
            return new UnaryExpression(postfix(), "!");
        }
        if (match(TokenType.OPERATOR, "~")) {
            return new UnaryExpression(postfix(), "~");
        }
//        if (match(TokenType.OPERATOR, "(")) {
//            int startIndex = index - 1;
//            try {
//                String type = parseType();
//                consume(TokenType.OPERATOR, ")");
//                return new CastExpression(postfix(), type);
//            } catch (RuntimeException re) {
//                index = startIndex;
//            }
//        }
//        if (match(TokenType.WORD, "sizeof")) {
//            consume(TokenType.OPERATOR, "(");
//            Expression value = expression();
//            consume(TokenType.OPERATOR, ")");
//            return new SizeOfExpression(value);
//        }
        return postfix();
    }

    private Expression postfix() {
        Expression ex = primary();
        while (true) {
            if (match(TokenType.OPERATOR, "++")) {
                ex = new PostfixExpression(ex, "++");
                continue;
            }
            if (match(TokenType.OPERATOR, "--")) {
                ex = new PostfixExpression(ex, "--");
                continue;
            }
            if (match(TokenType.OPERATOR, "[")) {
                Expression operatorIndex = expression();
                consume(TokenType.OPERATOR, "]");
                ex = new BinaryExpression(ex, operatorIndex, "[]");
                continue;
            }
            break;
        }
        return ex;
    }

    private Expression primary() {
        if (match(TokenType.OPERATOR, "(")) {
            Expression ex = expression();
            consume(TokenType.OPERATOR, ")");
            return ex;
        }
//        if(lookMatch(0, TokenType.WORD)) {
//            return new VariableExpression(variablePool.get(consume(TokenType.WORD).getValue()));
//        }
        /*
        if (match(TokenType.WORD, "new")) {
            if (lookMatch(0, TokenType.WORD)) {
                String name = parseType();
                consume(TokenType.OPERATOR, "(");
                List<Expression> args = new ArrayList<>();
                while (!match(TokenType.OPERATOR, ")")) {
                    args.add(expression());
                    match(TokenType.OPERATOR, ",");
                }
                return new ClassConstructorExpression(name, args);
            }
        }
        if (lookMatch(0, TokenType.WORD)) {
            if (lookMatch(1, TokenType.OPERATOR, ".")) {
                int startIndex = index;
                String classname = consume(TokenType.WORD).getValue();
                consume(TokenType.OPERATOR, ".");
                String name = consume(TokenType.WORD).getValue();
                if (match(TokenType.OPERATOR, "(")) {
                    List<Expression> args = new ArrayList<>();
                    while (!match(TokenType.OPERATOR, ")")) {
                        args.add(expression());
                        match(TokenType.OPERATOR, ",");
                    }
                    return new ClassFunctionExpression(classname, name, args);
                } else if (lookMatch(0, TokenType.OPERATOR, ".")) {
                    index = startIndex;
                    Expression ex = new VariableExpression(consume(TokenType.WORD).getValue());
                    while (match(TokenType.OPERATOR, ".")) {
                        String objectname = consume(TokenType.WORD).getValue();
                        ex = new ClassFieldAcsessExpression(ex, objectname);
                    }
                    return ex;
                }
                return new ClassFieldAcsessExpression(new VariableExpression(classname), name);
            }
            if (lookMatch(1, TokenType.OPERATOR, "(")) {
                String name = consume(TokenType.WORD).getValue();
                consume(TokenType.OPERATOR, "(");
                List<Expression> args = new ArrayList<>();
                while (!match(TokenType.OPERATOR, ")")) {
                    args.add(expression());
                    match(TokenType.OPERATOR, ",");
                }
                return new FunctionCallExpression(name, args);
            }
            return new VariableExpression(parseType());
        }
        if (match(TokenType.OPERATOR, "{")) {
            List<Expression> values = new ArrayList<>();
            while (!match(TokenType.OPERATOR, "}")) {
                values.add(expression());
                match(TokenType.OPERATOR, ",");
            }
            return new ArrayExpression(values);
        }*/
        return new ValueExpression(value());
    }

    private Value value() {
        if (lookMatch(0, TokenType.NUMBER)) {
            String number = consume(TokenType.NUMBER).getValue();
            try {
//                ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
//                buffer.putLong(Long.parseLong(number));
//                return new ByteValue(buffer.array());
                return new ByteValue((byte)Long.parseLong(number));
            } catch (NumberFormatException ex) {
                return new ByteValue((byte) Double.parseDouble(number));
            }
        }
        if (lookMatch(0, TokenType.STRING)) {
//            return new StringValue(consume(TokenType.STRING).getValue());
        }
        if (lookMatch(0, TokenType.CHAR)) {
//            return new CharValue((byte) consume(TokenType.CHAR).getValue().charAt(0));
        }
        errors.add(new ParsingError(peek(0), "Unknown value \"" + peek(0).getValue() + "\""));
        index++;
        return new ByteValue((byte) 0);
    }

    /*__  __  ____    __       ____    ______   __  __  ____      
     /\ \/\ \/\  _`\ /\ \     /\  _`\ /\__  _\ /\ \/\ \/\  _`\    
     \ \ \_\ \ \ \L\_\ \ \    \ \ \L\ \/_/\ \/ \ \ `\\ \ \ \L\_\  
      \ \  _  \ \  _\L\ \ \  __\ \ ,__/  \ \ \  \ \ , ` \ \ \L_L  
       \ \ \ \ \ \ \L\ \ \ \L\ \\ \ \/    \_\ \__\ \ \`\ \ \ \/, \
        \ \_\ \_\ \____/\ \____/ \ \_\    /\_____\\ \_\ \_\ \____/
         \/_/\/_/\/___/  \/___/   \/_/    \/_____/ \/_/\/_/\/___/ 
     */
    private boolean match(TokenType type) {
        if (peek(0).getType() != type) {
            return false;
        }
        index++;
        return true;
    }

    private boolean match(TokenType type, String val) {
        return match(new Token(type, val, peek(0).getLine(), peek(0).getCol()));
    }

    private boolean lookMatch(int relPos, TokenType type) {
        return peek(relPos).getType() == type;
    }

    private boolean lookMatch(int relPos, TokenType type, String val) {
        return peek(relPos).equals(new Token(type, val, peek(relPos).getLine(), peek(0).getCol()));
    }

    private Token consume(TokenType type, String val) {
        if (peek(0).equals(new Token(type, val, peek(0).getLine(), peek(0).getCol()))) {
            index++;
            return peek(-1);
        }
        errors.add(new ParsingError(peek(0), "Error: " + peek(0).getValue() + "met, but " + val + " expected"));
        return null;
    }

    private Token consume(TokenType type) {
        if (peek(0).getType() == type) {
            index++;
            return peek(-1);
        }
        errors.add(new ParsingError(peek(0), "Error: " + peek(0).getType() + "met, but " + type + " expected"));
        return null;
    }

    private boolean match(Token token) {
        if (!peek(0).equals(token)) {
            return false;
        }
        index++;
        return true;
    }

    private Token peek(int relPos) {
        int pos = index + relPos;
        if (pos >= tokens.size()) {
            return tokens.get(tokens.size() - 1);
        }
        return tokens.get(pos);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ParsingError> getErrors() {
        return errors;
    }
}
