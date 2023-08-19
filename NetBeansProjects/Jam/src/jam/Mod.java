package jam;

import java.awt.image.BufferedImage;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Mod {
    public static int id = 0;
    Class c;
    Method load;

    Mod(Class c) {
        this.c = c;
        Method m[] = c.getDeclaredMethods();
        for (int i = 0; i < m.length; i++) {
            if (m[i].getName().equals("Load")) {
                load = m[i];
            }
        }
    }

    public void load() {
        try {
            Loaded l = (Loaded) load.invoke(c.newInstance());
            for (Loaded.Part p : l.registeredParts) {
                BufferedImage[] parts = Arrays.copyOf(Parts.parts, Parts.parts.length + 1);
                Parts.parts = Arrays.copyOf(parts, parts.length);
                Parts.parts[Parts.IDs.values().length + Mod.id] = Jam.loadTexture(p.texture);
                if(p.isController) {
                    Parts.controllers.add(Parts.IDs.values().length + Mod.id);
                }
                if(p.isEngine) {
                    Parts.engines.add(Parts.IDs.values().length + Mod.id);
                    Parts.poweredEnginesTextures.add(Jam.loadTexture(p.poweredTexture));
                    Parts.unpoweredEnginesTextures.add(Parts.parts[Parts.IDs.values().length + Mod.id]);
                }
                int[] weight = Arrays.copyOf(Parts.weight, Parts.weight.length + 1);
                Parts.weight = Arrays.copyOf(weight, weight.length);
                int[] fuel = Arrays.copyOf(Parts.fuel, Parts.fuel.length + 1);
                Parts.fuel = Arrays.copyOf(fuel, fuel.length);
                int[] power = Arrays.copyOf(Parts.power, Parts.power.length + 1);
                Parts.power = Arrays.copyOf(power, power.length);
                Parts.weight[Parts.IDs.values().length + Mod.id] = p.weight;
                Parts.fuel[Parts.IDs.values().length + Mod.id] = p.fuel;
                Parts.power[Parts.IDs.values().length + Mod.id] = p.power;
                Mod.id++;
            }
        } catch (Exception ex) {
            Logger.getLogger(Mod.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
