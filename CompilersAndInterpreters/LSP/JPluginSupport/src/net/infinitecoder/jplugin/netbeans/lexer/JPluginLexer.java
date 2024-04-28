package net.infinitecoder.jplugin.netbeans.lexer;

import org.netbeans.spi.lexer.LexerInput;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.spi.lexer.TokenFactory;

public class JPluginLexer implements org.netbeans.spi.lexer.Lexer<TokenType> {

    private final LexerRestartInfo<TokenType> info;
    private final TokenFactory<TokenType> tokenFactory;

    private Lexer lexer;
    private boolean isInitialized;

    public JPluginLexer(LexerRestartInfo<TokenType> info) {
        this.info = info;
        tokenFactory = info.tokenFactory();
        isInitialized = false;
    }

    @Override
    public org.netbeans.api.lexer.Token<TokenType> nextToken() {
        if (!isInitialized) {
            synchronized (this) {
                if (!isInitialized) {
                    lexer = new Lexer(readAll(info.input()));
                    isInitialized = true;
                }
            }
        }
        if (info.input().readLength() <= 0) {
            return null;
        }
        try {
            final Token token = lexer.parseToken();
            return tokenFactory.createToken(token.getType(), token.getValue().length());
        } catch (Exception ex) {
            return tokenFactory.createToken(TokenType.WHITESPACE, 1);
        }
    }

    @Override
    public Object state() {
        return null;
    }

    @Override
    public void release() {
        lexer = null;
        System.gc();
    }

    private String readAll(LexerInput input) {
        final StringBuilder sb = new StringBuilder();
        int i;
        while ((i = input.read()) != -1) {
            sb.append((char) i);
        }
        return sb.toString();
    }

}
