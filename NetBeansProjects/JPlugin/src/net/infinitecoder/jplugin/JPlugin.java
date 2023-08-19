package net.infinitecoder.jplugin;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class JPlugin {

    private final List<JPluginError> errors;
    private final String path;
    public static byte[] variablePool = new byte[104857600]; //100 MB

    public JPlugin(String path) {
        this.errors = new ArrayList<>();
        this.path = path;
    }

    public void parse() {
        Lexer lexer = new Lexer(readFile(path));
        lexer.tokenize();
        if (lexer.hasErrors()) {
            errors.addAll(lexer.getErrors());
        }
        Parser parser = new Parser(lexer.getTokens());
        parser.parse();
    }

    public String readFile(String filepath) {
        try {
            return new String(Files.readAllBytes(Paths.get(path)), "UTF-8");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<JPluginError> getErrors() {
        return errors;
    }

    public void printErrors(PrintStream ps) {
        for (JPluginError error : errors) {
            ps.println(error);
        }
    }
}
