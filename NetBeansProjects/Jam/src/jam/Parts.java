package jam;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parts {

    public static int[] weight, fuel, power;
    public static List<Integer> engines;
    public static List<Integer> controllers;
    public static BufferedImage[] parts;
    public static List<BufferedImage> poweredEnginesTextures;
    public static List<BufferedImage> unpoweredEnginesTextures;

    public static void Load() {
        parts = new BufferedImage[IDs.values().length];
        weight = new int[IDs.values().length];
        fuel = new int[IDs.values().length];
        power = new int[IDs.values().length];
        engines = new ArrayList<>();
        controllers = new ArrayList<>();
        poweredEnginesTextures = new ArrayList<>();
        unpoweredEnginesTextures = new ArrayList<>();

        weight[getID(IDs.FUEL_BIG)] = 10;
        weight[getID(IDs.FUEL_ULTRA)] = 12;
        weight[getID(IDs.ENGINE_BIG)] = 7;
        weight[getID(IDs.ENGINE_ULTRA)] = 20;
        weight[getID(IDs.CONTROLLER_HUMAN)] = 5;

        fuel[getID(IDs.FUEL_BIG)] = 150;
        fuel[getID(IDs.FUEL_ULTRA)] = 300;
        fuel[getID(IDs.ENGINE_ULTRA)] = 50;

        power[getID(IDs.ENGINE_BIG)] = 50;
        power[getID(IDs.ENGINE_ULTRA)] = 100;

        /*Textures*/
        parts[getID(Parts.IDs.ENGINE_BIG)] = Jam.loadTexture("engineBig.png");
        parts[getID(Parts.IDs.ENGINE_ULTRA)] = Jam.loadTexture("engineUltra.png");
        parts[getID(Parts.IDs.FUEL_BIG)] = Jam.loadTexture("fuelTankBig.png");
        parts[getID(Parts.IDs.FUEL_ULTRA)] = Jam.loadTexture("fuelTankUltra.png");
        parts[getID(Parts.IDs.CONTROLLER_HUMAN)] = Jam.loadTexture("humanController.png");

        engines.add(getID(IDs.ENGINE_BIG));
        engines.add(getID(IDs.ENGINE_ULTRA));
        poweredEnginesTextures.add(Jam.loadTexture("engineBigPowered.png"));
        poweredEnginesTextures.add(Jam.loadTexture("engineUltraPowered.png"));
        unpoweredEnginesTextures.add(parts[getID(Parts.IDs.ENGINE_BIG)]);
        unpoweredEnginesTextures.add(parts[getID(Parts.IDs.ENGINE_ULTRA)]);

        controllers.add(getID(IDs.CONTROLLER_HUMAN));
    }

    public static int getID(IDs id) {
        return Arrays.asList(IDs.values()).indexOf(id);
    }

    static int find(Sprite item) {
        if(poweredEnginesTextures.indexOf(item.getTexture()) != -1) {
            return Arrays.asList(parts).indexOf(Parts.unpoweredEnginesTextures.get(Parts.poweredEnginesTextures.indexOf(item.getTexture())));
        }
        return Arrays.asList(parts).indexOf(item.getTexture());
    }

    static IDs findID(Sprite item) {
        return IDs.values()[find(item)];
    }

    public static enum IDs {
        ENGINE_BIG, ENGINE_ULTRA,
        FUEL_BIG, FUEL_ULTRA,
        CONTROLLER_HUMAN
    }
}
