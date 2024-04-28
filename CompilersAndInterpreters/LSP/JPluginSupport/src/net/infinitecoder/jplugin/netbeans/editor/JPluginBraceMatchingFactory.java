package net.infinitecoder.jplugin.netbeans.editor;

import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.bracesmatching.BracesMatcher;
import org.netbeans.spi.editor.bracesmatching.BracesMatcherFactory;
import org.netbeans.spi.editor.bracesmatching.MatcherContext;
import org.netbeans.spi.editor.bracesmatching.support.BracesMatcherSupport;

@MimeRegistration(mimeType = "text/x-jplugin", service = BracesMatcherFactory.class)
public class JPluginBraceMatchingFactory implements BracesMatcherFactory {

    @Override
    public BracesMatcher createMatcher(MatcherContext mc) {
        return BracesMatcherSupport.characterMatcher(mc, -1, -1, '(', ')', '[', ']', '{', '}');
    }

}
