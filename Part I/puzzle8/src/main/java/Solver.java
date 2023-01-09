import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;

public class Solver {

    private static class Node {
        public final Board board;
        public final Node parent;
        public final int depth;
        public final int distance;

        public Node(Board board, Node parent) {
            this.board = board;
            this.parent = parent;
            if (parent == null) depth = 0;
            else depth = parent.depth + 1;
            distance = board.manhattan();
        }
    }

    private Node solution;

    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();

        MinPQ<Node> goodPQueue = new MinPQ<>(Comparator.comparingInt(node -> node.distance + node.depth));
        goodPQueue.insert(new Node(initial, null));

        MinPQ<Node> badPQueue = new MinPQ<>(Comparator.comparingInt(node -> node.distance + node.depth));
        badPQueue.insert(new Node(initial.twin(), null));

        while (true) {
            Node goodNode = goodPQueue.delMin();
            Board goodBoard = goodNode.board;
            if (goodBoard.isGoal()) {
                solution = goodNode;
                break;
            }
            for (Board neighBoard : goodBoard.neighbors()) {
                if (goodNode.parent == null || !neighBoard.equals(goodNode.parent.board))
                    goodPQueue.insert(new Node(neighBoard, goodNode));
            }


            Node badNode = badPQueue.delMin();
            Board badBoard = badNode.board;
            if (badBoard.isGoal()) {
                break;
            }
            for (Board neighBoard : badBoard.neighbors()) {
                if (badNode.parent == null || !neighBoard.equals(badNode.parent.board))
                    badPQueue.insert(new Node(neighBoard, badNode));
            }
        }
    }

    public boolean isSolvable() {
        return solution != null;
    }

    public int moves() {
        if (!isSolvable()) return -1;
        else return solution.depth;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> stack = new Stack<>();
        Node current = solution;
        while (current != null) {
            stack.push(current.board);
            current = current.parent;
        }
        return stack;
    }

    public static void main(String[] args) {
        int[][] tiles = new int[3][3];
        int index = 0;
        int[] values = new int[] {1, 0, 2, 7, 5, 4, 8, 6, 3};
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                tiles[row][col] = values[index++];
            }
        }
        Board initial = new Board(tiles);
        Solver solver = new Solver(initial);

        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                System.out.println(board);
        }
    }


}
