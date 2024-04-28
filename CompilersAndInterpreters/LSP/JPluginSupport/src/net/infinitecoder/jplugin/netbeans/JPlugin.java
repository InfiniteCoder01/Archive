package net.infinitecoder.jplugin.netbeans;

import net.infinitecoder.jplugin.netbeans.lexer.TokenType;
import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

@LanguageRegistration(mimeType = "text/x-jplugin")
public class JPlugin extends DefaultLanguageConfig {

    @Override
    @SuppressWarnings("rawtypes")
    public Language getLexerLanguage() {
        return TokenType.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "JPlugin";
    }
}
