package parser.lexer;

import java.util.HashMap;
import java.util.Map;

public class Tokens {
    public static final Map<String, TokenType> OPERATORS;
    public static final Map<String, TokenType> KEYWORDS;

    static {
        OPERATORS = new HashMap<>();
        OPERATORS.put("+", TokenType.PLUS);
        OPERATORS.put("-", TokenType.MINUS);
        OPERATORS.put("*", TokenType.STAR);
        OPERATORS.put("/", TokenType.SLASH);
        OPERATORS.put("%", TokenType.PERCENT);
        OPERATORS.put("(", TokenType.LPAREN);
        OPERATORS.put(")", TokenType.RPAREN);
        OPERATORS.put("[", TokenType.LBRACKET);
        OPERATORS.put("]", TokenType.RBRACKET);
        OPERATORS.put("{", TokenType.LBRACE);
        OPERATORS.put("}", TokenType.RBRACE);
        OPERATORS.put("=", TokenType.EQ);
        OPERATORS.put("<", TokenType.LT);
        OPERATORS.put(">", TokenType.GT);
        OPERATORS.put(".", TokenType.DOT);
        OPERATORS.put(",", TokenType.COMMA);
        
        OPERATORS.put("~", TokenType.TILDE);
        OPERATORS.put("!", TokenType.EXCL);
        OPERATORS.put("&", TokenType.AMP);
        OPERATORS.put("|", TokenType.BAR);

        OPERATORS.put("==", TokenType.EQEQ);
        OPERATORS.put("!=", TokenType.EXCLEQ);
        OPERATORS.put("<=", TokenType.LTEQ);
        OPERATORS.put(">=", TokenType.GTEQ);

        OPERATORS.put("+=", TokenType.PLUSEQ);
        OPERATORS.put("-=", TokenType.MINUSEQ);
        OPERATORS.put("*=", TokenType.STAREQ);
        OPERATORS.put("/=", TokenType.SLASHEQ);
        OPERATORS.put("%=", TokenType.PERCENTEQ);
        OPERATORS.put("&=", TokenType.AMPEQ);
        OPERATORS.put("|=", TokenType.BAREQ);
        OPERATORS.put("<<=", TokenType.LTLTEQ);
        OPERATORS.put(">>=", TokenType.GTGTEQ);

        OPERATORS.put("++", TokenType.PLUSPLUS);
        OPERATORS.put("--", TokenType.MINUSMINUS);

        OPERATORS.put("&&", TokenType.AMPAMP);
        OPERATORS.put("||", TokenType.BARBAR);

        OPERATORS.put("<<", TokenType.LTLT);
        OPERATORS.put(">>", TokenType.GTGT);
    }

    static {
        KEYWORDS = new HashMap<>();
        KEYWORDS.put("if", TokenType.IF);
        KEYWORDS.put("else", TokenType.ELSE);
        KEYWORDS.put("switch", TokenType.SWITCH);
        KEYWORDS.put("case", TokenType.CASE);
        KEYWORDS.put("default", TokenType.DEFAULT);
        KEYWORDS.put("while", TokenType.WHILE);
        KEYWORDS.put("do", TokenType.DO);
        KEYWORDS.put("for", TokenType.FOR);
        KEYWORDS.put("goto", TokenType.GOTO);
        KEYWORDS.put("break", TokenType.BREAK);
        KEYWORDS.put("continue", TokenType.CONTINUE);
        
        KEYWORDS.put("import", TokenType.IMPORT);
        KEYWORDS.put("def", TokenType.DEF);
        KEYWORDS.put("int", TokenType.INT);
        KEYWORDS.put("byte", TokenType.BYTE);
        KEYWORDS.put("float", TokenType.FLOAT);
        KEYWORDS.put("return", TokenType.RETURN);
        KEYWORDS.put("use", TokenType.USE);
        KEYWORDS.put("class", TokenType.CLASS);
        KEYWORDS.put("new", TokenType.NEW);
        
        KEYWORDS.put("print", TokenType.DEBUG);
    }
}
