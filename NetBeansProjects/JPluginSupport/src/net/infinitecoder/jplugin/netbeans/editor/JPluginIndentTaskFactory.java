package net.infinitecoder.jplugin.netbeans.editor;

import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.editor.indent.spi.Context;
import org.netbeans.modules.editor.indent.spi.IndentTask;

@MimeRegistration(mimeType = "text/x-jplugin", service = IndentTask.Factory.class)
public class JPluginIndentTaskFactory implements IndentTask.Factory {

    @Override
    public IndentTask createTask(Context context) {
        return new JPluginIndentTask(context);
    }
}
