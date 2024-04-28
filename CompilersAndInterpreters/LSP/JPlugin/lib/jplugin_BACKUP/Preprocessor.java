package net.infinitecoder.jplugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Preprocessor {

    public static String readFile(String pathToFile, boolean test) {
        try {
            Scanner s = new Scanner(new File(pathToFile));
            String str = "";
            while (s.hasNextLine()) {
                str += s.nextLine() + "\n";
            }
            if(test) {
                return "";
            }
            return str;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<ParsingError> errors;
    public String pathToFile;
    public boolean test;
    public String code;
    public int i = 0;

    public Preprocessor(String pathToFile, String code, boolean test) {
        this.errors = new ArrayList<>();
        this.pathToFile = pathToFile;
        this.code = code;
    }

    String parse() {
        while (i < code.length()) {
            skipWhitespaces();
            skipCharsAndStrings();
            if (code.charAt(i) == '#') {
                deleteChar();
                if (code.substring(i).startsWith("include")) {
                    code = code.replaceFirst("include", "");
                    skipWhitespaces();
                    String filename = "";
                    int start = i;
                    if (code.charAt(i) == '"') {
                        i++;
                        filename = "";
                        while (code.charAt(i) != '"') {
                            filename += code.charAt(i);
                            i++;
                        }
                        filename = new File(new File(pathToFile).getParent(), filename).getAbsolutePath();
                    } else if (code.charAt(i) == '<') {
                        i++;
                        filename = new File("lib").getAbsolutePath() + "/";
                        while (code.charAt(i) != '>') {
                            filename += code.charAt(i);
                            i++;
                        }
                    }
                    try {
                        code = code.substring(0, i + 1) + readFile(filename, test) + code.substring(i + 1);
                    } catch (RuntimeException re) {
//                        errors.add(new ParsingError(code.substring(0, i).split("\n").length, re));
                    }
                    i++;
                    code = code.replaceFirst(code.substring(start, i), "");
                    i = start;
                }
            } else {
                i++;
            }
        }
        return code;
    }

    private void skipCharsAndStrings() {
        if (code.charAt(i) == '"') {
            i++;
            while (code.charAt(i) != '"' && i < code.length()) {
                i++;
            }
        }
        if (code.charAt(i) == '\'') {
            i++;
            while (code.charAt(i) != '\'' && i < code.length()) {
                i++;
            }
        }
    }

    private void skipWhitespaces() {
        while (code.charAt(i) == ' ' && i < code.length()) {
            i++;
        }
    }

    private void deleteChar() {
        code = new StringBuilder(code).deleteCharAt(i).toString();
    }

    List<ParsingError> getErrors() {
        return errors;
    }
}
