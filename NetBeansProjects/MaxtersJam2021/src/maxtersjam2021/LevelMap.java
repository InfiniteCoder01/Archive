package maxtersjam2021;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class LevelMap {

    public static final int BLOCK_SIZE = 30, BLOCKS_COUNT = 6;
    public static BufferedImage blocks = GameUtils.loadTexture("blocks.png");

    public List<Entity> entities;
    public int brightness;
    public int[][] blockMap;
    public boolean supercursor;

    public LevelMap(String filename) {
        brightness = -1;
        String levelCode = GameUtils.readFile(filename);
        int w = Integer.parseInt(levelCode.split("\n")[0].split(" ")[0]), h = Integer.parseInt(levelCode.split("\n")[0].split(" ")[1]);
        blockMap = new int[w][h];
        entities = new ArrayList<>();
        levelCode = levelCode.split("\n", 2)[1];
        for (String blockCode : levelCode.split("\n")) {
            if (blockCode.isEmpty() || blockCode.startsWith("//")) {
                continue;
            }
            if (blockCode.startsWith("rect")) {
                int block = Integer.parseInt(blockCode.split(" ")[1]);
                int startX = Integer.parseInt(blockCode.split(" ")[2]);
                int startY = Integer.parseInt(blockCode.split(" ")[3]);
                int endX = Integer.parseInt(blockCode.split(" ")[4]);
                int endY = Integer.parseInt(blockCode.split(" ")[5]);
                for (int x = startX; x <= endX; x++) {
                    for (int y = startY; y <= endY; y++) {
                        blockMap[x][y] = block;
                    }
                }
            } else if (blockCode.startsWith("entity")) {
                int entity = Integer.parseInt(blockCode.split(" ")[1]);
                int x = Integer.parseInt(blockCode.split(" ")[2]) * BLOCK_SIZE;
                int y = Integer.parseInt(blockCode.split(" ")[3]) * BLOCK_SIZE;
                entities.add(new Entity(x, y, Entity.entityAtlas.getSubimage(Entity.entityAtlas.getHeight() * entity, 0, Entity.entityAtlas.getHeight(), Entity.entityAtlas.getHeight()), 1, 2, entity + 1));
                if (entity == 2) {
                    brightness = 255;
                }
            } else {
                int block = Integer.parseInt(blockCode.split(" ")[0]);
                int x = Integer.parseInt(blockCode.split(" ")[1]);
                int y = Integer.parseInt(blockCode.split(" ")[2]);
                blockMap[x][y] = block;
            }
        }
        MaxtersJam2021.superCursor(false);
    }

    void draw(Graphics2D g2, int frame, int w, int h) {
        for (int mapX = 0; mapX < blockMap.length; mapX++) {
            for (int mapY = 0; mapY < blockMap[mapX].length; mapY++) {
                int block = blockMap[mapX][mapY];
                if (block != 0) {
                    if (block == BLOCKS_COUNT) {
                        GameUtils.drawAnimation(g2, mapX * LevelMap.BLOCK_SIZE, mapY * LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE, blocks, block - 1, block + 1, BLOCKS_COUNT + 2, frame);
                    } else if (block != BLOCKS_COUNT - 1) { //finish
                        GameUtils.drawAnimation(g2, mapX * LevelMap.BLOCK_SIZE, mapY * LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE, blocks, block - 1, block - 1, BLOCKS_COUNT + 2, frame);
                    }
                }
            }
        }
        if (brightness != -1) {
            g2.translate(-GameRenderer._instance.camX, -GameRenderer._instance.camY);
            g2.scale(1 / GameRenderer._instance.scale, 1 / GameRenderer._instance.scale);
            g2.setColor(new Color(0, 0, 0, 255 - brightness));
            g2.fillRect(0, 0, w, h);
            g2.scale(GameRenderer._instance.scale, GameRenderer._instance.scale);
            g2.translate(GameRenderer._instance.camX, GameRenderer._instance.camY);
        }
        for (int i = 0; i < entities.size(); i++) {
            Entity entity = entities.get(i);
            if (brightness != -1) {
                if (entity.type == 2) { //code of sweet is 2
                    if (brightness < 10) {
                        entity.draw(g2, frame);
                    }
                } else {
                    entity.draw(g2, frame);
                }
            } else {
                entity.draw(g2, frame);
            }
        }
        for (int mapX = 0; mapX < blockMap.length; mapX++) {
            for (int mapY = 0; mapY < blockMap[mapX].length; mapY++) {
                int block = blockMap[mapX][mapY];
                if (block != 0) {
                    if (block == BLOCKS_COUNT - 1) { //finish
                        GameUtils.drawAnimation(g2, mapX * LevelMap.BLOCK_SIZE, mapY * LevelMap.BLOCK_SIZE, LevelMap.BLOCK_SIZE, blocks, block - 1, block - 1, BLOCKS_COUNT + 2, frame);
                    }
                }
            }
        }
    }
}
