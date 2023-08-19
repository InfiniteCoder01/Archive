package parser.bin;

import compiller.ListByte;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class binarrer {

    private final String code;

    public binarrer(String code) {
        this.code = code;
    }

    public byte[] execute() {
        Map<String, String> mets = new HashMap<>();
        ListByte result = new ListByte();
        final String[] codes = code.split("\n");
        int gi = 0;
        for (int i = 0; i < codes.length; i++) {
            String current = codes[i];
            String[] words = current.split(" ");
            if (words[0].equals("met")) {
                mets.put(words[1], (gi) + "");
            } else {
                gi += words.length;
            }
        }
        for (int i = 0; i < codes.length; i++) {
            String current = codes[i];
            String[] words = current.split(" ");
            System.out.println(Arrays.toString(words));
            result.addAll(new BinOp().execute(words));
            result.addAll(new LogOp().execute(words));
            result.addAll(new JVOp().execute(words, mets));
            result.addAll(new KeyOp().execute(words, mets));
        }
        for (int i = 0; i < result.length(); i++) {
            System.out.println(result.get(i));
        }
        return result.toArray();
    }

    private String replend(String replaceFirst) {
        String res = "";
        for (int i = 0; i < replaceFirst.length(); i++) {
            if (i < replaceFirst.length() - 1) {
                res += replaceFirst.charAt(i);
            }
        }
        return res;
    }
    static Map<String, String> binconv = new HashMap<>();

    static {
        String[] cmdStrings = {
            "add",
            "sub",
            "mul",
            "del",
            "delost",
            "bitnot",
            "bitor",
            "bitxor",
            "bitand",
            "or",
            "xor",
            "not",
            "and",
            "jneq",
            "jeq",
            "jnlt",
            "jngt",
            "goto",
            "jni",
            "mov",
            "eqals",
            "neqals",
            "lt",
            "gt",
            "ieqalsmv",
        };
        int i = 0;
        for (String cmdString : cmdStrings) {
            binconv.put(cmdString, "" + i);
            i++;
        }
    }
}
