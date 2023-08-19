package net.infinitecoder.jplugin.netbeans.editor;

import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.ReformatTask;

@MimeRegistration(mimeType = "text/x-jplugin", service = ReformatTask.Factory.class)
public class JPluginReformatTaskFactory implements ReformatTask.Factory {

    @Override
    public ReformatTask createTask(Context context) {
        return new JPluginReformatTask(context);
    }

}
