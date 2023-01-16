import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class KdTree {

    private static class Node {
        public Point2D point;
        public Node left;
        public Node right;
        public boolean vertical;
        public Node(Point2D point, boolean vertical) {
            if (point == null) throw new IllegalArgumentException();
            this.point = point;
            this.vertical = vertical;
        }
    }

    private int size;
    private Node root;

    public KdTree() {
        root = null;
        size = 0;
    }
    public boolean isEmpty() {
        return root == null;
    }
    public int size() {
        if (isEmpty()) return 0;
        return size;
    }
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = insert(p, root, true);
    }
    private Node insert(Point2D p, Node node, boolean vertical) {
        if (node == null) {
            size++;
            return new Node(p, vertical);
        }
        if (p.equals(node.point)) return node;
        int compare;
        if (node.vertical) {
            compare = Double.compare(p.x(), node.point.x());
        } else {
            compare = Double.compare(p.y(), node.point.y());
        }
        if (compare >= 0) {
            node.right = insert(p, node.right, !node.vertical);
        } else {
            node.left = insert(p, node.left, !node.vertical);
        }
        return node;
    }
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node current = root;
        while (current != null) {
            int compare;
            if (current.vertical) {
                compare = Double.compare(p.x(), current.point.x());
            } else {
                compare = Double.compare(p.y(), current.point.y());
            }
            if (p.equals(current.point)) {
                break;
            } else if (compare >= 0) {
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return current != null;
    }
    private static class Line2D {
        double x1;
        double y1;
        double x2;
        double y2;

        public Line2D(double x1, double y1, double x2, double y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public double getX1() {
            return x1;
        }

        public double getY1() {
            return y1;
        }

        public double getX2() {
            return x2;
        }

        public double getY2() {
            return y2;
        }
    }
    public void draw() {
        Queue<Node> queue = new Queue<>();
        queue.enqueue(root);
        List<Line2D> horizontalLines = new ArrayList<>();
        List<Line2D> verticalLines = new ArrayList<>();
        while (!queue.isEmpty()) {
            Node node = queue.dequeue();
            if (node.left != null) queue.enqueue(node.left);
            if (node.right != null) queue.enqueue(node.right);
            node.point.draw();
            StdDraw.filledCircle(node.point.x(), node.point.y(), 0.005);
            double min = 0;
            double max = 1;
            if (node.vertical) {
                for (Line2D line : horizontalLines) {
                    if (node.point.x() >= line.getX1() && node.point.x() <= line.getX2()) {
                        if (line.getY1() > min && line.getY1() <= node.point.y()) min = line.getY1();
                        else if (line.getY1() > node.point.y() && line.getY1() <= max) max = line.getY1();
                    }
                }
                verticalLines.add(new Line2D(node.point.x(), min, node.point.x(), max));
                StdDraw.line(node.point.x(), min, node.point.x(), max);
            } else {
                for (Line2D line : verticalLines) {
                    if (node.point.y() >= line.getY1() && node.point.y() <= line.getY2()) {
                        if (line.getX1() > min && line.getX1() <= node.point.x()) min = line.getX1();
                        else if (line.getX1() > node.point.x() && line.getX1() <= max) max = line.getX1();
                    }
                }
                horizontalLines.add(new Line2D(min, node.point.y(), max, node.point.y()));
                StdDraw.line(min, node.point.y(), max, node.point.y());
            }
        }
    }
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        List<Point2D> points = new ArrayList<>();
        range(rect, root, points);
        return points;
    }
    private void range(RectHV rect, Node node, List<Point2D> points) {
        if (node == null) return;
        if (node.vertical) {
            if (node.point.x() > rect.xmax()) range(rect, node.left, points);
            else if (node.point.x() < rect.xmin()) range(rect, node.right, points);
            else {
                if (rect.contains(node.point)) points.add(node.point);
                range(rect, node.left, points);
                range(rect, node.right, points);
            }
        } else {
            if (node.point.y() > rect.ymax()) range(rect, node.left, points);
            else if (node.point.y() < rect.ymin()) range(rect, node.right, points);
            else {
                if (rect.contains(node.point)) points.add(node.point);
                range(rect, node.left, points);
                range(rect, node.right, points);
            }
        }
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return nearest(p, root, new double[] {Double.POSITIVE_INFINITY});
    }

    private Point2D nearest(Point2D p, Node node, double[] best) {
        if (node == null) return null;
        best[0] = Math.min(best[0], p.distanceSquaredTo(node.point));
        int compare;
        Point2D point;
        if (node.vertical) {
            compare = Double.compare(p.x(), node.point.x());
            if (compare < 0) {
                point = nearest(p, node.left, best);
                if (point == null) point = node.point;
                double dist = p.distanceSquaredTo(point);
                if (dist > p.distanceSquaredTo(node.point)) point = node.point;
                if (best[0] > Math.pow(node.point.x() - p.x(), 2.0)) {
                    Point2D point2 = nearest(p, node.right, best);
                    if (point2 != null && p.distanceSquaredTo(point2) < p.distanceSquaredTo(point)) point = point2;
                }
            } else if (compare > 0) {
                point = nearest(p, node.right, best);
                if (point == null) point = node.point;
                double dist = p.distanceSquaredTo(point);
                if (dist > p.distanceSquaredTo(node.point)) point = node.point;
                if (best[0] > Math.pow(p.x() - node.point.x(), 2.0)) {
                    Point2D point2 = nearest(p, node.left, best);
                    if (point2 != null && p.distanceSquaredTo(point2) < p.distanceSquaredTo(point)) point = point2;
                }
            } else {
                point = nearest(p, node.left, best);
                if (point == null) point = node.point;
                if (p.distanceSquaredTo(point) > p.distanceSquaredTo(node.point)) point = node.point;
                Point2D point2 = nearest(p, node.right, best);
                if (point2 != null && p.distanceSquaredTo(point2) < p.distanceSquaredTo(point))
                    point = point2;
            }
        } else {
            compare = Double.compare(p.y(), node.point.y());
            if (compare < 0) {
                point = nearest(p, node.left, best);
                if (point == null) point = node.point;
                double dist = p.distanceSquaredTo(point);
                if (dist > p.distanceSquaredTo(node.point)) point = node.point;
                if (best[0] > Math.pow(node.point.y() - p.y(), 2.0)) {
                    Point2D point2 = nearest(p, node.right, best);
                    if (point2 != null && p.distanceSquaredTo(point2) < p.distanceSquaredTo(point)) point = point2;
                }
            } else if (compare > 0) {
                point = nearest(p, node.right, best);
                if (point == null) point = node.point;
                double dist = p.distanceSquaredTo(point);
                if (dist > p.distanceSquaredTo(node.point)) point = node.point;
                if (best[0] > Math.pow(p.y() - node.point.y(), 2.0)) {
                    Point2D point2 = nearest(p, node.left, best);
                    if (point2 != null && p.distanceSquaredTo(point2) < p.distanceSquaredTo(point)) point = point2;
                }
            } else {
                point = nearest(p, node.left, best);
                if (point == null) point = node.point;
                if (p.distanceSquaredTo(point) > p.distanceSquaredTo(node.point)) point = node.point;
                Point2D point2 = nearest(p, node.right, best);
                if (point2 != null && p.distanceSquaredTo(point2) < p.distanceSquaredTo(point))
                    point = point2;
            }
        }
        return point;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        for (int i = 0; i < 30; i++) {
            Point2D point = new Point2D(StdRandom.uniformDouble(), StdRandom.uniformDouble());
            kdTree.insert(point);
        }
        kdTree.draw();

        StdDraw.setPenColor(StdDraw.BLUE);
        Point2D pointMin = new Point2D(StdRandom.uniformDouble(0, 0.5), StdRandom.uniformDouble(0, 0.5));
        Point2D pointMax = new Point2D(StdRandom.uniformDouble(pointMin.x(), 1), StdRandom.uniformDouble(pointMin.y(), 1));
        RectHV rectHV = new RectHV(pointMin.x(), pointMin.y(), pointMax.x(), pointMax.y());
        rectHV.draw();
        for (Point2D point : kdTree.range(rectHV)) {
            StdDraw.filledCircle(point.x(), point.y(), 0.0075);
        }

        StdDraw.setPenColor(StdDraw.RED);
        Point2D point = new Point2D(StdRandom.uniformDouble(), StdRandom.uniformDouble());
        StdDraw.filledCircle(point.x(), point.y(), 0.0075);
        Point2D nearest = kdTree.nearest(point);
        StdDraw.filledCircle(nearest.x(), nearest.y(), 0.0075);
        StdDraw.circle(point.x(), point.y(), point.distanceTo(nearest));
    }

}
