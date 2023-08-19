package jam;

import java.awt.Graphics2D;
import java.awt.Point;
import java.util.List;

public class Rocket {

    public RocketPart controller;
    public double scale = 1;
    public double x, y, attachX, attachY;
    public int weight, fuel, maxFuel, power, activePower, rotation;
    public boolean isAttached = false;
    public Planet attachPlanet = null;
    public double motionX, motionY;

    public Rocket() {
    }

    public Rocket(RocketPart controller) {
        this.controller = controller;
        for (Sprite sp : controller.getAllSprites()) {
            sp.setOnClickListener(new PressListener() {
                @Override
                public void clicked() {
                    Jam.selected = Rocket.this;
                }
                @Override
                public void pressed() {
                }
                @Override
                public void released() {
                }
            });
        }
    }

    void draw(Graphics2D g2) {
        Point pos = getCenter();
        g2.translate(pos.x, pos.y);
        g2.rotate(Math.toRadians(rotation));
        g2.translate(-pos.x, -pos.y);
        controller.draw(g2);
        g2.translate(pos.x, pos.y);
        g2.rotate(Math.toRadians(-rotation));
        g2.translate(-pos.x, -pos.y);
        if (activePower > 0) {
            for (Sprite sp : controller.getAllSprites()) {
                if (Parts.engines.indexOf(Parts.find(sp)) != -1) {
                    sp.setTexture(Parts.poweredEnginesTextures.get(Parts.engines.indexOf(Parts.find(sp))));
                }
            }
        } else {
            for (Sprite sp : controller.getAllSprites()) {
                if (Parts.poweredEnginesTextures.indexOf(sp.getTexture()) != -1) {
                    sp.setTexture(Parts.unpoweredEnginesTextures.get(Parts.poweredEnginesTextures.indexOf(sp.getTexture())));
                }
            }
        }
    }

    void setScale(double fact) {
        scale = fact;
        controller.scale(fact);
    }

    public void reGenerate() {
        controller.refresh();
    }

    Point getCenter() {
        Point out = new Point(0, 0);
        List<Sprite> sprites = controller.getAllSprites();
        for (Sprite sp : sprites) {
            out.x += sp.getX() + sp.getW() / 2;
            out.y += sp.getY() + sp.getH() / 2;
        }
        out.x /= sprites.size();
        out.y /= sprites.size();
        return out;
    }

    void place(double startX, double startY) {
        rotation = 0;
        List<Sprite> sprites = controller.getAllSprites();
        int maxY = Integer.MIN_VALUE;
        for (Sprite sp : sprites) {
            maxY = Math.max(maxY, sp.getY());
        }
        for (Sprite sp : sprites) {
            if (sp.getY() == maxY) {
                x = startX + (controller.sp.getX() - sp.getX()) / Preferences.PART_WIDTH;
                y = startY + (controller.sp.getY() - sp.getY()) / Preferences.PART_HEIGHT + 1;
                break;
            }
        }
        weight = Jam.getWeight(this);
        maxFuel = fuel = Jam.getFuel(this);
        power = Jam.getPower(this);
    }

    void motion() {
        rotation = rotation > 360 ? 0 : rotation;
        x += motionX;
        y += motionY;
        if (fuel > 0) {
            double motionEngine = Math.max(activePower - weight, 0) * 0.01;
            motionX += Math.cos(Math.toRadians(norm(rotation - 90))) * motionEngine;
            motionY += Math.sin(Math.toRadians(norm(rotation - 90))) * motionEngine;
            fuel = (int) Math.max(fuel - activePower * 0.01, 0);
            if (activePower - weight > 0) {
                isAttached = false;
            }
        }
        motionX *= 0.9;
        motionY *= 0.9;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public void rotate(int deg) {
        rotation += deg;
    }

    private double norm(int d) {
        return d > 360 ? 0 : d;
    }

}
