import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class PointSET {

    private final SET<Point2D> points;
    public PointSET() {
        points = new SET<>();
    }
    public boolean isEmpty() {
        return points.isEmpty();
    }
    public int size() {
        return points.size();
    }
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }
    public void draw() {
        for (Point2D p : points) StdDraw.filledCircle(p.x(), p.y(), 0.005);
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> pointsInRect = new ArrayList<>();
        for (Point2D p : points) {
            if (rect.contains(p)) pointsInRect.add(p);
        }
        return pointsInRect;
    }
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        double minDist = Double.POSITIVE_INFINITY;
        Point2D min = null;
        for (Point2D p1 : points) {
            if (p1 != p) {
                double dist = p.distanceSquaredTo(p1);
                if (dist < minDist) {
                    minDist = dist;
                    min = p1;
                }
            }
        }
        return min;
    }

    public static void main(String[] args) {
        PointSET pointSET = new PointSET();
        for (int i = 0; i < 30; i++) {
            Point2D point = new Point2D(StdRandom.uniformDouble(), StdRandom.uniformDouble());
            pointSET.insert(point);
        }
        pointSET.draw();

        StdDraw.setPenColor(StdDraw.BLUE);
        Point2D pointMin = new Point2D(StdRandom.uniformDouble(0, 0.5), StdRandom.uniformDouble(0, 0.5));
        Point2D pointMax = new Point2D(StdRandom.uniformDouble(pointMin.x(), 1), StdRandom.uniformDouble(pointMin.y(), 1));
        RectHV rectHV = new RectHV(pointMin.x(), pointMin.y(), pointMax.x(), pointMax.y());
        rectHV.draw();
        for (Point2D point : pointSET.range(rectHV)) {
            StdDraw.filledCircle(point.x(), point.y(), 0.0075);
        }

        StdDraw.setPenColor(StdDraw.RED);
        Point2D point = new Point2D(StdRandom.uniformDouble(), StdRandom.uniformDouble());
        StdDraw.filledCircle(point.x(), point.y(), 0.0075);
        Point2D nearest = pointSET.nearest(point);
        StdDraw.filledCircle(nearest.x(), nearest.y(), 0.0075);
        StdDraw.circle(point.x(), point.y(), point.distanceTo(nearest));
    }
}
