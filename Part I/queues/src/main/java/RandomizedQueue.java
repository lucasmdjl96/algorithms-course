import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int size;
    private boolean reduce;

    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        reduce = true;
    }

    private RandomizedQueue(RandomizedQueue<Item> randomizedQueue) {
        size = randomizedQueue.size;
        items = copyOf(randomizedQueue.items, size, size);
        reduce = false;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException();
        if (size == items.length) items = copyOf(items, items.length, 2 * items.length);
        items[size] = item;
        size++;
    }

    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(size);
        Item item = items[index];
        items[index] = items[size - 1];
        items[size - 1] = null;
        size--;
        if (reduce && size == items.length / 4 && items.length > 1) items = copyOf(items, items.length, items.length / 2);
        return item;
    }

    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException();
        int index = StdRandom.uniformInt(size);
        return items[index];
    }

    @Override
    public Iterator<Item> iterator() {
        RandomizedQueue<Item> randomizedQueue = new RandomizedQueue<>(this);
        return new Iterator<Item>() {
            @Override
            public boolean hasNext() {
                return !randomizedQueue.isEmpty();
            }
            @Override
            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                return randomizedQueue.dequeue();
            }
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue("A");
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println();
        randomizedQueue.enqueue("B");
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println();
        randomizedQueue.enqueue("C");
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println();
        randomizedQueue.enqueue("D");
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println();
        for (int i = 0; i < 10; i++) System.out.println(randomizedQueue.sample());
        System.out.println(randomizedQueue.size());
        System.out.println(randomizedQueue.isEmpty());
        String last = randomizedQueue.dequeue();
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println("-> " + last);
        last = randomizedQueue.dequeue();
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println("-> " + last);
        last = randomizedQueue.dequeue();
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println("-> " + last);
        randomizedQueue.dequeue();
        for (String str : randomizedQueue) System.out.print(str + ", ");
        System.out.println(randomizedQueue.isEmpty());
    }

    private static <Item> Item[] copyOf(Item[] oldItems, int oldSize, int newSize) {
        Item[] newItems = (Item[]) new Object[newSize];
        int i = 0;
        while (i < oldSize && i < newSize) {
            newItems[i] = oldItems[i];
            i++;
        }
        return newItems;
    }

}
