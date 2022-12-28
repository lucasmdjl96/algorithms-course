import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class FastCollinearPoints {

    private final LineSegment[] segments;
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i] == null) throw new IllegalArgumentException();
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }
        VPoint[] vPoints = new VPoint[points.length];
        VPoint[] vPointsCopy = new VPoint[points.length];
        for (int i = 0; i < points.length; i++) {
            VPoint vPoint = new VPoint(points[i]);
            vPoints[i] = vPoint;
            vPointsCopy[i] = vPoint;
        }
        List<LineSegment> segmentList = new ArrayList<>();
        for (VPoint p : vPoints) {
            Arrays.sort(vPointsCopy, p.slopeOrder());
            double lastSlope = Double.NaN;
            List<VPoint> currentPoints = new ArrayList<>(100);
            boolean pAdded = false;
            boolean skip = false;

            for (int i = 1; i < vPointsCopy.length; i++) {
                double newSlope = p.slopeTo(vPointsCopy[i]);
                if (skip && newSlope == lastSlope) continue;
                if (newSlope != lastSlope) {
                    if (!pAdded) currentPoints.add(p);
                    if (currentPoints.size() >= 4 && !skip) {
                        segmentList.add(group(currentPoints));
                    }
                    currentPoints = new ArrayList<>(100);
                    lastSlope = newSlope;
                    skip = false;
                    pAdded = false;
                }
                VPoint newPoint = vPointsCopy[i];
                if (!pAdded && newPoint.compareTo(p) > 0) {
                    currentPoints.add(p);
                    pAdded = true;
                }
                currentPoints.add(newPoint);
                if (newPoint.visited) skip = true;
            }

            if (!pAdded) currentPoints.add(p);
            if (currentPoints.size() >= 4 && !skip) {
                segmentList.add(group(currentPoints));
            }
            p.visited = true;
        }
        this.segments = segmentList.toArray(new LineSegment[0]);
    }

    private static class VPoint implements Comparable<VPoint> {
        public boolean visited;
        private final Point point;
        public VPoint(Point point) {
            this.point = point;
            visited = false;
        }

        public double slopeTo(VPoint that) {
            return this.point.slopeTo(that.point);
        }

        @Override
        public int compareTo(VPoint that) {
            return this.point.compareTo(that.point);
        }

        public Comparator<VPoint> slopeOrder() {
            return (p1, p2) -> {
                int compareSlope = Double.compare(slopeTo(p1), slopeTo(p2));
                if (compareSlope != 0) return compareSlope;
                else return p1.compareTo(p2);
            };
        }
    }

    private static LineSegment group(List<VPoint> collinearPoints) {
        return new LineSegment(collinearPoints.get(0).point, collinearPoints.get(collinearPoints.size() - 1).point);
    }

    public int numberOfSegments() { return segments.length; }

    public LineSegment[] segments() { return Arrays.copyOf(segments, segments.length); }

    public static void main(String[] args) {
        Point[] points = new Point[5000];
        for (int i = 0; i < 5000; i++) {
            points[i] = new Point(StdRandom.uniformInt(10000), StdRandom.uniformInt(10000));
        }
        System.out.println(new FastCollinearPoints(points).numberOfSegments());
    }

}
