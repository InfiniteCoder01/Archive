package maxtersjam2021;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class GameRenderer extends JPanel {

    private static final long serialVersionUID = 1L;
    public static boolean isMouseDown = false, inventoryOpened = true, lock = false;
    public float frame = 0;
    public LevelMap levelMap;
    public Entity player;
    public int camX = 0, camY = 0, clickX = 0, clickY = 0;
    public int currentLevel = 0;
    public long levelStarted = 0;
    public int sweets = 0, prevSweets = 0;
    public int inMenu = 1;
    public double scale;
    public Timer t;
    public TimerTask tt;
    public static GameRenderer _instance;
    public static BufferedImage inventory, background, logo, play, win, info;
    public static String infoText = "This game was maded by InfiniteCoder(Dima)\n"
            + "I am 12 years old.\n"
            + "To hide/show inventory press E.\n"
            + "In data/assets folder all game textures, in\n"
            + "data/levels folder level files with comment.\n"
            + "To add custom level just put code into level_.txt where _ is\n"
            + "level number like level1, level2...\n"
            + "In top-left corner sweet counter, share you records!\n"
            + "To reset progress remove progress.txt!";

    public GameRenderer() {
        inventory = GameUtils.loadTexture("inventory.png");
        background = GameUtils.loadTexture("background.png");
        logo = GameUtils.loadTexture("logo.png");
        play = GameUtils.loadTexture("play.png");
        win = GameUtils.loadTexture("win.png");
        info = GameUtils.loadTexture("info.png");
        lock = true;
        MaxtersJam2021.frame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if ((inMenu == 1)) {
                    clickX = e.getX();
                    clickY = e.getY();
                    int startButtonX = (int) (getWidth() / 2 + play.getWidth() * scale / 2);
                    int startButtonY = (int) (getHeight() / 2 + play.getHeight() * scale / 2);
                    int infoButtonX = (int) (getWidth() / 2 + info.getWidth() * scale / 2);
                    int infoButtonY = (int) (startButtonY + play.getHeight() * scale + info.getHeight() * scale / 2 + scale * 3);
                    if (clickX > startButtonX * scale && clickX < (startButtonX + play.getWidth() * scale)
                            && clickY > startButtonY * scale && clickY < (startButtonY + play.getHeight() * scale)) {
                        inMenu = 0;
                        try {
                            currentLevel = Integer.parseInt(GameUtils.readFile("progress.txt").replaceAll("\n", "").trim());
                        } catch (NumberFormatException nfe) {
                            currentLevel = 0;
                            System.out.println(nfe);
                        } catch(RuntimeException re) {
                            currentLevel = 0;
                            System.out.println(re);
                        }
                        restartLevel();
                    } else if (clickX > infoButtonX * scale && clickX < (infoButtonX + info.getWidth() * scale)) {
                        if (clickY > infoButtonY * scale && clickY < (infoButtonY + info.getHeight() * scale)) {
                            JOptionPane.showMessageDialog(null, infoText);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isMouseDown = true;
                    clickX = e.getX();
                    clickY = e.getY();
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    MaxtersJam2021.superCursor(true);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    isMouseDown = false;
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    MaxtersJam2021.superCursor(false);
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }
        });
        t = new Timer();
        tt = new TimerTask() {
            @Override
            public void run() {
                if (!lock) {
                    update();
                }
            }
        };
        t.scheduleAtFixedRate(tt, 0, 10);
        _instance = this;
    }

    @Override
    public void paintComponent(Graphics g) {
        scale = MaxtersJam2021.frame.getHeight() / (double) LevelMap.BLOCK_SIZE / 25;
        Graphics2D g2 = (Graphics2D) g;
        if (inMenu == 0) {
            moveCam();
            g2.setColor(Color.BLACK);
            g2.drawImage(background, 0, 0, getWidth(), getHeight(), null);
            g2.scale(scale, scale);
            g2.translate(camX, camY);
            levelMap.draw(g2, (int) frame, getWidth(), getHeight());
            player.draw(g2, (int) frame);
            g2.translate(-camX, -camY);
            g2.drawImage(Entity.entityAtlas.getSubimage(Entity.entityAtlas.getHeight(), 0, Entity.entityAtlas.getHeight(), Entity.entityAtlas.getHeight()), (int) (scale * 12), (int) (scale * 1.3), Entity.entityAtlas.getHeight() * 2, Entity.entityAtlas.getHeight() * 2, null);
            g2.scale(2, 2);
            g2.drawString(sweets + "", (int) (scale * 30), (int) (scale * 17));
            g2.scale(0.5, 0.5);
            g2.scale(1 / scale, 1 / scale);
            if (inventoryOpened) {
                drawInventory(g2, scale);
            }
        } else if (inMenu == 1) {
            g2.drawImage(logo, 0, 0, getWidth(), getHeight(), null);
            g2.scale(scale, scale);
            int startButtonX = (int) (getWidth() / 2 + play.getWidth() * scale / 2);
            int startButtonY = (int) (getHeight() / 2 + play.getHeight() * scale / 2);
            g2.drawImage(play, startButtonX, startButtonY, null);
            int infoButtonX = (int) (getWidth() / 2 + info.getWidth() * scale / 2);
            int infoButtonY = (int) (startButtonY + play.getHeight() * scale + info.getHeight() * scale / 2 + scale * 3);
            g2.drawImage(info, infoButtonX, infoButtonY, null);
        } else if (inMenu == 2) {
            g2.drawImage(win, 0, 0, getWidth(), getHeight(), null);
            g2.scale(scale, scale);
            g2.drawImage(Entity.entityAtlas.getSubimage(Entity.entityAtlas.getHeight(), 0, Entity.entityAtlas.getHeight(), Entity.entityAtlas.getHeight()), (int) (scale * 12), (int) (scale * 1.3), Entity.entityAtlas.getHeight() * 2, Entity.entityAtlas.getHeight() * 2, null);
            g2.scale(2, 2);
            g2.drawString(sweets + "", (int) (scale * 30), (int) (scale * 17));
        }
        repaint();
    }

    public final void startLevel(String levelName) {
        levelStarted = System.currentTimeMillis();
        lock = true;
        GameUtils.writeFile("progress.txt", currentLevel + "");
        player = new Entity(100, 100, GameUtils.loadTexture("Player.png"), 3, 2, 0);
        levelMap = new LevelMap("data/levels/" + levelName);
        camX = camY = 0;
        prevSweets = sweets;
        lock = false;
    }

    public final void restartLevel() {
        levelStarted = System.currentTimeMillis();
        lock = true;
        sweets = prevSweets;
        startLevel("level" + currentLevel + ".txt");
    }

    public final void nextLevel() {
        if (System.currentTimeMillis() - levelStarted < 1000) {
            return;
        }
        levelStarted = System.currentTimeMillis();
        lock = true;
        try {//FIXME: level 2 do not loading
            startLevel("level" + (++currentLevel) + ".txt");
        } catch (RuntimeException ex) {
            if (ex.getCause() instanceof FileNotFoundException) {
                inMenu = 2;
                currentLevel = 0;
                restartLevel();
            }
        }
    }

    public void moveCam() {
        if (player.x + camX < 100) {
            camX = (int) (100 - player.x);
        }
        if (player.x + camX > getWidth() - 100) {
            camX = (int) (getWidth() - player.x - 100);
        }
        if (player.y + camY < 50) {
            camY = (int) (50 - player.y);
        }
        if (player.y + camY > getHeight() - 50) {
            camY = (int) (getHeight() - player.y - 50);
        }
    }

    private void drawInventory(Graphics2D g2, double scale) {
        g2.drawImage(inventory, (int) (getWidth() / 2 - inventory.getWidth() / 2 * scale), 10, (int) (inventory.getWidth() * scale), (int) (inventory.getHeight() * scale), null);
        int index = 0;
        for (int i = 0; i < player.inventory.size(); i++) {
            int item = player.inventory.get(i);
            BufferedImage itemTexture = Entity.entityAtlas.getSubimage(Entity.entityAtlas.getHeight() * (item - 1), 0, Entity.entityAtlas.getHeight(), Entity.entityAtlas.getHeight());
            g2.drawImage(itemTexture, (int) (getWidth() / 2 - inventory.getWidth() / 2 * scale + index * inventory.getWidth() * scale / 9 + scale * 3), 10, (int) ((inventory.getHeight() - 5) * scale), (int) ((inventory.getHeight() - 5) * scale), null);
            index++;
        }
    }

    void update() {
        player.update(levelMap);
        frame += 0.1;
        for (int i = 0; i < levelMap.entities.size(); i++) {
            Entity entity = levelMap.entities.get(i);
            if (GameUtils.collides((int) player.x, (int) player.y, (int) player.w, (int) player.h, (int) entity.x, (int) entity.y, (int) entity.w, (int) entity.h)) {
                switch (entity.type) {
                    case 1:
                        if (player.inventory.size() < 9) {
                            player.inventory.add(entity.type);
                            levelMap.entities.remove(i);
                        }
                        break;
                    case 2:
                        sweets++;
                        levelMap.entities.remove(i);
                        break;
                    case 3://code of sun is 3
                        levelMap.brightness -= 63;
                        levelMap.entities.remove(i);
                        break;
                }
            }
        }
        if (player.y > levelMap.blockMap[0].length * LevelMap.BLOCK_SIZE + 30) {
            restartLevel();
        }
    }
}
