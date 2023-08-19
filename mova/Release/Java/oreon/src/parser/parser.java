package parser;

import compiller.utils;
import java.io.File;
import java.io.IOException;
import parser.lexer.Token;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class parser {

    private String file;
    private String code;

    public parser() {
    }

    public void main(String[] args) {
        boolean fileF = false;
        if (args.length == 1) {
            if (!args[0].startsWith("-")) {
                file = args[0];
                code = new utils().readF(file);
            } else if (args[0].equals("-h")) {
                System.out.println("Help doesn`t support at this version!");
            } else if (args[0].equals("-r")) {
                System.out.println("Debug mode doesn`t support at this version!");
            }
        } else {
            for (String arg : args) {
                if (arg.startsWith("-")) {
                    if (arg.equals("-f")) {
                        fileF = true;
                    }
                } else {
                    if (fileF) {
                        file = arg;
                        code = new utils().readF(file);
                        fileF = false;
                    }
                }
            }
        }
        if (file.endsWith(".amf") || file.endsWith(".amf\"")) {
            try {
                Runtime.getRuntime().exec("cmd /c start \"\" " + new File("assembler.cmd").getPath() + " " + file);
            } catch (IOException ex) {
                Logger.getLogger(parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            Compiller cmp = new Compiller();
            new utils().writeF("this.amf", cmp.main(code));
            try {
                Runtime.getRuntime().exec("cmd /c start \"\" " + new File("assembler.cmd").getPath() + " this.amf");
            } catch (IOException ex) {
                Logger.getLogger(parser.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
