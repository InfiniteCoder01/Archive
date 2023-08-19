package jam;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevelObject implements Serializable {

    public List<Integer> inv;
    public List<SerialRocket> rockets;
    public int menuID;
    public boolean engine, isSpacePressed, spectMode, left, right;
    public int selected;
    public List<SerialPlanet> planets;
    public List<String> planetsVisited;

    public LevelObject() {
        menuID = Jam.menuID;
        engine = Jam.engine;
        isSpacePressed = Jam.isSpacePressed;
        spectMode = Jam.spectMode;
        left = Jam.left;
        right = Jam.right;
        planets = new ArrayList<>();
        for (Planet p : Jam.planets) {
            planets.add(new SerialPlanet(p));
        }
        rockets = new ArrayList<>();
        int i = 0;
        for (Rocket r : Jam.rockets) {
            if (r == Jam.selected) {
                selected = i;
            }
            rockets.add(new SerialRocket(r, planets));
            i++;
        }
        planetsVisited = new ArrayList<>();
        for (Planet p : Jam.planetsVisited) {
            planetsVisited.add(p.name);
        }
        inv = new ArrayList<>();
        for (DragAndDrop d : Jam.inv.objects) {
            inv.add(Parts.find(d.asSprite()));
        }
    }

    void load() {
        Jam.inventory = new ArrayList<>();
        for (int i : inv) {
            Jam.addToInventory(Parts.parts[i]);
        }
        Jam.menuID = menuID;
        Jam.engine = engine;
        Jam.isSpacePressed = isSpacePressed;
        Jam.spectMode = spectMode;
        Jam.left = left;
        Jam.right = right;
        for (Planet p : Jam.planets) {
            for (SerialPlanet sp : planets) {
                if (p.name.equals(sp.name)) {
                    sp.load(p);
                    break;
                }
            }
        }
        Jam.rockets = new ArrayList<>();
        for (SerialRocket r : rockets) {
            Jam.rockets.add(r.load(Jam.planets));
        }
        try {
            Jam.selected = Jam.rockets.get(selected);
        } catch (Exception ex) {
            Jam.selected = null;
        }
        Jam.planetsVisited = new ArrayList<>();
        for (String p : planetsVisited) {
            for (Planet p1 : Jam.planets) {
                if (p1.name.equals(p)) {
                    Jam.planetsVisited.add(p1);
                    break;
                }
            }
        }
    }

}

class SerialPlanet implements Serializable {

    double x, y, degrees;
    String name;

    public SerialPlanet(Planet p) {
        x = p.x;
        y = p.y;
        degrees = p.degrees;
        name = p.name;
    }

    public void load(Planet p) {
        p.x = x;
        p.y = y;
        p.degrees = degrees;
        p.name = name;
    }
}

class SerialRocket implements Serializable {

    public double scale;
    public double x, y, attachX, attachY;
    public int weight, fuel, maxFuel, power, activePower, rotation;
    public boolean isAttached;
    public String attachPlanet;
    public double motionX, motionY;
    public SerialRocketPart controller;

    public SerialRocket(Rocket r, List<SerialPlanet> planets) {
        scale = r.scale;
        x = r.x;
        y = r.y;
        attachX = r.attachX;
        attachY = r.attachY;
        weight = r.weight;
        fuel = r.fuel;
        maxFuel = r.maxFuel;
        power = r.power;
        activePower = r.activePower;
        rotation = r.rotation;
        isAttached = r.isAttached;
        motionX = r.motionX;
        motionY = r.motionY;
        for (SerialPlanet p : planets) {
            if (p.name.equals(r.attachPlanet.name)) {
                attachPlanet = p.name;
                break;
            }
        }
        controller = new SerialRocketPart(r.controller);
    }

    public Rocket load(List<Planet> planets) {
        Rocket r = new Rocket();
        r.scale = scale;
        r.x = x;
        r.y = y;
        r.attachX = attachX;
        r.attachY = attachY;
        r.weight = weight;
        r.fuel = fuel;
        r.maxFuel = maxFuel;
        r.power = power;
        r.activePower = activePower;
        r.rotation = rotation;
        r.isAttached = isAttached;
        r.motionX = motionX;
        r.motionY = motionY;
        r.attachPlanet = null;
        for (Planet p : planets) {
            if (p.name.equals(attachPlanet)) {
                r.attachPlanet = p;
                break;
            }
        }
        r.controller = controller.load();
        return r;
    }
}

class SerialRocketPart implements Serializable {

    int id, x, y;
    SerialRocketPart left, right, top, bottom;

    public SerialRocketPart(RocketPart r) {
        id = Parts.find(r.sp);
        x = r.sp.getX();
        y = r.sp.getY();
        if (r.top != null) {
            top = new SerialRocketPart(r.top);
        }
        if (r.right != null) {
            right = new SerialRocketPart(r.right);
        }
        if (r.bottom != null) {
            bottom = new SerialRocketPart(r.bottom);
        }
        if (r.left != null) {
            left = new SerialRocketPart(r.left);
        }
    }

    public RocketPart load() {
        RocketPart r = new RocketPart(new Sprite(Parts.parts[id], x, y, Preferences.PART_WIDTH, Preferences.PART_HEIGHT, Jam.frame));
        if (top != null) {
            r.top = top.load();
        }
        if (right != null) {
            r.right = right.load();
        }
        if (bottom != null) {
            r.bottom = bottom.load();
        }
        if (left != null) {
            r.left = left.load();
        }
        return r;
    }
}
