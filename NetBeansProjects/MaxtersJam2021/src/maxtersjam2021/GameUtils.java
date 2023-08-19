package maxtersjam2021;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import javax.imageio.ImageIO;

public class GameUtils {

    static BufferedImage loadTexture(String name) {
        try {
            return ImageIO.read(new File("data/assets/" + name));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    static void drawAnimation(Graphics2D g2, int x, int y, int w, BufferedImage atlas, int from, int to, int steps, int frame) {
        int tw = atlas.getWidth() / steps;
        BufferedImage frameImage = atlas.getSubimage((frame % (to - from + 1) + from) * tw, 0, tw, atlas.getHeight());
        int h = w * frameImage.getHeight() / frameImage.getWidth();
        g2.drawImage(frameImage, x, y, w, h, null);
    }

    static boolean collides(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
        return x1 < x2 + w2
                && x1 + w1 > x2
                && y1 < y2 + h2
                && y1 + h1 > y2;
    }

    public static String readFile(String filename) {
        try {
            Scanner s = new Scanner(new File(filename));
            String str = "";
            while (s.hasNextLine()) {
                str += s.nextLine() + "\n";
            }
            return str;
        } catch (FileNotFoundException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void writeFile(String filename, String data) {
        byte bytes[] = data.getBytes();
        Path file = Paths.get(filename);
        try {
            Files.write(file, bytes);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
