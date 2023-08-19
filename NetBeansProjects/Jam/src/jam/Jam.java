package jam;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.lang.reflect.*;

/**
 *
 * @author Дима и Мама
 */
public class Jam {

    public static BufferedImage[] backgrounds, buttons;
    public static List<DragAndDrop> inventory;
    public static ScrollList inv;
    public static List<Sprite> rocket;
    public static List<Rocket> rockets;
    public static JFrame frame;
    public static JPanel renderPanel;
    public static int weight = 0, fuel = 0, power = 0, menuID = 0;
    public static boolean engine = false, isSpacePressed = false, spectMode = false, left = false, right = false;
    public static Rocket selected = null;
    public static long spaceTime, saveCooldown;
    public static List<Planet> planets;
    public static List<Planet> planetsVisited;
    public static List<Mod> mods;
    public static final String tutorialText = "Short tutorial:\n"
            + "Press start button, using drag and drop mechanics build simple rocket!\n"
            + "Launch button will be appear, click it!\n"
            + "Using space key you controlling engines power, \n"
            + "using 'A' and 'D' keys you can rotate the rocket!\n"
            + "In bottom-right coner fuel bar, in bottom-left - active and max\n"
            + "power of you engines!\n"
            + "Fly to the planets, stars, and moons, get new components, to build better rockets!\n"
            + "Using 'B' key you can back to the building current rocket, using 'N' build new!\n"
            + "Using 'F' key you can spectate you rocket, using mouse wheel you can zoom, \n"
            + "hold left mouse button and drag the mouse to move the map!\n"
            + "To create backup open folder with you space.jar file and open levels folder.\n"
            + "Clone level.dat file to some folder, to load the backup clone level.dat back.\n"
            + "To edit textures, open folder with you space.jar file and open assets/textures;\n"
            + "here is all textures. In assets/data folder you can find planets.txt and loot.txt.\n"
            + "This files is the mechanics files.\n"
            + "To add mods put jars to the mods folder!";

