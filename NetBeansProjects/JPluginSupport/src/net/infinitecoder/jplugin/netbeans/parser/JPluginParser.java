package net.infinitecoder.jplugin.netbeans.parser;

import javax.swing.event.ChangeListener;
import net.infinitecoder.jplugin.JPlugin;
import org.netbeans.modules.parsing.api.Snapshot;
import org.netbeans.modules.parsing.api.Task;
import org.netbeans.modules.parsing.spi.ParseException;
import org.netbeans.modules.parsing.spi.SourceModificationEvent;
import org.openide.util.Exceptions;

public class JPluginParser extends org.netbeans.modules.parsing.spi.Parser {

    private Snapshot snapshot;
    private JPlugin JP;

    @Override
    public void parse(Snapshot snapshot, Task task, SourceModificationEvent sme) throws ParseException {
        this.snapshot = snapshot;
        JP = new JPlugin(snapshot.getSource().getFileObject().getPath(), snapshot.getText().toString());
        try {
            JP.parse();
        } catch (RuntimeException re) {
            Exceptions.printStackTrace(re);
        }
    }

    @Override
    public Result getResult(Task task) throws ParseException {
        return new JPluginParserResult(snapshot, JP);
    }

    @Override
    public void addChangeListener(ChangeListener cl) {
    }

    @Override
    public void removeChangeListener(ChangeListener cl) {
    }

}
