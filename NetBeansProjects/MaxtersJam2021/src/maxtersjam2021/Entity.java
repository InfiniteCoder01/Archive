package maxtersjam2021;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import static maxtersjam2021.LevelMap.BLOCKS_COUNT;

public class Entity {

    public static BufferedImage entityAtlas = GameUtils.loadTexture("entity.png");
    public int speed = 3;
    public double GRAVITY = -0.098;//-0.2;
    public double x, y, w, h;
    public double dx, dy;
    public boolean onGround = false;
    public BufferedImage texture;
    public int type = 0, frames;
    public List<Integer> inventory;

    public Entity(double x, double y, BufferedImage texture, int frames, double scale, int type) {
        this.x = x;
        this.y = y;
        this.w = texture.getWidth() / frames * scale;
        this.h = texture.getHeight() * scale;
        this.texture = texture;
        this.type = type;
        this.frames = frames;
        this.inventory = new ArrayList<>();
    }

    void update(LevelMap lm) {
        onGround = false;
        x += dx;
        if (testCollision(lm)) {
            x -= dx;
            onGround = true;
        }
        y += dy;
        if (testCollision(lm)) {
            if (dy > 0) {
                onGround = true;
            }
            y -= dy;
            dy = 0;
        }
        dy -= GRAVITY;
    }

    public boolean testCollision(LevelMap lm) {
        boolean collides = false;
        for (int mapX = 0; mapX < lm.blockMap.length; mapX++) {
            for (int mapY = 0; mapY < lm.blockMap[mapX].length; mapY++) {
                int block = lm.blockMap[mapX][mapY];
                if (block != 0 && block != BLOCKS_COUNT && block != BLOCKS_COUNT - 1) {
                    if (GameUtils.collides((int) x, (int) y, (int) w, (int) h, mapX * LevelMap.BLOCK_SIZE, mapY * LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE)) {
                        collides = true;
                    }
                } else if (block == BLOCKS_COUNT && GameRenderer._instance != null) {
                    if (GameUtils.collides((int) x, (int) y, (int) w, (int) h, mapX * LevelMap.BLOCK_SIZE, mapY * LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE)) {
                        if (inventory.contains(1)) { //code of water is 1
                            lm.blockMap[mapX][mapY] = 4; //code of ice is 3
                            inventory.remove(inventory.indexOf(1)); //code of water is 1
                            collides = true;
                        } else {
                            GameRenderer._instance.restartLevel();
                            return false;
                        }
                    }
                } else if (block == BLOCKS_COUNT - 1 && GameRenderer._instance != null) {
                    if (GameUtils.collides((int) x, (int) y, (int) w, (int) h, mapX * LevelMap.BLOCK_SIZE, mapY * LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE)) {
                        GameRenderer._instance.nextLevel();
                        return false;
                    }
                }
            }
        }
        if (lm.supercursor) {
            if (MaxtersJam2021.frame != null && MaxtersJam2021.frame.getMousePosition() != null) {
                if (GameRenderer.isMouseDown) {
                    if (GameUtils.collides((int) x, (int) y, (int) w, (int) h, MaxtersJam2021.frame.getMousePosition().x - 14, MaxtersJam2021.frame.getMousePosition().y - 28, 14, 14)) {
                        return true;
                    }
                }
            }
        }
        return collides;
    }

    void draw(Graphics2D g2, int frame) {
        if (dx == 0 || type != 0) {
            GameUtils.drawAnimation(g2, (int) x, (int) y, (int) w, texture, 0, 0, frames, frame);
        } else {
            GameUtils.drawAnimation(g2, (int) x, (int) y, (int) w, texture, 1, 2, frames, frame);
        }
    }
}
