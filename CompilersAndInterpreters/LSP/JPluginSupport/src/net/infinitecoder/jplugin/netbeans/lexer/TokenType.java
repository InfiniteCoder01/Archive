package net.infinitecoder.jplugin.netbeans.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

public enum TokenType implements TokenId {
    WORD("word"),
    KEYWORD("keyword"),
    CHAR("char"),
    STRING("string"),
    NUMBER("number"),
    OPERATOR("operator"),
    COMMENT("comment"),
    WHITESPACE("whitespace"),
    EOF("whitespace");

    private final String category;

    private TokenType(String category) {
        this.category = category;
    }
    
    @Override
    public String primaryCategory() {
        return category;
    }
    
    public static Language<TokenType> getLanguage() {
        return new JPluginHierarchy().language();
    }
}