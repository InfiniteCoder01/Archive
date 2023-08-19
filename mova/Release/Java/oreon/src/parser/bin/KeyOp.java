package parser.bin;

import compiller.ListByte;
import java.util.Map;

public class KeyOp {//0b10101000

    public ListByte execute(String[] words, Map<String, String> mets) {
        ListByte result = new ListByte();
        if (words[0].startsWith("mov")) {
            if (words[0].replaceFirst("mov", "").startsWith("c")) {
                result.add((byte) 0b10101000);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            } else {
                result.add((byte) 0b10101001);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            }
        }
        return result;
    }
}
