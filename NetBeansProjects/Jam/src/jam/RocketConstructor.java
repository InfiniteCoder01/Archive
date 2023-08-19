package jam;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class RocketConstructor {

    private List<Sprite> rocket;
    private int err;

    RocketConstructor(List<Sprite> rocket) {
        this.rocket = rocket;
    }

    List<Rocket> construct(double startX, double startY) {
        err = 0;
        List<Rocket> out = new ArrayList<>();
        boolean controller = false;
        for (int i = 0; i < rocket.size(); i++) {
            Sprite sp = rocket.get(i);
            if (Parts.controllers.indexOf(Parts.find(sp)) != -1) {
                controller = true;
                Rocket r = generateRocket(sp);
                if (r != null) {
                    r.place(startX, startY);
                    out.add(r);
                }
            }
        }
        if (!controller) {
            err = 1;
        }
        return out;
    }

    boolean error() {
        return err != 0;
    }

    String what() {
        switch (err) {
            case 1:
                return "Unable to find controller!";
        }
        return "Unknown error in you rocket!";
    }

    private Rocket generateRocket(Sprite controller) {
        RocketPart connected = getConnected(controller, false, true, false, false);
        connected.sp = controller;
        try {
            rocket.remove(rocket.indexOf(connected.sp));
            rocket.remove(rocket.indexOf(connected.top.sp));
            rocket.remove(rocket.indexOf(connected.right.sp));
            rocket.remove(rocket.indexOf(connected.bottom.sp));
            rocket.remove(rocket.indexOf(connected.left.sp));
        } catch (Exception ex) {
        }
        connected = connectedTree(connected.sp);
        return new Rocket(connected);
    }

    private RocketPart connectedTree(Sprite sp) {
        RocketPart connected = getConnected(sp, true, true, true, true);
        connected.sp = sp;
        try {
            rocket.remove(rocket.indexOf(connected.sp));
            rocket.remove(rocket.indexOf(connected.top.sp));
            rocket.remove(rocket.indexOf(connected.right.sp));
            rocket.remove(rocket.indexOf(connected.bottom.sp));
            rocket.remove(rocket.indexOf(connected.left.sp));
        } catch (Exception ex) {
        }
        if (connected.top != null) {
            connected.top = connectedTree(connected.top.sp);
        }
        if (connected.right != null) {
            connected.right = connectedTree(connected.right.sp);
        }
        if (connected.bottom != null) {
            connected.bottom = connectedTree(connected.bottom.sp);
        }
        if (connected.left != null) {
            connected.left = connectedTree(connected.left.sp);
        }
        return connected;
    }

    private RocketPart getConnected(Sprite sp, boolean up, boolean right, boolean down, boolean left) {
        RocketPart out = new RocketPart();
        for (Sprite item : rocket) {
            int diffx = item.getX() - sp.getX();
            int diffy = item.getY() - sp.getY();
            if (diffx + Preferences.PART_WIDTH == 0 && left
                    || diffx - Preferences.PART_WIDTH == 0 && right
                    || diffx == 0 && (up || down)) {
                if (diffy + Preferences.PART_HEIGHT == 0 && up
                        || diffy - Preferences.PART_HEIGHT == 0 && down
                        || diffy == 0 && (left || right)) {
                    if (Math.abs(diffx) != Math.abs(diffy)) {
                        if (diffx > 0) {
                            out.right = new RocketPart(item);
                        } else if (diffx < 0) {
                            out.left = new RocketPart(item);
                        } else if (diffy < 0) {
                            out.top = new RocketPart(item);
                        } else {
                            out.bottom = new RocketPart(item);
                        }
                    }
                }
            }
        }
        return out;
    }

}
