import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;
    private final int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw() {
        StdDraw.point(x, y);
    }

    public void drawTo(Point that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public int compareTo(Point that) {
        if (this.y < that.y) return -1;
        if (this.y > that.y) return 1;
        return Integer.compare(this.x, that.x);
    }

    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (this.y == that.y) return Double.NEGATIVE_INFINITY;
            return Double.POSITIVE_INFINITY;
        }
        if (this.y == that.y) return 0;
        return 1.0 * (that.y - this.y) / (that.x - this.x);
    }

    public Comparator<Point> slopeOrder() {
        return (p1, p2) -> Double.compare(slopeTo(p1), slopeTo(p2));
    }
}
