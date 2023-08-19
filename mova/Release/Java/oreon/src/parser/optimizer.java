package parser;

import compiller.utils;
import java.io.File;
import parser.ast.Statement;

public class optimizer {

    Statement code;
    final String libs = ".\\libs";

    utils u = new utils();

    public optimizer(Statement code) {
        this.code = code;
    }

    String optimize() {
        String result = "";
        for (String statement : code.build().split("\n")) {
            String[] words = statement.split(" ");
            switch (words[0]) {
                case "addcc":
                    result += "movc " + words[1] + " " + (pi(words[2]) + pi(words[3])) + "\n";
                    break;
                case "subcc":
                    result += "movc " + words[1] + " " + +(pi(words[2]) - pi(words[3])) + "\n";
                    break;
                case "mulcc":
                    result += "movc " + words[1] + " " + +(pi(words[2]) * pi(words[3])) + "\n";
                    break;
                case "delcc":
                    result += "movc " + words[1] + " " + +(pi(words[2]) / pi(words[3])) + "\n";
                    break;
                case "delostcc":
                    result += "movc " + words[1] + " " + +(pi(words[2]) % pi(words[3])) + "\n";
                    break;
                case "import":
                    result += include(words[1]);
                    break;
                default:
                    result += statement + "\n";
                    break;
            }
        }
        return result;
    }

    private int pi(String word) {
        return Integer.parseInt(word);
    }

    private String include(String file) {
        String fileParent = new File(file).getParent();
        if (new utils().readF(fileParent + "\\" + file) != null) {
            return new utils().readF(fileParent + "\\" + file);
        } else if (new utils().readF(libs + "\\" + file) != null) {
            return new utils().readF(libs + "\\" + file);
        } else if (new utils().readF(file) != null) {
            return new utils().readF(file);
        }
        throw new RuntimeException("Error including file " + file);
    }
}
