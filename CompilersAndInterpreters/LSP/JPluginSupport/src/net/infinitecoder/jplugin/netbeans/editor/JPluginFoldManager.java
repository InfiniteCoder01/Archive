package net.infinitecoder.jplugin.netbeans.editor;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import net.infinitecoder.jplugin.netbeans.lexer.Lexer;
import net.infinitecoder.jplugin.netbeans.lexer.Token;
import org.netbeans.api.editor.fold.Fold;
import org.netbeans.api.editor.fold.FoldHierarchy;
import org.netbeans.api.editor.fold.FoldTemplate;
import org.netbeans.api.editor.fold.FoldType;
import org.netbeans.spi.editor.fold.FoldHierarchyTransaction;
import org.netbeans.spi.editor.fold.FoldManager;
import org.netbeans.spi.editor.fold.FoldOperation;

public class JPluginFoldManager implements FoldManager {

    private FoldOperation operation;

    @Override
    public void init(FoldOperation operation) {
        this.operation = operation;
    }

    @Override
    public void initFolds(FoldHierarchyTransaction transaction) {
        final FoldHierarchy hierarchy = operation.getHierarchy();
        Lexer lexer = new Lexer(hierarchy.getComponent().getText().replace("\r", ""));
        int offset = 0;
        List<SimpleEntry<String, SimpleEntry<Integer, Integer>>> folds = new ArrayList<>();
        Stack<Integer> braces = new Stack<>();
        while (lexer.index < lexer.code.length()) {
            final Token token = lexer.parseToken();
            switch (token.getType()) {
                case COMMENT:
                    if (token.getValue().startsWith("/*")) {
                        folds.add(new SimpleEntry<>("comment", new SimpleEntry<>(offset, offset + token.getValue().length())));
                    }
                    break;
                case OPERATOR:
                    if (token.getValue().equals("{")) {
                        folds.add(new SimpleEntry<>("brace", new SimpleEntry<>(offset, -1)));
                        braces.push(folds.size() - 1);
                    } else if (token.getValue().equals("}")) {
                        folds.get(braces.pop()).getValue().setValue(offset + 1);
                    }
                    break;
            }
            offset += token.getValue().length();
        }
        for (SimpleEntry<String, SimpleEntry<Integer, Integer>> fold : folds) {
            if (fold.getValue().getValue() != -1) {
                try {
                    switch (fold.getKey()) {
                        case "comment":
                            operation.addToHierarchy(
                                    FoldType.COMMENT,
                                    fold.getValue().getKey(), fold.getValue().getValue(),
                                    false,
                                    FoldTemplate.DEFAULT,
                                    "/* ... */",
                                    null, transaction);
                            break;
                        case "brace":
                            operation.addToHierarchy(
                                    FoldType.CODE_BLOCK,
                                    fold.getValue().getKey(), fold.getValue().getValue(),
                                    false,
                                    FoldTemplate.DEFAULT_BLOCK,
                                    "{ ... }",
                                    null, transaction);
                            break;
                    }
                } catch (BadLocationException ex) {
                }
            }
        }
    }

    @Override
    public void insertUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
        initFolds(transaction);
    }

    @Override
    public void removeUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
        initFolds(transaction);
    }

    @Override
    public void changedUpdate(DocumentEvent evt, FoldHierarchyTransaction transaction) {
        initFolds(transaction);
    }

    @Override
    public void removeEmptyNotify(Fold epmtyFold) {
    }

    @Override
    public void removeDamagedNotify(Fold damagedFold) {
    }

    @Override
    public void expandNotify(Fold expandedFold) {
    }

    @Override
    public void release() {
    }
}
