import java.util.ArrayList;
import java.util.List;

public class Board {

    private final int[][] tiles;
    private final int dimension;
    private int zRow;
    private int zCol;

    public Board(int[][] tiles) {
        this.dimension = tiles.length;
        this.tiles = new int[dimension][dimension];
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                this.tiles[row][col] = tiles[row][col];
                if (tiles[row][col] == 0) {
                    zRow = row;
                    zCol = col;
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(dimension()).append('\n');
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                stringBuilder.append(' ').append(tiles[row][col]).append(' ');
            }
            stringBuilder.append('\n');
        }
        return stringBuilder.toString();
    }

    public int dimension() {
        return dimension;
    }

    public int hamming() {
        int result = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int tile = tiles[row][col];
                if (tile != 0 && tile != 1 + row * dimension + col) result++;
            }
        }
        return result;
    }

    public int manhattan() {
        int result = 0;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                int tile = tiles[row][col];
                if (tile != 0)
                    result += Math.abs(row - (tile - 1) / dimension) + Math.abs(col - (tile - 1) % dimension);
            }
        }
        return result;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Board oth)) return false;
        if (this.dimension != oth.dimension) return false;
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                if (this.tiles[row][col] != oth.tiles[row][col]) return false;
            }
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        List<Board> neighbors = new ArrayList<>(4);
        if (zRow != 0) {
            neighbors.add(this.copy().move(zRow - 1, zCol));
        }
        if (zRow != dimension - 1) {
            neighbors.add(this.copy().move(zRow + 1, zCol));
        }
        if (zCol != 0) {
            neighbors.add(this.copy().move(zRow, zCol - 1));
        }
        if (zCol != dimension - 1) {
            neighbors.add(this.copy().move(zRow, zCol + 1));
        }
        return neighbors;
    }

    public Board twin() {
        Board board = this.copy();
        if (zRow != 0) {
            int temp = board.tiles[0][0];
            board.tiles[0][0] = board.tiles[0][1];
            board.tiles[0][1] = temp;
        } else if (zCol != 0) {
            int temp = board.tiles[0][0];
            board.tiles[0][0] = board.tiles[1][0];
            board.tiles[1][0] = temp;
        } else {
            int temp = board.tiles[1][1];
            board.tiles[1][1] = board.tiles[0][1];
            board.tiles[0][1] = temp;
        }
        return board;
    }

    private Board copy() {
        int[][] tiles = new int[dimension][dimension];
        for (int row = 0; row < dimension; row++) {
            for (int col = 0; col < dimension; col++) {
                tiles[row][col] = this.tiles[row][col];
            }
        }
        return new Board(tiles);
    }

    private Board move(int row, int col) {
        if (Math.abs(row - zRow) + Math.abs(col - zCol) != 1) throw new IllegalArgumentException();
        if (row < 0 || row >= dimension) throw new IllegalArgumentException();
        if (col < 0 || col >= dimension) throw new IllegalArgumentException();
        tiles[zRow][zCol] = tiles[row][col];
        tiles[row][col] = 0;
        zRow = row;
        zCol = col;
        return this;
    }

    public static void main(String[] args) {
        int[][] tiles = new int[3][3];
        int index = 0;
        int[] values = new int[] {8, 1, 3, 4, 0, 2, 7, 6, 5};
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                tiles[row][col] = values[index++];
            }
        }
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.println("Is goal: " + board.isGoal());
        System.out.println("Is same: " + board.equals(board.copy()));
        System.out.println("Manhattan: " + board.manhattan());
        System.out.println("Hamming: " + board.hamming());
        for (Board b : board.neighbors()) {
            System.out.println(b);
            System.out.println("Is goal: " + b.isGoal());
            System.out.println("Is same: " + b.equals(board));
        }
    }

}
