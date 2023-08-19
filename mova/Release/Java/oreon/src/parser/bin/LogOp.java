package parser.bin;

import compiller.ListByte;

public class LogOp {

    public ListByte execute(String[] words) {
        ListByte result = new ListByte();
        if (words[0].startsWith("eqals")) {
            if (words[0].replaceFirst("eqals", "").startsWith("c")) {
                if (words[0].replaceFirst("eqalsc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b01010000));
                } else {
                    result.addAll(add4(words, 0b01010001));
                }
            } else {
                if (words[0].replaceFirst("eqalsv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b01010010));
                } else {
                    result.addAll(add4(words, 0b01010011));
                }
            }
        } else if (words[0].startsWith("neqals")) {
            if (words[0].replaceFirst("neqals", "").startsWith("c")) {
                if (words[0].replaceFirst("neqalsc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b11010000));
                } else {
                    result.addAll(add4(words, 0b11010001));
                }
            } else {
                if (words[0].replaceFirst("neqalsv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b11010010));
                } else {
                    result.addAll(add4(words, 0b11010011));
                }
            }
        } else if (words[0].startsWith("lt")) {
            if (words[0].replaceFirst("lt", "").startsWith("c")) {
                if (words[0].replaceFirst("ltc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b00110000));
                } else {
                    result.addAll(add4(words, 0b00110001));
                }
            } else {
                if (words[0].replaceFirst("ltv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b00110010));
                } else {
                    result.addAll(add4(words, 0b00110011));
                }
            }
        } else if (words[0].startsWith("gt")) {
            if (words[0].replaceFirst("gt", "").startsWith("c")) {
                if (words[0].replaceFirst("gtc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b10110000));
                } else {
                    result.addAll(add4(words, 0b10110001));
                }
            } else {
                if (words[0].replaceFirst("gtv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b10110010));
                } else {
                    result.addAll(add4(words, 0b10110011));
                }
            }
        } else if (words[0].startsWith("ieqalsmv")) {
            if (words[0].replaceFirst("ieqalsmv", "").startsWith("c")) {
                if (words[0].replaceFirst("ieqalsmvc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b01110000));
                } else {
                    result.addAll(add4(words, 0b01110001));
                }
            } else {
                if (words[0].replaceFirst("ieqalsmvv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b01110010));
                } else {
                    result.addAll(add4(words, 0b01110011));
                }
            }
        }else if (words[0].startsWith("and")) {
            if (words[0].replaceFirst("and", "").startsWith("c")) {
                if (words[0].replaceFirst("andc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b11110000));
                } else {
                    result.addAll(add4(words, 0b11110001));
                }
            } else {
                if (words[0].replaceFirst("andv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b11110010));
                } else {
                    result.addAll(add4(words, 0b11110011));
                }
            }
        }else if (words[0].startsWith("or")) {
            if (words[0].replaceFirst("or", "").startsWith("c")) {
                if (words[0].replaceFirst("orc", "").startsWith("c")) {
                    result.addAll(add4(words, 0b00001000));
                } else {
                    result.addAll(add4(words, 0b00001001));
                }
            } else {
                if (words[0].replaceFirst("orv", "").startsWith("c")) {
                    result.addAll(add4(words, 0b00001010));
                } else {
                    result.addAll(add4(words, 0b00001011));
                }
            }
        }
        return result;
    }

    private ListByte add4(String[] words, int bin) {
        ListByte result = new ListByte();
        result.add((byte) bin);
        result.add((byte) Integer.parseInt(words[1]));
        result.add((byte) Integer.parseInt(words[2]));
        result.add((byte) Integer.parseInt(words[3]));
        return result;
    }
}
