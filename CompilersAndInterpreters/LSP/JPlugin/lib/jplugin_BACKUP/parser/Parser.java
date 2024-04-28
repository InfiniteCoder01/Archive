package net.infinitecoder.jplugin.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.util.Pair;
import net.infinitecoder.jplugin.*;
import net.infinitecoder.jplugin.parser.expressions.*;
import net.infinitecoder.jplugin.parser.statements.*;
import net.infinitecoder.jplugin.parser.values.*;

public class Parser {

    public final List<Token> tokens;
    public final List<ParsingError> errors;
    public final Map<String, Pair<String, Long>> variablePool;
    public long pointer;
    private int index = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
        this.errors = new ArrayList<>();
        this.variablePool = new HashMap<>();
    }

    public void parse() {
        statement().execute();
        System.out.println(expression().eval());
    }

    public Statement statement() {
        if (match(TokenType.OPERATOR, "{")) {
            return blockStatement(false);
        }
        if (match(TokenType.WORD, "return")) {
            Statement st = new ReturnStatement(expression());
            consume(TokenType.OPERATOR, ";");
            return st;
        }
        if (match(TokenType.WORD, "throw")) {
            Statement st = new ThrowStatement(expression());
            consume(TokenType.OPERATOR, ";");
            return st;
        }
//        if (match(TokenType.WORD, "class") || match(TokenType.WORD, "struct")) {
//            String name = consume(TokenType.WORD).getValue();
//            List<String> supers = new ArrayList<>();
//            if (match(TokenType.WORD, "extends")) {
//                supers.add(parseType());
//                while (match(TokenType.OPERATOR, ",")) {
//                    supers.add(parseType());
//                }
//            }
//            Statement body = statement();
//            if (!(body instanceof BlockStatement)) {
//                throw new RuntimeException("Invalid class body!");
//            }
//            Statement st = new ClassRegisterStatement(name, body, supers);
//            consume(TokenType.OPERATOR, ";");
//            return st;
//        }
//        if (match(TokenType.WORD, "typedef")) {
//            activeVariables.defTypes.put(consume(TokenType.WORD).getValue(), consume(TokenType.WORD).getValue());
//            consume(TokenType.OPERATOR, ";");
//            return null;
//        }
        if (match(TokenType.WORD, "if")) {
            consume(TokenType.OPERATOR, "(");
            Expression ex = expression();
            consume(TokenType.OPERATOR, ")");
            Statement ifStatement = statement();
            if (match(TokenType.WORD, "else")) {
                Statement elseStatement = statement();
                return new IfElseStatement(ex, ifStatement, elseStatement);
            }
            return new IfElseStatement(ex, ifStatement);
        }
        if (match(TokenType.WORD, "while")) {
            consume(TokenType.OPERATOR, "(");
            Expression ex = expression();
            consume(TokenType.OPERATOR, ")");
            Statement statement = statement();
            return new WhileStatement(ex, statement);
        }
        if (match(TokenType.WORD, "do")) {
            Statement statement = statement();
            consume(TokenType.WORD, "while");
            consume(TokenType.OPERATOR, "(");
            Expression ex = expression();
            consume(TokenType.OPERATOR, ")");
            consume(TokenType.OPERATOR, ";");
            return new DoWhileStatement(ex, statement);
        }
        if (match(TokenType.WORD, "for")) {
            consume(TokenType.OPERATOR, "(");
            Statement init = statement();
            Expression ex = expression();
            consume(TokenType.OPERATOR, ";");
            Statement tick = statement();
            consume(TokenType.OPERATOR, ")");
            BlockStatement bs = new BlockStatement();
            BlockStatement body = new BlockStatement();
            body.add(statement());
            bs.add(init);
            bs.add(new ForStatement(ex, body, tick));
            return bs;
        }
//        if (match(TokenType.WORD, "enum")) {
//            String name = consume(TokenType.WORD).getValue();
//            String type = "int";
//            if (match(TokenType.OPERATOR, ":")) {
//                type = parseType();
//            }
//            List<String> names = new ArrayList<>();
//            List<Expression> values = new ArrayList<>();
//            consume(TokenType.OPERATOR, "{");
//            int i = 0;
//            while (!match(TokenType.OPERATOR, "}")) {
//                names.add(consume(TokenType.WORD).getValue());
//                Expression value = new ValueExpression(new IntValue(0));
//                if (i > 0) {
//                    value = new BinaryExpression(values.get(i - 1), new ValueExpression(new IntValue(1)), "+");
//                }
//                if (match(TokenType.OPERATOR, "=")) {
//                    value = expression();
//                }
//                values.add(value);
//                match(TokenType.OPERATOR, ",");
//                i++;
//            }
//            activeVariables.defTypes.put(name, type);
//            consume(TokenType.OPERATOR, ";");
//            return new ObjectCreationStatement(type, names, values);
//        }
        if (match(TokenType.WORD, "break")) {
            consume(TokenType.OPERATOR, ";");
            return new BreakStatement();
        }
        if (match(TokenType.WORD, "continue")) {
            consume(TokenType.OPERATOR, ";");
            return new ContinueStatement();
        }
        if (match(TokenType.WORD, "switch")) {
            consume(TokenType.OPERATOR, "(");
            Expression ex = expression();
            consume(TokenType.OPERATOR, ")");
            SwitchStatement st = new SwitchStatement(ex);
            consume(TokenType.OPERATOR, "{");
            while (!match(TokenType.OPERATOR, "}")) {
                if (match(TokenType.WORD, "case")) {
                    Expression caseValue = expression();
                    consume(TokenType.OPERATOR, ":");
                    BlockStatement bs = new BlockStatement();
                    while (!lookMatch(0, TokenType.WORD, "case") && !lookMatch(0, TokenType.WORD, "default") && !lookMatch(0, TokenType.OPERATOR, "}")) {
                        bs.add(statement());
                    }
                    st.add(caseValue, bs);
                }
                if (match(TokenType.WORD, "default")) {
                    consume(TokenType.OPERATOR, ":");
                    BlockStatement bs = new BlockStatement();
                    while (!lookMatch(0, TokenType.WORD, "case") && !lookMatch(0, TokenType.OPERATOR, "}")) {
                        bs.add(statement());
                    }
                    st.defaultCase = bs;
                }
            }
            return st;
        }
        if (match(TokenType.WORD, "try")) {
            TryCatchStatement st = new TryCatchStatement(statement());
            while (match(TokenType.WORD, "catch")) {
                consume(TokenType.OPERATOR, "(");
                ObjectCreationStatement ocs = (ObjectCreationStatement) statement();
                consume(TokenType.OPERATOR, ")");
                st.add(ocs, statement());
            }
            return st;
        }
//        if (lookMatch(0, TokenType.WORD)) {
//            int startIndex = index;
//            String name = consume(TokenType.WORD).getValue();
//            if (match(TokenType.OPERATOR, ".")) {
//                index--;
//                List<String> classnames = new ArrayList<>();
//                classnames.add(name);
//                if (lookMatch(0, TokenType.OPERATOR, ".")) {
//                    while (match(TokenType.OPERATOR, ".")) {
//                        classnames.add(consume(TokenType.WORD).getValue());
//                    }
//                } else if (lookMatch(0, TokenType.OPERATOR, "[")) {
//                    List<Expression> indexes = new ArrayList<>();
//                    while (match(TokenType.OPERATOR, "[")) {
//                        indexes.add(expression());
//                        consume(TokenType.OPERATOR, "]");
//                    }
//                    if (!match(TokenType.OPERATOR, "=")) {
//                        String operator = consume(TokenType.OPERATOR).getValue();
//                        consume(TokenType.OPERATOR, "=");
//                        Expression value = expression();
//                        index = startIndex;
//                        Statement st = new ClassFieldArrayAssignmentStatement(indexes, classnames.get(0), name, new BinaryExpression(postfix(), value, operator));
//                        consume(TokenType.OPERATOR, operator);
//                        consume(TokenType.OPERATOR, "=");
//                        expression();
//                        match(TokenType.OPERATOR, ";");
//                        return st;
//                    }
//                    Expression value = expression();
//                    match(TokenType.OPERATOR, ";");
//                    return new ClassFieldArrayAssignmentStatement(indexes, classnames.get(0), name, value);
//                }
//                if (!match(TokenType.OPERATOR, "=")) {
//                    index = startIndex;
//                    Statement st = new ExpressionStatement(expression());
//                    match(TokenType.OPERATOR, ";");
//                    return st;
//                }
//                Expression ex = expression();
//                match(TokenType.OPERATOR, ";");
//                return new ClassFieldAssignmentStatement(classnames.subList(0, classnames.size() - 1), classnames.get(classnames.size() - 1), ex);
//            } else if (match(TokenType.OPERATOR, "=")) {
//                Expression value = expression();
//                match(TokenType.OPERATOR, ";");
//                return new AssignmentStatement(name, value);
//            } else if (lookMatch(0, TokenType.OPERATOR) && lookMatch(1, TokenType.OPERATOR, "=")) {
//                String operator = consume(TokenType.OPERATOR).getValue();
//                consume(TokenType.OPERATOR, "=");
//                Expression value = expression();
//                match(TokenType.OPERATOR, ";");
//                return new AssignmentStatement(name, new BinaryExpression(new VariableExpression(name), value, operator));
//            } else if (lookMatch(0, TokenType.OPERATOR, "[")) {
//                int startingIndex = index - 1;
//                List<Expression> indexes = new ArrayList<>();
//                while (match(TokenType.OPERATOR, "[")) {
//                    indexes.add(expression());
//                    consume(TokenType.OPERATOR, "]");
//                }
//                if (!match(TokenType.OPERATOR, "=")) {
//                    String operator = consume(TokenType.OPERATOR).getValue();
//                    consume(TokenType.OPERATOR, "=");
//                    Expression value = expression();
//                    index = startingIndex;
//                    Statement st = new ArrayAssignmentStatement(indexes, name, new BinaryExpression(postfix(), value, operator));
//                    consume(TokenType.OPERATOR, operator);
//                    consume(TokenType.OPERATOR, "=");
//                    expression();
//                    match(TokenType.OPERATOR, ";");
//                    return st;
//                }
//                Expression value = expression();
//                match(TokenType.OPERATOR, ";");
//                return new ArrayAssignmentStatement(indexes, name, value);
//            } else if (lookMatch(0, TokenType.WORD) || lookMatch(0, TokenType.OPERATOR, "::")) {
//                List<String> names = new ArrayList<>();
//                List<Expression> values = new ArrayList<>();
//                index--;
//                String type = parseType();
//                name = consume(TokenType.WORD).getValue();
//                if (name.equals("operator")) {
//                    name += consume(TokenType.OPERATOR).getValue();
//                }
//                names.add(name);
//                if (lookMatch(0, TokenType.OPERATOR, "[")) {
//                    List<Expression> sizes = new ArrayList<>();
//                    while (match(TokenType.OPERATOR, "[")) {
//                        sizes.add(expression());
//                        consume(TokenType.OPERATOR, "]");
//                    }
//                    values.add(new ArrayExpression(sizes, 0));
//                    match(TokenType.OPERATOR, ";");
//                    return new ObjectCreationStatement(type, names, values);
//                }
//                Expression value = new ValueExpression(new IntValue(0));
//                if (match(TokenType.OPERATOR, "=")) {
//                    value = expression();
//                } else if (match(TokenType.OPERATOR, "(")) {
//                    List<String> arglist = new ArrayList<>();
//                    while (!match(TokenType.OPERATOR, ")")) {
//                        arglist.add(parseType()
//                                + " " + consume(TokenType.WORD).getValue()
//                        );
//                        match(TokenType.OPERATOR, ",");
//                    }
//                    return new FunctionRegisterStatement(type, name, arglist, statement());
//                }
//                values.add(value);
//                while (match(TokenType.OPERATOR, ",")) {
//                    names.add(consume(TokenType.WORD).getValue());
//                    if (match(TokenType.OPERATOR, "=")) {
//                        values.add(expression());
//                    } else {
//                        values.add(new ValueExpression(new IntValue(0)));
//                    }
//                }
//                match(TokenType.OPERATOR, ";");
//                return new ObjectCreationStatement(type, names, values);
//            } else {
//                index--;
//            }
//        }
        if (lookMatch(0, TokenType.WORD) && lookMatch(1, TokenType.WORD)) {
            //Object creation
            String type = consume(TokenType.WORD).getValue();
            variablePool.put(consume(TokenType.WORD).getValue(), new Pair<>(type, pointer));
            pointer++;
        }
        Statement st = new ExpressionStatement(expression());
        match(TokenType.OPERATOR, ";");
        return st;
    }

    private Statement blockStatement(boolean unclosing) {
        Token t = null;
        if (!unclosing) {
            t = peek(-1);
        }
        BlockStatement st = new BlockStatement();
        while (!match(TokenType.OPERATOR, "}")) {
            st.add(statement());
            if (match(TokenType.EOF)) {
                if (!unclosing) {
                    errors.add(new ParsingError(t, "EOF met, but '}' expected!"));
                }
                break;
            }
        }
        return st;
    }

    /*
     _______   ______________ _____ _____ _____ _____ _____ _   _ 
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
            return new BinaryExpression(new ValueExpression(new IntValue(0)), postfix(), "+");
        }
        if (match(TokenType.OPERATOR, "-")) {
            return new BinaryExpression(new ValueExpression(new IntValue(0)), postfix(), "-");
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
        if (match(TokenType.OPERATOR, "(")) {
            int startIndex = index - 1;
            try {
                String type = parseType();
                consume(TokenType.OPERATOR, ")");
                return new CastExpression(postfix(), type);
            } catch (RuntimeException re) {
                index = startIndex;
            }
        }
        if (match(TokenType.WORD, "sizeof")) {
            consume(TokenType.OPERATOR, "(");
            Expression value = expression();
            consume(TokenType.OPERATOR, ")");
            return new SizeOfExpression(value);
        }
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
        if(lookMatch(0, TokenType.WORD)) {
            return new VariableExpression(variablePool.get(consume(TokenType.WORD).getValue()));
        }
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
                return new LongValue(Long.parseLong(number));
            } catch (NumberFormatException ex) {
                return new DoubleValue(Double.parseDouble(number));
            }
        }
        if (lookMatch(0, TokenType.STRING)) {
            return new StringValue(consume(TokenType.STRING).getValue());
        }
        if (lookMatch(0, TokenType.CHAR)) {
            return new CharValue((byte) consume(TokenType.CHAR).getValue().charAt(0));
        }
        errors.add(new ParsingError(peek(0), "Unknown value \"" + peek(0).getValue() + "\""));
        index++;
        return new IntValue(0);
    }
    //TODO: helping
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
//        errors.add(new ParsingError(peek(0).getLine(), new RuntimeException("Error finded " + peek(0).getValue() + ", expected " + val)));
        return null;
    }

    private Token consume(TokenType type) {
        if (peek(0).getType() == type) {
            index++;
            return peek(-1);
        }
//        errors.add(new ParsingError(peek(0).getLine(), new RuntimeException("Error finded " + peek(0).getType() + ", expected " + type)));
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

    private String parseType() {
        String type = consume(TokenType.WORD).getValue();
        while (match(TokenType.OPERATOR, "::")) {
            type += "::" + consume(TokenType.WORD).getValue();
        }
        return type;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<ParsingError> getErrors() {
        return errors;
    }
}
