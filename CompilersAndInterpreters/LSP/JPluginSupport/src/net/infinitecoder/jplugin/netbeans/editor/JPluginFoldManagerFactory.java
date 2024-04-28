package net.infinitecoder.jplugin.netbeans.editor;

import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.spi.editor.fold.FoldManager;
import org.netbeans.spi.editor.fold.FoldManagerFactory;

@MimeRegistration(mimeType = "text/x-jplugin", service = FoldManagerFactory.class)
public class JPluginFoldManagerFactory implements FoldManagerFactory {

    @Override
    public FoldManager createFoldManager() {
        return new JPluginFoldManager();
    }
}