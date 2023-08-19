package net.infinitecoder.jplugin.netbeans.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import net.infinitecoder.jplugin.JPlugin;
import net.infinitecoder.jplugin.ParseError;
import org.netbeans.api.io.IOProvider;
import org.netbeans.api.io.InputOutput;
import org.netbeans.api.io.OutputColor;
import org.openide.LifecycleManager;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.loaders.DataObject;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.util.io.ReaderInputStream;

@ActionID(
        category = "Build",
        id = "net.infinitecoder.jplugin.netbeans.actions.RunAction"
)
@ActionRegistration(
        displayName = "#CTL_RunAction"
)
@ActionReferences({
    @ActionReference(path = "Loaders/text/x-jplugin/Actions", position = 0, separatorAfter = 50),
    @ActionReference(path = "Editors/text/x-jplugin/Popup", position = 400, separatorAfter = 450)
})
@Messages("CTL_RunAction=Run file")
public final class RunAction implements ActionListener {

    private final DataObject context;

    public RunAction(DataObject context) {
        this.context = context;
    }

    @Override public void actionPerformed(ActionEvent ev) {
        LifecycleManager.getDefault().saveAll();
        final InputOutput IO = IOProvider.getDefault()
                .getIO(context.getName(), true);
        prepareIO(IO);
        runProgram(IO);
        System.setIn(System.in);
        System.setOut(System.out);
        System.setErr(System.err);
    }

    private void prepareIO(InputOutput IO) {
        try {
            System.setIn(new ReaderInputStream(IO.getIn()));
            System.setOut(new WriterPrintStream(IO.getOut()));
            System.setErr(new WriterPrintStream(IO.getErr()));
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void runProgram(InputOutput IO) {
        JPlugin plugin = new JPlugin(context.getPrimaryFile().getPath());
        plugin.createFunction("print", "void", JPlugin.printFunction);
        plugin.createFunction("println", "void", JPlugin.printlnFunction);
        plugin.parse();
        if (plugin.getParseErrors().isEmpty()) {
            try {
                plugin.execute();
            } catch (Exception | Error e) {
                IO.getErr().println("Unknown execution error: " + e, OutputColor.failure());
                IO.getErr().println("BUILD FAILED WITH AN EXCEPTION!", OutputColor.failure());
                return;
            }
            IO.getOut().println("BUILD SUCSESS!", OutputColor.success());
        } else {
            for (ParseError pe : plugin.getParseErrors()) {
                IO.getErr().println("At line: " + pe.getLine() + ", " + pe, OutputColor.failure());
            }
            IO.getErr().println("BUILD FAILED WITH AN EXCEPTION!", OutputColor.failure());
        }
    }
}
