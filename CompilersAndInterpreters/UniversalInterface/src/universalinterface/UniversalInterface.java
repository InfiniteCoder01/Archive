package universalinterface;

import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;
import net.infinitecoder.jplugin.JPlugin;
import net.infinitecoder.jplugin.parser.statements.JCode;
import net.infinitecoder.jplugin.parser.values.*;

public class UniversalInterface {

    private static Map<String, JPlugin> plugins;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Unerversal Interface");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        JPanel panel = new JPanel(new FlowLayout());
        frame.add(panel);
        List<Long> components = new ArrayList<>();
        plugins = Util.loadAllPlugins();
        new Thread(() -> {
            JPlugin plugin = plugins.get("Default");
            plugin.createFunction("addButton", "void", new JCode("String name, long index") {
                @Override
                public Value execute(Value... args) {
                    String name = ((StringValue) args[0]).value;
                    long id = (int) ((LongValue) args[1]).value;
                    components.add(id);
                    panel.add(new JButton(name));
                    frame.pack();
                    return JPlugin.ZERO;
                }
            });
            plugin.createFunction("addTextField", "void", new JCode("String defaultText, long index") {
                @Override
                public Value execute(Value... args) {
                    String defaultText = ((StringValue) args[0]).value;
                    long id = (int) ((LongValue) args[1]).value;
                    components.add(id);
                    panel.add(new JTextField(defaultText));
                    frame.pack();
                    return JPlugin.ZERO;
                }
            });
            plugin.createFunction("addLabel", "void", new JCode("String text, long index") {
                @Override
                public Value execute(Value... args) {
                    String text = ((StringValue) args[0]).value;
                    long id = (int) ((LongValue) args[1]).value;
                    components.add(id);
                    panel.add(new JLabel(text));
                    frame.pack();
                    return JPlugin.ZERO;
                }
            });
            plugin.createFunction("getButtonState", "bool", new JCode("long id") {
                @Override
                public Value execute(Value... args) {
                    long id = ((LongValue) args[0]).value;
                    JButton b = (JButton) panel.getComponent(components.indexOf(id));
                    return new BoolValue((byte) (b.getModel().isPressed() ? 1 : 0));
                }
            });
            plugin.createFunction("getFieldText", "String", new JCode("long id") {
                @Override
                public Value execute(Value... args) {
                    long id = ((LongValue) args[0]).value;
                    JTextField f = (JTextField) panel.getComponent(components.indexOf(id));
                    return new StringValue(f.getText());
                }
            });
            plugin.createFunction("setFieldText", "void", new JCode("String text, long id") {
                @Override
                public Value execute(Value... args) {
                    String text = ((StringValue) args[0]).value;
                    long id = ((LongValue) args[1]).value;
                    JTextField f = (JTextField) panel.getComponent(components.indexOf(id));
                    f.setText(text);
                    return JPlugin.ZERO;
                }
            });
            plugin.createFunction("getLabelText", "String", new JCode("long id") {
                @Override
                public Value execute(Value... args) {
                    long id = ((LongValue) args[0]).value;
                    JLabel l = (JLabel) panel.getComponent(components.indexOf(id));
                    return new StringValue(l.getText());
                }
            });
            plugin.createFunction("setLabelText", "void", new JCode("String text, long id") {
                @Override
                public Value execute(Value... args) {
                    String text = ((StringValue) args[0]).value;
                    long id = ((LongValue) args[1]).value;
                    JLabel l = (JLabel) panel.getComponent(components.indexOf(id));
                    l.setText(text);
                    return JPlugin.ZERO;
                }
            });
            plugin.createFunction("setResolution", "void", new JCode("int w, int h") {
                @Override
                public Value execute(Value... args) {
                    int w = ((IntValue) args[0]).value;
                    int h = ((IntValue) args[1]).value;
                    frame.setSize(w, h);
                    return JPlugin.ZERO;
                }
            });
            plugin.createFunction("setTitle", "void", new JCode("String title") {
                @Override
                public Value execute(Value... args) {
                    String title = ((StringValue) args[0]).value;
                    frame.setTitle(title);
                    return JPlugin.ZERO;
                }
            });
            plugin.createFunction("clearWindow", "void", new JCode("") {
                @Override
                public Value execute(Value... args) {
                    for (int i = 0; i < components.size(); i++) {
                        panel.remove(i);
                    }
                    components.clear();
                    return JPlugin.ZERO;
                }
            });
            Util.parseAndRun(plugin, "Default", true);
        }
        ).start();
    }
}
