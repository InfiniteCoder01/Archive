package jam;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

public class RocketPart {

    Sprite sp;
    RocketPart left, right, top, bottom;

    public RocketPart() {
    }

    public RocketPart(Sprite sp) {
        this.sp = sp;
    }

    void scale(double fact) {
        sp.scale(fact);
        if (top != null) {
            top.scale(fact);
        }
        if (right != null) {
            right.scale(fact);
        }
        if (bottom != null) {
            bottom.scale(fact);
        }
        if (left != null) {
            left.scale(fact);
        }
    }

    void draw(Graphics2D g2) {
        sp.draw(g2);
        if (top != null) {
            top.draw(g2);
        }
        if (right != null) {
            right.draw(g2);
        }
        if (bottom != null) {
            bottom.draw(g2);
        }
        if (left != null) {
            left.draw(g2);
        }
    }

    List<Sprite> getAllSprites() {
        List<Sprite> out = new ArrayList<>();
        out.add(sp);
        if (top != null) {
            out.addAll(top.getAllSprites());
        }
        if (right != null) {
            out.addAll(right.getAllSprites());
        }
        if (bottom != null) {
            out.addAll(bottom.getAllSprites());
        }
        if (left != null) {
            out.addAll(left.getAllSprites());
        }
        return out;
    }

    void refresh() {
        if (top != null) {
            top.sp.setPosition(sp.getX(), sp.getY() - sp.getH());
            top.refresh();
        }
        if (bottom != null) {
            bottom.sp.setPosition(sp.getX(), sp.getY() + sp.getH());
            bottom.refresh();
        }
        if (right != null) {
            right.sp.setPosition(sp.getX() + sp.getW(), sp.getY());
            right.refresh();
        }
        if (left != null) {
            left.sp.setPosition(sp.getX() - sp.getW(), sp.getY());
            left.refresh();
        }
    }
}
