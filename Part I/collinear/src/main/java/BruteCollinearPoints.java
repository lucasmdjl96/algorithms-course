import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class BruteCollinearPoints {

    private final LineSegment[] segments;
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length - 1; i++) {
            for (int j = i + 1; j < points.length; j++) {
                if (points[i].compareTo(points[j]) == 0) throw new IllegalArgumentException();
            }
        }

        List<LineSegment> segmentList = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int r = k + 1; r < points.length; r++) {
                        if (collinear(points[i], points[j], points[k], points[r])) {
                            segmentList.add(group(new Point[] {points[i], points[j], points[k], points[r]}));
                        }
                    }
                }
            }
        }
        segments = segmentList.toArray(new LineSegment[0]);
    }

    private static boolean collinear(Point p0, Point p1, Point p2, Point p3) {
        Comparator<Point> slopeComparatorP0 = p0.slopeOrder();
        if (slopeComparatorP0.compare(p1, p2) != 0) return false;
        return slopeComparatorP0.compare(p1, p3) == 0;
    }

    private static LineSegment group(Point[] collinearPoints) {
        Arrays.sort(collinearPoints);
        return new LineSegment(collinearPoints[0], collinearPoints[collinearPoints.length - 1]);
    }

    public int numberOfSegments() { return segments.length; }

    public LineSegment[] segments() { return Arrays.copyOf(segments, segments.length); }

}
