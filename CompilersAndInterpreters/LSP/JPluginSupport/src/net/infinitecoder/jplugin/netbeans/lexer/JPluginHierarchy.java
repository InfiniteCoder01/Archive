package net.infinitecoder.jplugin.netbeans.lexer;

import java.util.Collection;
import java.util.EnumSet;
import org.netbeans.spi.lexer.LanguageHierarchy;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;

public class JPluginHierarchy extends LanguageHierarchy<TokenType> {

    @Override
    protected Collection<TokenType> createTokenIds() {
        return EnumSet.allOf(TokenType.class);
    }

    @Override
    protected Lexer<TokenType> createLexer(LexerRestartInfo<TokenType> info) {
        return new JPluginLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-jplugin";
    }

}
