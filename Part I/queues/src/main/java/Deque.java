import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException();
        Node<Item> node = new Node<>(item);
        if (isEmpty()) {
            first = node;
            last = node;
        } else {
            node.next = first;
            first.previous = node;
            first = node;
        }
        size++;
    }

    public void addLast(Item item) {
        if (isEmpty()) {
            addFirst(item);
            return;
        }
        if (item == null) throw new IllegalArgumentException();
        Node<Item> node = new Node<>(item);
        last.next = node;
        node.previous = last;
        last = node;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = first.data;
        first = first.next;
        if (first != null) first.previous = null;
        else last = null;
        size--;
        return item;
    }

    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = last.data;
        last = last.previous;
        if (last != null) last.next = null;
        else first = null;
        size--;
        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new Iterator<Item>() {
            private Node<Item> node = first;
            @Override
            public boolean hasNext() {
                return node != null;
            }

            @Override
            public Item next() {
                if (!hasNext()) throw new NoSuchElementException();
                Item result = node.data;
                node = node.next;
                return result;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        for (String str : deque) System.out.print(str + ", ");
        System.out.println();
        deque.addFirst("A");
        for (String str : deque) System.out.print(str + ", ");
        System.out.println();
        deque.addLast("B");
        for (String str : deque) System.out.print(str + ", ");
        System.out.println();
        deque.addLast("C");
        for (String str : deque) System.out.print(str + ", ");
        System.out.println();
        deque.addFirst("D");
        for (String str : deque) System.out.print(str + ", ");
        System.out.println();
        String last = deque.removeLast();
        for (String str : deque) System.out.print(str + ", ");
        System.out.println("-> " + last);
        String first = deque.removeFirst();
        for (String str : deque) System.out.print(str + ", ");
        System.out.println("-> " + first);
        System.out.println(deque.isEmpty());
        deque.removeFirst();
        deque.removeFirst();
        deque.addLast("E");
        for (String str : deque) System.out.print(str + ", ");
        System.out.println();
        deque.removeLast();
        System.out.println(deque.isEmpty());
        System.out.println(deque.size());
    }

    private static class Node<Item> {
        Item data;
        Node<Item> next;
        Node<Item> previous;
        Node(Item item) {
            data = item;
        }
    }
}
