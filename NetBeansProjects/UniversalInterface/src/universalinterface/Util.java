package universalinterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import net.infinitecoder.jplugin.JPlugin;
import net.infinitecoder.jplugin.ParsingError;
import net.infinitecoder.jplugin.parser.statements.JCode;
import net.infinitecoder.jplugin.parser.values.ArrayValue;
import net.infinitecoder.jplugin.parser.values.CharValue;
import net.infinitecoder.jplugin.parser.values.StringValue;
import net.infinitecoder.jplugin.parser.values.Value;

public class Util {

    public static JPlugin plugin;
    public static JCode readFileJCode = new JCode("String filename") {
        @Override
        public Value execute(Value... args) {
            return new StringValue(Util.readFile(
                    plugin.path.substring(0,
                                Math.min(plugin.path.lastIndexOf('/') == -1
                                        ? 1000 : plugin.path.lastIndexOf('/'),
                                        plugin.path.lastIndexOf('\\') == -1
                                        ? 1000 : plugin.path.lastIndexOf('\\'))),
                    ((StringValue) args[0]).value
            ));
        }
    };
    public static JCode readBinFileJCode = new JCode("String filename") {
        @Override
        public Value execute(Value... args) {
            try {
                byte[] bytes = Files.readAllBytes(
                        Paths.get(
                                combine(plugin.path, ((StringValue) args[0]).value)
                        ));
                CharValue[] chars = new CharValue[bytes.length];
                for (int i = 0; i < bytes.length; i++) {
                    chars[i] = new CharValue(bytes[i]);
                }
                return new ArrayValue(chars);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    };

    public static List<String> getPluginsAvaliable() {
        List<String> plugins = new ArrayList<>();
        String avaliablePlugins = readFile("plugins/plugins.txt");
        plugins.addAll(Arrays.asList(avaliablePlugins.split("\n")));
        return plugins;
    }

    public static String readFile(String filename) {
        try {
            Scanner s = new Scanner(new File(filename));
            String text = "";
            while (s.hasNextLine()) {
                text += s.nextLine() + "\n";
            }
            return text;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String readFile(String parent, String filename) {
            return readFile(new File(new File(parent), filename).getAbsolutePath());
    }

    public static Map<String, JPlugin> loadAvaliablePlugins(List<String> pluginFiles) {
        Map<String, JPlugin> plugins = new HashMap<>();
        for (String pluginFile : pluginFiles) {
            String config = readFile(pluginFile);
            String name = config.split("\n")[0];
            JPlugin jplugin = new JPlugin(config.split("\n")[1]);
            plugins.put(name, jplugin);
        }
        return plugins;
    }

    public static void initializeIO(JPlugin plugin) {
        plugin.createFunction("print", "void", JPlugin.printFunction);
        plugin.createFunction("println", "void", JPlugin.printlnFunction);
        plugin.createFunction("readFile", "String", readFileJCode);
        plugin.createFunction("readBinFile", "char", readBinFileJCode);
    }

    public static void showErrors(String name, List<ParsingError> errors) {
        for (ParsingError error : errors) {
            System.err.println("Compiling error in plugin \"" + name
                    + "\" at line: " + error.getLine() + ", " + error.getException());
        }
    }

    public static void parseAndRun(JPlugin plugin1, String name, boolean standartOutput) {
        plugin = plugin1;
        if (standartOutput) {
            initializeIO(plugin);
        }
        plugin.parse();
        if (!plugin.getParseErrors().isEmpty()) {
            showErrors(name, plugin.getParseErrors());
        } else {
            System.out.println("BUILD FINISH!");
//            System.out.println(plugin.getAST());
            plugin.execute();
        }
    }

    public static Map<String, JPlugin> loadAllPlugins() {
        Map<String, JPlugin> plugins;
        List<String> pluginFiles = getPluginsAvaliable();
        System.out.println("Plugins avaliable: " + pluginFiles);
        plugins = loadAvaliablePlugins(pluginFiles);
        System.out.println("Plugins loaded: " + plugins.keySet());
        return plugins;
    }
    
    public static String combine(String a, String b) {
        return new File(a.substring(0, Math.min(a.lastIndexOf('/') == -1
                                        ? 1000 : a.lastIndexOf('/'),
                                        a.lastIndexOf('\\') == -1
                                        ? 1000 : a.lastIndexOf('\\'))), b
        ).getAbsolutePath();
    }
}
