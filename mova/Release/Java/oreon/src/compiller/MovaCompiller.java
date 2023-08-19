package compiller;

import java.io.*;
import java.awt.GraphicsEnvironment;
import parser.parser;

public class MovaCompiller {

    static Console console = System.console();

    public static void main(String[] args) {
        initCnsole();
        new parser().main(args);
    }

    public static void initCnsole() {
        if (console == null && !GraphicsEnvironment.isHeadless()) {
            String filename = MovaCompiller.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            try {
                File batch = new File("..\\oreon.cmd");
                if (!batch.exists()) {
                    batch.createNewFile();
                    PrintWriter writer = new PrintWriter(batch);
                    writer.println("@echo off");
                    writer.println("color 02");
                    writer.println("java -jar " + filename + " %*");
                    writer.println("pause");
                    writer.flush();
                }
                Runtime.getRuntime().exec("cmd /c start \"\" " + batch.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
