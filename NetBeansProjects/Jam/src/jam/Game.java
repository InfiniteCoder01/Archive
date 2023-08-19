package jam;

import java.awt.Point;

public class Game {
    public static boolean collide(Point a, Point b, Point size) {
        return inRange(a.x, b.x, b.x + size.x) &&
                inRange(a.y, b.y, b.y + size.y);
    }

    public static boolean inRange(int v, int min, int max) {
        return (v <= max) && (v >= min);
    }
    
    public static int align(int v, int prec) {
        return (int) (Math.round(v * 1.0 / prec) * prec);
    }

    public static double len(Point p) {
        return Math.sqrt(Math.pow(p.x, 2) + Math.pow(p.y, 2));
    }

    public static double dist(Point a, Point b) {
        return len(new Point(a.x - b.x, a.y - b.y));
    }

    public static Point normalize(Point vec) {
        double v = len(vec);
        return new Point((int) (vec.x / v * 100), (int) (vec.y / v * 100));
    }

    static int diff(int a, int b) {
        return Math.abs(a - b);
    }

    static int map(int v, int minv, int maxv, int min, int max) {
        return (int) ((v - minv) / (double) (maxv - minv) * (max - min) + min);
    }
}
