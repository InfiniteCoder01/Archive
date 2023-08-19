package jam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import java.util.List;

public class Planet {

    public String name, centerObject;
    public int orbit, gravity, centX, centY;
    public Sprite sp;
    public int attachX, attachY;
    public boolean isStartPlanet = false, hasMap = false;
    public BufferedImage map;
    public double x, y, speed, degrees;
    public List<Integer> loot;
    private long rotateTimer;

    public Planet() {
    }

    public Planet(String data, JFrame frame) {
        String[] vals = data.split(";");
        name = vals[0].trim().substring(1, vals[0].trim().length() - 1);
        centX = Integer.parseInt(vals[1].trim());
        centY = Integer.parseInt(vals[2].trim());
        orbit = Integer.parseInt(vals[3].trim());
        gravity = Integer.parseInt(vals[4].trim());
        speed = Double.parseDouble(vals[5].trim());
        isStartPlanet = Integer.parseInt(vals[6].trim()) != 0;
        hasMap = Integer.parseInt(vals[7].trim()) != 0;
        attachX = Integer.parseInt(vals[8].trim());
        attachY = Integer.parseInt(vals[9].trim());
        centerObject = vals[10].trim().substring(1, vals[10].trim().length() - 1);
        BufferedImage tex = Jam.loadTexture("planets/" + name.toLowerCase() + ".png");
        sp = new Sprite(tex, centX, centY + orbit, tex.getWidth() * 112, tex.getHeight() * 112, frame);
        x = centX;
        y = centY + orbit;
        if (hasMap) {
            map = Jam.loadTexture("planets/" + name.toLowerCase() + "Map" + ".bmp");
        } else {
            map = new BufferedImage(sp.getTexture().getWidth(), sp.getTexture().getHeight(), BufferedImage.TYPE_3BYTE_BGR);
            for (int x = 0; x < sp.getTexture().getWidth(); x++) {
                for (int y = 0; y < sp.getTexture().getHeight(); y++) {
                    map.setRGB(x, y, new Color(sp.getTexture().getRGB(x, y)).getAlpha() == 255 ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }
        }
    }

    void draw(Graphics2D g2) {
        g2.setColor(Color.WHITE);
        g2.drawString(name, sp.getX() + sp.getW() / 2 - name.length() * 3, sp.getY() - 10);
        g2.setColor(Color.BLACK);
        sp.draw(g2);
        if (System.nanoTime() / 1000.0 - rotateTimer > Preferences.TURNTIME / Math.max(speed, 1)) {
            degrees += 0.1;
            x = Math.cos(Math.toRadians(degrees)) * orbit;
            x += centX;
            y = Math.sin(Math.toRadians(degrees)) * orbit;
            y += centY;
            rotateTimer = System.nanoTime() / 1000;
            for(Planet p : Jam.planets) {
                if(p.name.equals(centerObject)) {
                    x += p.x;
                    y += p.y;
                    break;
                }
            }
        }
    }
}
