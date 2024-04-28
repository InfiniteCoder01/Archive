package net.infinitecoder.jplugin.netbeans.completion;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.annotations.common.StaticResource;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;

public class JPluginCompletionItem implements CompletionItem {

    @StaticResource
    private static final String JPLUGIN_ICON_PATH = "net/infinitecoder/jplugin/netbeans/filetype/JPlugin.png";

    private static final ImageIcon ICON;
    private static final Color FIELD_COLOR = Color.decode("0x0000B2");

    static {
        ICON = ImageUtilities.loadImageIcon(JPLUGIN_ICON_PATH, false);
    }

    private final String text;
    private final ItemType type;
    private final int dotOffset;
    private final int caretOffset;

    public JPluginCompletionItem(String text, ItemType type, int dotOffset, int caretOffset) {
        this.text = text;
        this.type = type;
        this.dotOffset = dotOffset;
        this.caretOffset = caretOffset;
    }

    @Override
    public void defaultAction(JTextComponent component) {
        try {
            StyledDocument doc = (StyledDocument) component.getDocument();
            doc.remove(dotOffset, caretOffset - dotOffset);
            doc.insertString(dotOffset, formatItemText(), null);
            Completion.get().hideAll();
        } catch (BadLocationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void processKeyEvent(KeyEvent evt) {
    }

    @Override
    public int getPreferredWidth(Graphics g, Font defaultFont) {
        return CompletionUtilities.getPreferredWidth(formatItemText(), null, g, defaultFont);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor,
            Color backgroundColor, int width, int height, boolean selected) {
        CompletionUtilities.renderHtml(ICON,
                formatItemText(),
                null,
                g,
                defaultFont,
                selected ? Color.white : FIELD_COLOR,
                width,
                height,
                selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return null;
    }

    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
        return text;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return text;
    }

    String formatItemText() {
        switch (type) {
            case FUNCTION:
                return text + "();";
            default:
                return text;
        }
    }

    public enum ItemType {
        FUNCTION,
        VARIABLE,
        KEYWORD
    }
}
