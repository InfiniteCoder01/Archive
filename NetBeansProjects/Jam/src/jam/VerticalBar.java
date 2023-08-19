package jam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import javax.swing.JFrame;

public class VerticalBar {

    private int value, min, max, x, y, w, h;
    private JFrame frame;

    public VerticalBar(int value, int min, int max, JFrame frame, int x, int y, int w, int h) {
        this.value = value;
        this.min = min;
        this.max = max;
        this.frame = frame;
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }
    
    public void draw(Graphics2D g2) {
        g2.setColor(Color.BLUE);
        g2.fillRect(x, y, w, h);
        g2.setColor(Color.RED);
        g2.fillRect(x + 1, Game.map(max - value, min, max, y + 1, y + h - 1), w - 2, Game.map(value, min, max, 0, h - 2));
        g2.setColor(Color.BLACK);
    }
    
}
