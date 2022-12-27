import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            String next = StdIn.readString();
            if (randomizedQueue.size() == k - 1) {
                int random = StdRandom.uniformInt(k);
                if (random != 0)  {
                    randomizedQueue.dequeue();
                    randomizedQueue.enqueue(next);
                }
            } else {
                randomizedQueue.enqueue(next);
            }
        }
        for (int i = 0; i < k; i++) System.out.println(randomizedQueue.dequeue());
    }
}
