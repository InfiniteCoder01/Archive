package net.infinitecoder.jplugin.netbeans.parser;

import java.util.Collections;
import java.util.List;
import net.infinitecoder.jplugin.JPlugin;
import org.netbeans.modules.csl.api.Error;
import org.netbeans.modules.csl.spi.ParserResult;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.spi.ParseException;

public class JPluginParserResult extends ParserResult {

    private final JPlugin jp;
    private boolean isValid;

    public JPluginParserResult(Snapshot snapshot, JPlugin JP) {
        super(snapshot);
        jp = JP;
        isValid = true;
    }

    public JPlugin getParser() throws ParseException {
        if (!isValid) {
            throw new ParseException();
        }
        return jp;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<? extends Error> getDiagnostics() {
        return Collections.EMPTY_LIST;
    }

    @Override
    protected void invalidate() {
        isValid = false;
    }

}
