package parser.lexer;

public enum TokenType {
    NUMBER,
    HEX_NUMBER,
    WORD,
    TEXT,
    // keyword
    IF,
    ELSE,
    WHILE,
    FOR,
    DO,
    BREAK,
    GOTO,
    CONTINUE,
    IMPORT,
    DEF,
    INT,
    BYTE,
    FLOAT,
    RETURN,
    USE,
    SWITCH,
    CASE,
    DEFAULT,
    DEBUG,
    CLASS,
    NEW,
    PLUS, // +
    MINUS, // -
    STAR, // *
    SLASH, // /
    PERCENT,// %

    EQ, // =
    EQEQ, // ==
    EXCL, // !
    EXCLEQ, // !=
    LTEQ, // <=
    LT, // <
    GT, // >
    GTEQ, // >=

    PLUSEQ, // +=
    MINUSEQ, // -=
    STAREQ, // *=
    SLASHEQ, // /=
    PERCENTEQ, // %=
    AMPEQ, // &=
    CARETEQ, // ^=
    BAREQ, // |=
    LTLTEQ, // <<=
    GTGTEQ, // >>=

    PLUSPLUS, // ++
    MINUSMINUS, // --

    LTLT, // <<
    GTGT, // >>

    TILDE, // ~
    BAR, // |
    BARBAR, // ||
    AMP, // &
    AMPAMP, // &&

    LPAREN, // (
    RPAREN, // )
    LBRACKET, // [
    RBRACKET, // ]
    LBRACE, // {
    RBRACE, // }
    COMMA, // ,
    DOT, // .

    EOF
}
