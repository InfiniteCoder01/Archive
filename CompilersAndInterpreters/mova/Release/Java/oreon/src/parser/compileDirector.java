package parser;

import compiller.utils;
import java.io.File;

public class compileDirector {

    final String code;
    final String file;
    final String libs = ".\\libs";

    public compileDirector(String code, String file) {
        this.code = code;
        this.file = file;
    }

    public String execute() {
        String result = code;
        String[] statements;
        statements = result.split(";");
        for (String statement : statements) {
            if (statement.contains("#")) {
                String[] words = statement.replaceAll("\n", "").split(" ");
                switch (words[0]) {
                    case "#define":
                        result = result.replaceFirst(statement + ";", "");
                        result = result.replaceAll(words[1], words[2]);
                        break;
                    case "#include":
                        String file = words[1].replaceFirst("<", "").replace(">", "");
                        result = result.replaceFirst(statement + ";", new compileDirector(include(file), file).execute());
                        break;
                }
            }
        }
        return result;
    }

    private String include(String file) {
        String fileParent = new File(file).getParent();
        if (new utils().readF(fileParent + "\\" + file) != null) {
            return new utils().readF(fileParent + "\\" + file);
        } else if (new utils().readF(libs + "\\" + file) != null) {
            return new utils().readF(libs + "\\" + file);
        }else if (new utils().readF(file) != null) {
            return new utils().readF(file);
        }
        throw new RuntimeException("Error including file " + file);
    }
}
