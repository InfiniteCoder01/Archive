package jam;

import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.MouseMotionListener;

public class DragAndDrop {

    private Sprite sp;
    private DragListener l;
    private boolean dragging = false;
    private int dx, dy, dragx, dragy, mx, my, prec;
    private int effect_shift = 5;
    private boolean allowDrag = true;

    public DragAndDrop() {
        this.prec = 1;
    }

    public DragAndDrop(BufferedImage img, int x, int y, int w, int h, JFrame frame) {
        sp = new Sprite(img, x, y, w, h, frame);
        setOnDragDetectedListener(new DragListener() {
            @Override
            public void onDragged() {
            }

            @Override
            public void onDropped(int dragX, int dragY) {
            }

            @Override
            public void onMoved(int dragX, int dragY) {
            }
        });
        sp.setOnClickListener(new PressListener() {
            @Override
            public void pressed() {
                if (allowDrag) {
                    drag();
                    l.onDragged();
                }
            }

            @Override
            public void released() {
                if (allowDrag) {
                    releaseDrag();
                    l.onDropped(dragx, dragy);
                }
            }

            @Override
            public void clicked() {
            }
        });
        frame.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragging && allowDrag) {
                    dragx += (e.getX() - 7) - mx;
                    dragy += (e.getY() - 30) - my;
                    sp.setPosition(mx - dx - effect_shift, my - dy - effect_shift);
                    l.onMoved((e.getX() - 7) - mx, (e.getY() - 30) - my);
                }
                mx = e.getX() - 7;
                my = e.getY() - 30;
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                mx = e.getX() - 7;
                my = e.getY() - 30;
            }
        });
        this.prec = 1;
    }

    DragAndDrop(BufferedImage img, int x, int y, int w, int h, JFrame frame, int prec) {
        this(img, x, y, w, h, frame);
        this.prec = prec;
    }

    public boolean isDragging() {
        return dragging;
    }

    public void drag() {
        dragging = true;
        dragx = dragy = 0;
        dx = mx - sp.getX();
        dy = my - sp.getY();
        sp.translate(-effect_shift, -effect_shift);
    }

    public void releaseDrag() {
        dragging = false;
        int spx = sp.getX() + effect_shift;
        int spy = sp.getY() + effect_shift;
        sp.setPosition(Game.align(spx, prec), Game.align(spy, prec));
        if (effect_shift != 0) {
            if (sp.getX() < 0) {
                spx = 0;
            } else if (sp.getX() > (Preferences.SCREEN_WIDTH - Preferences.PART_WIDTH)) {
                spx = Preferences.SCREEN_WIDTH - Preferences.PART_WIDTH * 2;
            }
            if (sp.getY() < 0) {
                spy = 0;
            } else if (sp.getY() > (Preferences.SCREEN_HEIGHT - Preferences.PART_HEIGHT)) {
                spy = Preferences.SCREEN_HEIGHT - Preferences.PART_HEIGHT * 2;
            }
            sp.setPosition(Game.align(spx, prec), Game.align(spy, prec));
        }
    }

    public void setOnDragDetectedListener(DragListener l) {
        this.l = l;
    }

    public void draw(Graphics2D g2) {
        sp.draw(g2);
    }

    public Sprite asSprite() {
        return sp;
    }

    public void enableEffects(boolean enabled) {
        effect_shift = enabled ? 5 : 0;
    }

    void dragEnabled(boolean b) {
        allowDrag = b;
    }

    void drag(int x, int y) {
        l.onDragged();
        sp.setPosition(sp.getX() + x, sp.getY() + y);
        l.onMoved(x, y);
        l.onDropped(x, y);
    }
}
