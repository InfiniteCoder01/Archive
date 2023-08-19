package jam;

import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Sprite {

    private BufferedImage img;
    private JFrame frame;
    private PressListener l;
    private int w, h, mx, my, x, y, W, H;
    private MouseListener ml;
    private boolean drawing = true;

    public Sprite() {
    }

    public Sprite(BufferedImage img, int x, int y, int w, int h, JFrame frame) {
        this.img = img;
        this.w = w;
        W = w;
        this.h = h;
        H = h;
        this.x = x;
        this.y = y;
        this.frame = frame;
        frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mx = e.getX() - 7;
                my = e.getY() - 30;
            }
        });
        ml = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Game.collide(new Point(mx, my), new Point(Sprite.this.x, Sprite.this.y), new Point(Sprite.this.w, Sprite.this.h))) {
                    l.clicked();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (Game.collide(new Point(mx, my), new Point(Sprite.this.x, Sprite.this.y), new Point(Sprite.this.w, Sprite.this.h))) {
                    l.pressed();
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mx = e.getX() - 7;
                my = e.getY() - 30;
                if (Game.collide(new Point(mx, my), new Point(Sprite.this.x, Sprite.this.y), new Point(Sprite.this.w, Sprite.this.h))) {
                    l.released();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
            }

            @Override
            public void mouseExited(MouseEvent e) {
            }

        };
        setOnClickListener(new PressListener() {
            @Override
            public void clicked() {
            }

            @Override
            public void pressed() {
            }

            @Override
            public void released() {
            }
        });
        
        Runnable helloRunnable = new Runnable() {
            public void run() {
                if(drawing) {
                    drawing = false;
                } else {
                    frame.removeMouseListener(ml);
                }
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, 100, TimeUnit.MILLISECONDS);
    }

    public void draw(Graphics2D g2) {
        if (!Arrays.asList(frame.getMouseListeners()).contains(ml)) {
            frame.addMouseListener(ml);
        }
        g2.drawImage(img, x, y, w, h, null);
        drawing = true;
    }

    public void setOnClickListener(PressListener l) {
        this.l = l;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    void translate(int x, int y) {
        setPosition(getX() + x, getY() + y);
    }

    BufferedImage getTexture() {
        return img;
    }

    void scale(double factor) {
        this.w = (int) (W * factor);
        this.h = (int) (H * factor);
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }

    void setTexture(BufferedImage tex) {
        img = tex;
    }

}