    public static void main(String[] args) {
        Preferences.SPACE_WIDTH *= loadTexture("space.png").getWidth() / 2048.0;
        Preferences.SPACE_HEIGHT *= loadTexture("space.png").getHeight() / 2048.0;
        Preferences.SPACE_X = Preferences.SPACE_WIDTH / 2;
        Preferences.SPACE_Y = Preferences.SPACE_HEIGHT / 2;
        frame = new JFrame("Space");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        Container pane = frame.getContentPane();
        pane.setLayout(new BorderLayout());
        inv = new ScrollList(frame);
        rockets = new ArrayList<>();
        planetsVisited = new ArrayList<>();
        mods = new ArrayList<>();

        Parts.Load();
        loadAssets();
        loadLevel();
        for (String file : listFiles("mods")) {
            if (file.endsWith(".jar")) {
                mods.add(loadClass("mods/" + file));
            }
        }
        
        for(Mod m : mods) {
            m.load();
        }

        DragAndDrop launchBackground = new DragAndDrop(backgrounds[1], Preferences.SPACE_X, Preferences.SPACE_Y, Preferences.SPACE_WIDTH, Preferences.SPACE_HEIGHT, frame, 1);
        Sprite buildBackground = new Sprite(backgrounds[0], 0, 0, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, frame);
        Sprite launchButton = new Sprite(buttons[0], 0, 0, Preferences.LAUNCH_BUTTON_WIDTH, Preferences.LAUNCH_BUTTON_HEIGHT, frame);
        Sprite startButton = new Sprite(buttons[1], 0, 0, Preferences.LAUNCH_BUTTON_WIDTH * 2, Preferences.LAUNCH_BUTTON_HEIGHT * 2, frame);
        Sprite helpButton = new Sprite(buttons[2], 0, 0, Preferences.LAUNCH_BUTTON_WIDTH * 2, Preferences.LAUNCH_BUTTON_HEIGHT * 2, frame);
        VerticalBar fuelBar = new VerticalBar(20, 0, 100, frame,
                Preferences.SCREEN_WIDTH - Preferences.POWER_SLIDER_WIDTH - 3,
                Preferences.SCREEN_HEIGHT - Preferences.POWER_SLIDER_HEIGHT - 3,
                Preferences.POWER_SLIDER_WIDTH, Preferences.POWER_SLIDER_HEIGHT);

        startButton.setOnClickListener(new PressListener() {
            @Override
            public void clicked() {
                menuID = 1;
                try {
                    LevelObject l = (LevelObject) loadObject("levels/level.dat");
                    l.load();
                } catch (Exception ex) {
                    System.out.println("Can not load state!");
                    Logger.getLogger(Jam.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            @Override
            public void pressed() {
            }

            @Override
            public void released() {
            }
        });

        helpButton.setOnClickListener(new PressListener() {
            @Override
            public void clicked() {
                JOptionPane.showMessageDialog(null, tutorialText, "Short tutorial", JOptionPane.INFORMATION_MESSAGE);
            }

            @Override
            public void pressed() {
            }

            @Override
            public void released() {
            }
        });

        MouseWheelListener mwl = new MouseWheelListener() {
            double scale = 1;

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                Point center = new Point(
                        (int) ((launchBackground.asSprite().getX() * 2 + Preferences.SPACE_WIDTH * scale) / 2),
                        (int) ((launchBackground.asSprite().getY() * 2 + Preferences.SPACE_HEIGHT * scale) / 2));
                double vectorSize = Game.dist(center, new Point(e.getX(), e.getY()));
                double scaleRelative = scale;
                scale *= Math.pow(1.1, e.getUnitsToScroll() * -1);
                scaleRelative = scale / scaleRelative;
                double newVectorSize = vectorSize * scaleRelative;
                launchBackground.asSprite().scale(scale);
                Point newCenter = new Point(
                        (int) ((launchBackground.asSprite().getX() * 2 + Preferences.SPACE_WIDTH * scale) / 2),
                        (int) ((launchBackground.asSprite().getY() * 2 + Preferences.SPACE_HEIGHT * scale) / 2));
                launchBackground.asSprite().translate(
                        center.x - newCenter.x,
                        center.y - newCenter.y);
                center = new Point(
                        (int) ((launchBackground.asSprite().getX() * 2 + Preferences.SPACE_WIDTH * scale) / 2),
                        (int) ((launchBackground.asSprite().getY() * 2 + Preferences.SPACE_HEIGHT * scale) / 2));
                vectorSize = Game.dist(center, new Point(e.getX(), e.getY()));
                center.x = e.getX() - center.x;
                center.y = e.getY() - center.y;
                center = Game.normalize(center);
                center.x *= vectorSize - newVectorSize;
                center.y *= vectorSize - newVectorSize;
                center.x /= 100;
                center.y /= 100;
                launchBackground.asSprite().translate(
                        center.x,
                        center.y);
                for (int i = 0; i < rockets.size(); i++) {
                    Rocket r = rockets.get(i);
                    r.setScale(scale * Preferences.PART_SCALE / Preferences.PART_WIDTH);
                    r.reGenerate();
                    if (r.isAttached) {
                        double addX = 0, addY = 0;
                        r.place(r.attachPlanet.x + r.attachX, r.attachPlanet.y + r.attachY);
                    }
                    r.controller.sp.setPosition(
                            (int) (launchBackground.asSprite().getX() + r.x * scale * (Preferences.SPACE_WIDTH / backgrounds[1].getWidth())),
                            (int) (launchBackground.asSprite().getY() + r.y * scale * (Preferences.SPACE_HEIGHT / backgrounds[1].getHeight())));
                    r.reGenerate();
                }
                for (Planet p : planets) {
                    p.sp.scale(scale);
                    p.sp.setPosition(
                            (int) (launchBackground.asSprite().getX() + p.x * scale * (Preferences.SPACE_WIDTH / backgrounds[1].getWidth())),
                            (int) (launchBackground.asSprite().getY() + p.y * scale * (Preferences.SPACE_HEIGHT / backgrounds[1].getHeight())));
                }
            }
        };
        launchButton.setOnClickListener(new PressListener() {
            @Override
            public void clicked() {
                menuID = 2;
                RocketConstructor c = new RocketConstructor(rocket);
                List<Rocket> applied = c.construct(0, 0);
                rockets.addAll(applied);
                if (c.error()) {
                    JOptionPane.showMessageDialog(null, c.what(), "Bad rocket!", JOptionPane.INFORMATION_MESSAGE);
                    menuID = 1;
                    rockets.removeAll(applied);
                    for (Rocket r : applied) {
                        for (Sprite sp : r.controller.getAllSprites()) {
                            for (DragAndDrop d : inventory) {
                                if (sp == d.asSprite()) {
                                    d.dragEnabled(true);
                                }
                            }
                        }
                    }
                } else {
                    for (Rocket r : rockets) {
                        for (Sprite sp : r.controller.getAllSprites()) {
                            for (DragAndDrop d : inventory) {
                                if (sp == d.asSprite()) {
                                    d.dragEnabled(false);
                                }
                            }
                        }
                        r.isAttached = true;
                        for (Planet planet : planets) {
                            if (planet.isStartPlanet) {
                                r.attachPlanet = planet;
                                r.attachX = planet.attachX;
                                r.attachY = planet.attachY;
                                break;
                            }
                        }
                    }
                    selected = applied.get(0);
                }
            }

            @Override
            public void pressed() {
            }

            @Override
            public void released() {
            }
        });
        frame.addMouseWheelListener(mwl);
        launchBackground.enableEffects(false);
        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                switch (e.getKeyChar()) {
                    case 'f':
                        spectMode = !spectMode;
                        break;
                    case 'b':
                        rockets.remove(selected);
                        for (Sprite sp : selected.controller.getAllSprites()) {
                            if (Parts.poweredEnginesTextures.contains(sp.getTexture())) {
                                addToInventory(Parts.unpoweredEnginesTextures.get(Parts.poweredEnginesTextures.indexOf(sp.getTexture())));
                            } else {
                                addToInventory(sp.getTexture());
                            }
                        }
                        for (int i = 0; i < inventory.size(); i++) {
                            DragAndDrop d = inventory.get(i);
                            if (inv.indexOf(d) == -1) {
                                inventory.remove(d);
                            }
                        }
                        try {
                            selected = rockets.get(rockets.size() - 1);
                        } catch (Exception ex) {
                            selected = null;
                        }
                    case 'n':
                        menuID = 1;
                        break;
                }
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    isSpacePressed = true;
                }
                if (e.getKeyChar() == 'a') {
                    left = true;
                }
                if (e.getKeyChar() == 'd') {
                    right = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyChar() == ' ') {
                    isSpacePressed = false;
                }
                if (e.getKeyChar() == 'a') {
                    left = false;
                }
                if (e.getKeyChar() == 'd') {
                    right = false;
                }
            }

        });

        renderPanel = new JPanel() {
            private void drawBuild(Graphics2D g2) {
                inv.refresh();
                recalculateWeight();
                buildBackground.draw(g2);
                for (DragAndDrop item : inventory) {
                    item.draw(g2);
                }
                String info = "Weight: " + weight + ", fuel: " + fuel + ", power: " + power;
                if (engine) {
                    launchButton.setPosition(Preferences.SCREEN_WIDTH - 6 * info.length() - Preferences.LAUNCH_BUTTON_WIDTH - 6, Preferences.SCREEN_HEIGHT - 19);
                    launchButton.draw(g2);
                }
                g2.drawString(info, Preferences.SCREEN_WIDTH - 6 * info.length() - 5, Preferences.SCREEN_HEIGHT - 5);
                if (System.currentTimeMillis() - saveCooldown > Preferences.SAVE_TIME) {
                    saveLevel();
                    saveCooldown = System.currentTimeMillis();
                }
            }

            private void drawLaunch(Graphics2D g2) {
                launchBackground.draw(g2);
                if (selected == null) {
                    menuID = 1;
                    return;
                }
                int xs = Preferences.SCREEN_WIDTH / 2 - selected.getCenter().x;
                int ys = Preferences.SCREEN_HEIGHT / 2 - selected.getCenter().y;
                if (spectMode) {
                    launchBackground.drag(xs, ys);
                }
                mwl.mouseWheelMoved(new MouseWheelEvent(frame, 0, 0, 0, 0, 0, 0, false, 0, 0, 0));
                fuelBar.setMax(selected.maxFuel);
                fuelBar.setValue(selected.fuel);
                if (System.nanoTime() - spaceTime > Preferences.POWER_SPEED) {
                    selected.activePower += isSpacePressed && selected.fuel > 0 ? 1 : -2;
                    selected.activePower = Math.max(0, Math.min(selected.activePower, selected.power));
                    selected.motion();
                    if (left) {
                        selected.rotate(-Preferences.TURNANGLE);
                    } else if (right) {
                        selected.rotate(Preferences.TURNANGLE);
                    }
                    spaceTime = System.nanoTime();
                }
                for (Planet planet : planets) {
                    planet.draw(g2);
                    for (int x = 0; x < planet.map.getWidth(); x++) {
                        for (int y = 0; y < planet.map.getHeight(); y++) {
                            if (new Color(planet.map.getRGB(x, y)).equals(Color.BLACK)) {
                                Point pixel = new Point(
                                        planet.sp.getW() / planet.map.getWidth() * x + planet.sp.getX(),
                                        planet.sp.getH() / planet.map.getHeight() * y + planet.sp.getY()
                                );
                                for (Rocket r : rockets) {
                                    boolean collides = false;
                                    for (Sprite sp : r.controller.getAllSprites()) {
                                        if (Game.collide(pixel,
                                                new Point(sp.getX(), sp.getY()),
                                                new Point(sp.getW(), sp.getH()))) {
                                            collides = true;
                                            break;
                                        }
                                    }
                                    if (collides && r.attachPlanet != planet) {
                                        r.isAttached = true;
                                        r.attachPlanet = planet;
                                        r.attachX = planet.attachX;
                                        r.attachY = planet.attachY;
                                        r.activePower = 0;
                                        if (planetsVisited.indexOf(planet) == -1) {
                                            planetsVisited.add(planet);
                                            for (int id : planet.loot) {
                                                addToInventory(Parts.parts[id]);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                for (Rocket r : rockets) {
                    r.draw(g2);
                }
                if (spectMode) {
                    g2.translate(-xs, -ys);
                }
                fuelBar.draw(g2);
                g2.setColor(Color.BLUE);
                g2.drawString("Power: " + selected.activePower + "/" + selected.power, 5, Preferences.SCREEN_HEIGHT - 5);
                g2.setColor(Color.BLACK);
                if (System.currentTimeMillis() - saveCooldown > Preferences.SAVE_TIME) {
                    saveLevel();
                    saveCooldown = System.currentTimeMillis();
                }
            }

            public void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(Color.BLACK);
                g2.fillRect(0, 0, getWidth(), getHeight());
                // rendering magic will happen here
                switch (menuID) {
                    case 0:
                        drawMenu(g2);
                        break;
                    case 1:
                        drawBuild(g2);
                        break;
                    case 2:
                        drawLaunch(g2);
                        break;
                }
                this.repaint();
            }

            private void drawMenu(Graphics2D g2) {
                BufferedImage logo = loadTexture("logo.png");
                g2.drawImage(loadTexture("menu.png"), 0, 0, Preferences.SCREEN_WIDTH, Preferences.SCREEN_HEIGHT, null);
                g2.drawImage(logo, Preferences.SCREEN_WIDTH / 2 - logo.getWidth() / 2, 0, null);
                startButton.setPosition(Preferences.SCREEN_WIDTH / 2 - Preferences.LAUNCH_BUTTON_WIDTH,
                        Preferences.SCREEN_HEIGHT / 2 - Preferences.LAUNCH_BUTTON_HEIGHT);
                startButton.draw(g2);
                helpButton.setPosition(Preferences.SCREEN_WIDTH / 2 - Preferences.LAUNCH_BUTTON_WIDTH, (int) (Preferences.SCREEN_HEIGHT / 2 + Preferences.LAUNCH_BUTTON_HEIGHT * 1.5));
                helpButton.draw(g2);
            }
        };

        pane.add(renderPanel, BorderLayout.CENTER);
        frame.setSize(Preferences.SCREEN_WIDTH + 6, Preferences.SCREEN_HEIGHT + 28);
        frame.setVisible(true);
    }

    private static void loadAssets() {
        backgrounds = new BufferedImage[2];
        buttons = new BufferedImage[3];
        backgrounds[0] = loadTexture("buildBackground.png");
        buttons[0] = loadTexture("launchButton.png");
        buttons[1] = loadTexture("startBtn.png");
        buttons[2] = loadTexture("helpBtn.png");
        loadPlanets();
        loadLoot();
    }

    public static BufferedImage loadTexture(String filename) {
        try {
            return ImageIO.read(new File("assets/textures/" + filename));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Error loading texture: " + filename + ", try to reinstall app!", "Error loading assets!", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
        return null;
    }

    public static void loadLevel() {
        inventory = new ArrayList<>();
        addToInventory(Parts.parts[Parts.getID(Parts.IDs.CONTROLLER_HUMAN)]);
        addToInventory(Parts.parts[Parts.getID(Parts.IDs.FUEL_BIG)]);
        addToInventory(Parts.parts[Parts.getID(Parts.IDs.ENGINE_BIG)]);
    }

    public static void addToInventory(BufferedImage img) {
        DragAndDrop item = new DragAndDrop(img, 0, inventory.size() * (Preferences.PART_HEIGHT + 3), Preferences.PART_WIDTH, Preferences.PART_HEIGHT, frame, Preferences.PART_PRECICTION);
        item.setOnDragDetectedListener(new DragListener() {
            @Override
            public void onDragged() {
                for (DragAndDrop other : inventory) {
                    if (other != item && other.isDragging()) {
                        item.releaseDrag();
                        return;
                    }
                }
                if (inv.indexOf(item) != -1) {
                    inv.remove(inv.indexOf(item));
                }
                int idx = inventory.indexOf(item);
                if (idx > -1) {
                    inventory.remove(idx);
                }
                inventory.add(item);
            }

            @Override
            public void onDropped(int dragX, int dragY) {
                if (item.asSprite().getX() == 0) {
                    if (inv.indexOf(item) == -1) {
                        inv.rawAdd(item);
                    }
                } else {
                    for (DragAndDrop other : inventory) {
                        if (other.asSprite().getX() == item.asSprite().getX()
                                && other.asSprite().getY() == item.asSprite().getY()
                                && other != item) {
                            item.asSprite().translate(-dragX, -dragY);
                            item.releaseDrag();
                        }
                    }
                }
                recalculateWeight();
            }

            @Override
            public void onMoved(int dragX, int dragY) {
            }

        });
        inventory.add(item);
        inv.add(item);
    }

    private static void recalculateWeight() {
        rocket = new ArrayList<>();
        weight = 0;
        fuel = 0;
        power = 0;
        engine = false;
        for (DragAndDrop item : inventory) {
            if (inv.indexOf(item) == -1) {
                rocket.add(item.asSprite());
                weight += Parts.weight[Parts.find(item.asSprite())];
                fuel += Parts.fuel[Parts.find(item.asSprite())];
                power += Parts.power[Parts.find(item.asSprite())];
                if (Parts.engines.indexOf(Parts.find(item.asSprite())) != -1) {
                    engine = true;
                }
            }
        }
    }

    public static int getWeight(Rocket r) {
        List<Sprite> sprites = r.controller.getAllSprites();
        int weight = 0;
        for (Sprite sp : sprites) {
            weight += Parts.weight[Parts.find(sp)];
        }
        return weight;
    }

    public static int getFuel(Rocket r) {
        List<Sprite> sprites = r.controller.getAllSprites();
        int fuel = 0;
        for (Sprite sp : sprites) {
            fuel += Parts.fuel[Parts.find(sp)];
        }
        return fuel;
    }

    public static int getPower(Rocket r) {
        List<Sprite> sprites = r.controller.getAllSprites();
        int power = 0;
        for (Sprite sp : sprites) {
            power += Parts.power[Parts.find(sp)];
        }
        return power;
    }

    private static void loadPlanets() {
        backgrounds[1] = loadTexture("space.png");
        try {
            Scanner s = new Scanner(new File("assets/data/planets.txt"));
            String data = "";
            while (s.hasNextLine()) {
                String next = s.nextLine();
                data += next.charAt(0) == '#' ? "" : next;
            }
            planets = new ArrayList<>();
            String[] datas = data.split(",");
            for (String planet : datas) {
                planets.add(new Planet(planet, frame));
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error loading planets.txt, try to reinstall app!", "Error loading assets!", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    private static void loadLoot() {
        try {
            Scanner s = new Scanner(new File("assets/data/loot.txt"));
            String data = "";
            while (s.hasNextLine()) {
                String next = s.nextLine();
                data += next.charAt(0) == '#' ? "" : next;
            }
            String[] datas = data.split(";");
            for (String lt : datas) {
                String[] vals = lt.split(":");
                for (Planet p : planets) {
                    if (p.name.equals(vals[0].trim().substring(1, vals[0].trim().length() - 1))) {
                        p.loot = new ArrayList<>();
                        for (String id : vals[1].trim().substring(1, vals[1].trim().length() - 1).split(",")) {
                            try {
                                p.loot.add(Integer.parseInt(id.trim()));
                            } catch (NumberFormatException ex) {
                            }
                        }
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, "Error loading loot.txt, try to reinstall app!", "Error loading assets!", JOptionPane.ERROR_MESSAGE);
            System.exit(-1);
        }
    }

    public static void saveObject(String filename, Object o) {
        try {
            if (!new File(filename).exists()) {
                new File(filename).createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filename);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(o);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(Jam.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Object loadObject(String filename) throws IOException {
        try {
            FileInputStream fin = new FileInputStream(filename);
            ObjectInputStream ois = new ObjectInputStream(fin);
            Object o = ois.readObject();
            ois.close();
            return o;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Jam.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static void saveLevel() {
        saveObject("levels/level.dat", new LevelObject());
    }

    public static Mod loadClass(String filename) {
        try {
            JarFile jarFile = new JarFile(filename);
            Enumeration<JarEntry> e = jarFile.entries();

            URL[] urls = {new URL("jar:file:" + filename + "!/")};
            URLClassLoader cl = URLClassLoader.newInstance(urls);

            while (e.hasMoreElements()) {
                JarEntry je = e.nextElement();
                if (je.isDirectory() || !je.getName().endsWith(".class")) {
                    continue;
                }
                // -6 because of .class
                String className = je.getName().substring(0, je.getName().length() - 6);
                className = className.replace('/', '.');
                Class c = cl.loadClass(className);
                Method m[] = c.getDeclaredMethods();
                for (int i = 0; i < m.length; i++) {
                    if(m[i].getName().equals("Load")) {
                        return new Mod(c);
                    }
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Jam.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static List<String> listFiles(String dir) {
        return Stream.of(new File(dir).listFiles())
                .filter(file -> !file.isDirectory())
                .map(File::getName).collect(Collectors.toList());
    }
}
