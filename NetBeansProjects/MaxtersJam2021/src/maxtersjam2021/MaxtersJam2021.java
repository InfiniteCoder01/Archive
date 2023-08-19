package maxtersjam2021;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MaxtersJam2021 {

    public static JFrame frame;

    public static void superCursor(boolean block) {
        if (block) {
            frame.setCursor(Cursor.HAND_CURSOR);
        } else {
            frame.setCursor(Cursor.DEFAULT_CURSOR);
        }
    }

    public static void main(String[] args) {
        frame = new JFrame("Sweet");
        frame.setSize(100, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        GameRenderer panel = new GameRenderer();
        frame.add(panel);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setVisible(true);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'e':
                    case 'E':
                        GameRenderer.inventoryOpened = !GameRenderer.inventoryOpened;
                        break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'a':
                    case 'A':
                        panel.player.dx = panel.player.onGround ? -panel.player.speed : panel.player.speed * -1.7;
                        break;
                    case 'd':
                    case 'D':
                        panel.player.dx = panel.player.onGround ? panel.player.speed : panel.player.speed * 1.7;
                        break;
                    case ' ':
                        if (panel.player.onGround) {
                            panel.player.dy = -5;
                        }
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'a':
                    case 'A':
                    case 'd':
                    case 'D':
                        panel.player.dx = 0;
                        break;
                }
            }
        });
    }
;
}
