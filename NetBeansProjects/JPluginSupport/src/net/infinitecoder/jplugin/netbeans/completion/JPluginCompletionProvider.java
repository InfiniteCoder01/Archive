package net.infinitecoder.jplugin.netbeans.completion;

import java.util.ArrayList;
import java.util.List;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import net.infinitecoder.jplugin.JPlugin;
import net.infinitecoder.jplugin.netbeans.completion.JPluginCompletionItem.ItemType;
import net.infinitecoder.jplugin.netbeans.lexer.Lexer;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.completion.CompletionProvider;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;
import org.netbeans.spi.editor.completion.support.AsyncCompletionTask;
import org.openide.util.Exceptions;

@MimeRegistration(mimeType = "text/x-jplugin", service = CompletionProvider.class)
public class JPluginCompletionProvider implements CompletionProvider {

    static int getRowFirstNonWhitespace(StyledDocument doc, int offset) throws BadLocationException {
        Element element = doc.getParagraphElement(offset);
        int start = element.getStartOffset();
        while (start + 1 < element.getEndOffset()) {
            try {
                if (Character.isWhitespace(doc.getText(start, 1).charAt(0)) == false) {
                    break;
                }
            } catch (BadLocationException ex) {
                throw (BadLocationException) new BadLocationException("Calling getText("
                        + start + ", " + (start + 1) + ") on document of length "
                        + doc.getLength(), start)
                        .initCause(ex);
            }

            start++;
        }
        return start;
    }

    static int indexOfWhitespace(char line[]) {
        int i = line.length;
        while (--i > -1) {
            final char c = line[i];
            if (Character.isWhitespace(c) == true) {
                return i;
            }
        }
        return -1;
    }

    public List<JPluginCompletionItem> getProposals(JTextComponent component, String filter, int startOffset, int caretOffset) {
        final List<JPluginCompletionItem> proposals = new ArrayList<>();
        // identifiers
        final JPlugin jp = new JPlugin("null", component.getText());
        jp.parse();
        if (jp.statement != null) {
            Visitor visitor = new Visitor(jp.statement);
            for (String name : visitor.getFunctions()) {
                if (name.startsWith(filter)) {
                    proposals.add(new JPluginCompletionItem(name, ItemType.FUNCTION, startOffset, caretOffset));
                }
            }
            for (String name : visitor.getClasses()) {
                if (name.startsWith(filter)) {
                    proposals.add(new JPluginCompletionItem(name, ItemType.VARIABLE, startOffset, caretOffset));
                }
            }
            for (String name : visitor.getVariables()) {
                if (name.startsWith(filter)) {
                    proposals.add(new JPluginCompletionItem(name, ItemType.VARIABLE, startOffset, caretOffset));
                }
            }
        }

        // keywords
        for (String keyword : Lexer.keywords) {
            if (keyword.startsWith(filter)) {
                proposals.add(new JPluginCompletionItem(keyword, ItemType.KEYWORD, startOffset, caretOffset));
            }
        }
        return proposals;
    }

    @Override
    public CompletionTask createTask(int queryType, final JTextComponent component) {
        if (queryType != CompletionProvider.COMPLETION_QUERY_TYPE) {
            return null;
        }
        return new AsyncCompletionTask(new AsyncCompletionQuery() {
            @Override
            protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
                String filter = null;
                int startOffset = caretOffset - 1;
                try {
                    final StyledDocument styledDoc = (StyledDocument) doc;
                    final int lineStartOffset = getRowFirstNonWhitespace(styledDoc, caretOffset);
                    final char line[] = styledDoc.getText(lineStartOffset, caretOffset - lineStartOffset).toCharArray();
                    final int whiteSpaceOffset = indexOfWhitespace(line);
                    filter = String.valueOf(line, whiteSpaceOffset + 1, line.length - whiteSpaceOffset - 1);
                    if (whiteSpaceOffset > 0) {
                        startOffset = lineStartOffset + whiteSpaceOffset + 1;
                    } else {
                        startOffset = lineStartOffset;
                    }
                } catch (BadLocationException ex) {
                    Exceptions.printStackTrace(ex);
                }

                resultSet.addAllItems(getProposals(component, filter, startOffset, caretOffset));
                resultSet.finish();
            }
        }, component);
    }

    @Override
    public int getAutoQueryTypes(JTextComponent jtc, String string) {
        return 32;
    }

}
