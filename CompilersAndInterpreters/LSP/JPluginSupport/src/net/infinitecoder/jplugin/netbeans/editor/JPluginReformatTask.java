package net.infinitecoder.jplugin.netbeans.editor;

import javax.swing.text.BadLocationException;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ExtraLock;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

public class JPluginReformatTask implements ReformatTask {

    private final Context context;
    
    public JPluginReformatTask(Context context) {
        this.context = context;
    }

    @Override
    public void reformat() throws BadLocationException {
        final int start = context.startOffset();
        final int end = context.endOffset();
        String input = context.document().getText(start, end);
        context.document().remove(start, end - start);
        context.document().insertString(start, CodeBeautifier.beautify(input), null);
    }

    @Override
    public ExtraLock reformatLock() {
        return null;
    }
}
