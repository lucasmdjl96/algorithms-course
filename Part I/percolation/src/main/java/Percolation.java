import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private final int n;
    private final WeightedQuickUnionUF weightedQuickUnionUF;

    private final boolean[] open;

    private int nOpen = 0;

    private int openFirst = 0;

    private int openLast = 0;

    public Percolation(int n) {
        if (n <= 0) throw new IllegalArgumentException("n should be greater than 0.");
        this.n = n;
        weightedQuickUnionUF = new WeightedQuickUnionUF(n * n);
        open = new boolean[n * n];
        for (int i = 1; i <= n; i++) {
            weightedQuickUnionUF.union(pos(1, 1), pos(1, i));
            weightedQuickUnionUF.union(pos(n, n), pos(n, i));
        }
    }

    private int pos(int row, int col) {
        return (row - 1) * n + col - 1;
    }

    private void validate(int row, int col) {
        if (row < 1 || row > this.n) throw new IllegalArgumentException("row should be between 1 and n");
        if (col < 1 || col > this.n) throw new IllegalArgumentException("column should be between 1 and n");
    }

    public void open(int row, int col) {
        validate(row, col);
        if (isOpen(row, col)) return;
        nOpen++;
        open[pos(row, col)] = true;
        if (row == 1) openFirst = col;
        if (row == n) openLast = col;
        if (row > 1 && isOpen(row - 1, col)) weightedQuickUnionUF.union(pos(row, col), pos(row - 1, col));
        if (col > 1 && isOpen(row, col - 1)) weightedQuickUnionUF.union(pos(row, col), pos(row, col - 1));
        if (row < n && isOpen(row + 1, col)) weightedQuickUnionUF.union(pos(row, col), pos(row + 1, col));
        if (col < n && isOpen(row, col + 1)) weightedQuickUnionUF.union(pos(row, col), pos(row, col + 1));
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return open[pos(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        if (openFirst == 0) return false;
        if (!isOpen(row, col)) return false;
        return weightedQuickUnionUF.find(pos(1, 1)) == weightedQuickUnionUF.find(pos(row, col));
    }

    public int numberOfOpenSites() {
        return nOpen;
    }

    public boolean percolates() {
        if (openLast == 0) return false;
        return isFull(n, openLast);
    }

    public static void main(String[] args) {
        // Do nothing
    }

}
