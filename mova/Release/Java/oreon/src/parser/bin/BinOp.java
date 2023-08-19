package parser.bin;

import compiller.ListByte;

public class BinOp {

    public ListByte execute(String[] words) {
        ListByte result = new ListByte();
        if (words[0].startsWith("add")) {
            if (words[0].replaceFirst("add", "").startsWith("c")) {
                if (words[0].replaceFirst("addc", "").startsWith("c")) {
                    result.add((byte) 0b10000000);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b10000001);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            } else {
                if (words[0].replaceFirst("addv", "").startsWith("c")) {
                    result.add((byte) 0b10000010);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b10000011);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            }
        } else if (words[0].startsWith("sub")) {
            if (words[0].replaceFirst("sub", "").startsWith("c")) {
                if (words[0].replaceFirst("subc", "").startsWith("c")) {
                    result.add((byte) 0b01000000);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b01000001);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            } else {
                if (words[0].replaceFirst("subv", "").startsWith("c")) {
                    result.add((byte) 0b01000010);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b01000011);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            }
        } else if (words[0].startsWith("mul")) {
            if (words[0].replaceFirst("mul", "").startsWith("c")) {
                if (words[0].replaceFirst("mulc", "").startsWith("c")) {
                    result.add((byte) 0b11000000);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b11000001);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            } else {
                if (words[0].replaceFirst("mulv", "").startsWith("c")) {
                    result.add((byte) 0b11000010);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b11000011);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            }
        } else if (words[0].startsWith("delost")) {
            if (words[0].replaceFirst("delost", "").startsWith("c")) {
                if (words[0].replaceFirst("delostc", "").startsWith("c")) {
                    result.add((byte) 0b10100000);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b10100001);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            } else {
                if (words[0].replaceFirst("delostv", "").startsWith("c")) {
                    result.add((byte) 0b10100010);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b10100011);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            }
        } else if (words[0].startsWith("del")) {
            if (words[0].replaceFirst("del", "").startsWith("c")) {
                if (words[0].replaceFirst("delc", "").startsWith("c")) {
                    result.add((byte) 0b00100000);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b00100001);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            } else {
                if (words[0].replaceFirst("delv", "").startsWith("c")) {
                    result.add((byte) 0b00100010);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                } else {
                    result.add((byte) 0b00100011);
                    result.add((byte) Integer.parseInt(words[1]));
                    result.add((byte) Integer.parseInt(words[2]));
                    result.add((byte) Integer.parseInt(words[3]));
                }
            }
        } else if (words[0].startsWith("not")) {
            if (words[0].replaceFirst("not", "").startsWith("c")) {
                result.add((byte) 0b01100000);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            } else {
                result.add((byte) 0b01100001);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            }
        } else if (words[0].startsWith("bitnot")) {
            if (words[0].replaceFirst("bitnot", "").startsWith("c")) {
                result.add((byte) 0b11100000);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            } else {
                result.add((byte) 0b11100001);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            }
        } else if (words[0].startsWith("bitor")) {
            if (words[0].replaceFirst("bitor", "").startsWith("c")) {
                result.add((byte) 0b00010000);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            } else {
                result.add((byte) 0b00010001);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            }
        } else if (words[0].startsWith("bitand")) {
            if (words[0].replaceFirst("bitand", "").startsWith("c")) {
                result.add((byte) 0b10010000);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            } else {
                result.add((byte) 0b10010001);
                result.add((byte) Integer.parseInt(words[1]));
                result.add((byte) Integer.parseInt(words[2]));
            }
        }
        return result;
    }
}
