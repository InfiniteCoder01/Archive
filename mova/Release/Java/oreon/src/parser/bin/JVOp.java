package parser.bin;

import compiller.ListByte;
import java.util.Map;

public class JVOp {

    Map<String, String> mets;

    public ListByte execute(String[] words, Map<String, String> mets) {
        this.mets = mets;
        ListByte result = new ListByte();
        if (words[0].startsWith("jneq")) {
            if (words[0].replaceFirst("jneq", "").startsWith("c")) {
                if (words[0].replaceFirst("jneqc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b10001000));
                } else {
                    result.addAll(add4(words, 0b10001001));
                }
            } else {
                if (words[0].replaceFirst("jneqv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b10001010));
                } else {
                    result.addAll(add4(words, 0b10001011));
                }
            }
        } else if (words[0].startsWith("jeq")) {
            if (words[0].replaceFirst("jeq", "").startsWith("c")) {
                if (words[0].replaceFirst("jeqc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b01001000));
                } else {
                    result.addAll(add4(words, 0b01001001));
                }
            } else {
                if (words[0].replaceFirst("jeqv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b01001010));
                } else {
                    result.addAll(add4(words, 0b01001011));
                }
            }
        } else if (words[0].startsWith("jnlt")) {
            if (words[0].replaceFirst("jnlt", "").startsWith("c")) {
                if (words[0].replaceFirst("jnltc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b11001000));
                } else {
                    result.addAll(add4(words, 0b11001001));
                }
            } else {
                if (words[0].replaceFirst("jnltv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b11001010));
                } else {
                    result.addAll(add4(words, 0b11001011));
                }
            }
        } else if (words[0].startsWith("goto")) {
            result.add((byte) 0b00101000);
            result.add((byte) Integer.parseInt(mets.get(words[1])));
        } else if (words[0].startsWith("jni")) {
            if (words[0].replaceFirst("jni", "").startsWith("c")) {
                result.add((byte) 0b10101000);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(mets.get(words[2])));
            } else {
                result.add((byte) 0b10101001);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(mets.get(words[2])));
            }
        }
        return result;
    }

    private ListByte add4(String[] words, int bin) {
        ListByte result = new ListByte();
        result.add((byte) bin);
        result.add((byte) Integer.parseInt(words[1]));
        result.add((byte) Integer.parseInt(words[2]));
        result.add((byte) Integer.parseInt(mets.get(words[3])));
        return result;
    }
}
