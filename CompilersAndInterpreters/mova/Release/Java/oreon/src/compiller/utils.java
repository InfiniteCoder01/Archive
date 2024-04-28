package compiller;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public final class utils {

    public String readF(String file) {
        try {
            String result = "";
            FileReader fr = new FileReader(file);
            Scanner s = new Scanner(fr);
            while (s.hasNextLine()) {
                result += s.nextLine() + "\n";
            }
            return result;
        } catch (FileNotFoundException ex) {
        }
        return null;
    }

    public void writeF(String file, String text) {
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(text);
            fw.flush();
            fw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public void writeF(String file, byte[] bytes) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.close();
//            FileWriter fw = new FileWriter(file);
//            char[] cbuf = new char[text.length];
//            for (int i = 0; i < text.length; i++) {
//                cbuf[i] = (char) text[i];
//            }
//            fw.write(cbuf);
//            fw.flush();
//            fw.close();
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public String perev(String text) {
        String res = "";
        for (int i = text.length() - 1; i >= 0; i--) {
            res += text.charAt(i);
        }
        return res;
    }

    public boolean isNum(String text) {
        boolean res = true;
        for (int i = 0; i < text.length(); i++) {
            if (!Character.isDigit(text.charAt(i)) && text.charAt(i) != '-') {
                return false;
            }
        }
        return true;
    }
}
