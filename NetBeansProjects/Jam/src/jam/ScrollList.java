package jam;

import java.util.List;
import java.util.ArrayList;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.Serializable;
import javax.swing.JFrame;

public class ScrollList implements Serializable {
    public List<DragAndDrop> objects;
    
    public ScrollList() {}
    
    public ScrollList(JFrame frame) {
        objects = new ArrayList<>();
        frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                scroll(e.getUnitsToScroll() * -3);
            }
        });
    }
    
    void add(DragAndDrop d) {
        objects.add(d);
    }
    void remove(int i) {
        objects.remove(i);
    }
    int indexOf(DragAndDrop d) {
        return objects.indexOf(d);
    }
    void scroll(int pixels) {
        int y = 1000000;
        for(DragAndDrop item : objects) {
            item.asSprite().translate(0, pixels);
        }
        for(DragAndDrop item : objects) {
            y = Math.min(y, item.asSprite().getY());
        }
        if(y > 0) {
            for(DragAndDrop item : objects) {
                item.asSprite().translate(0, -y);
            }
            y = 0;
        }
        if(objects.size() * (Preferences.PART_HEIGHT + 3) + y < Preferences.SCREEN_HEIGHT && y < 0) {
            y = Preferences.SCREEN_HEIGHT - (objects.size() * (Preferences.PART_HEIGHT + 3) + y);
            for(DragAndDrop item : objects) {
                item.asSprite().translate(0, y);
            }
        }
        for(DragAndDrop item : objects) {
            y = Math.min(y, item.asSprite().getY());
        }
        if(y > 0) {
            for(DragAndDrop item : objects) {
                item.asSprite().translate(0, -y);
            }
            y = 0;
        }
    }
    void rawAdd(DragAndDrop d) {
        int y = -50;
        for(DragAndDrop item : objects) {
            y = Math.max(y, item.asSprite().getY());
        }
        add(d);
        d.asSprite().setPosition(0, y + 50);
    }

    void refresh() {
        int y = 1000000;
        List<DragAndDrop> objects1 = new ArrayList<>();
        for(DragAndDrop item : objects) {
            objects1.add(item);
            y = Math.min(y, item.asSprite().getY());
        }
        objects.clear();
        for(DragAndDrop item : objects1) {
            rawAdd(item);
        }
        scroll(y);
    }
}
