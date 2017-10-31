public class LinkedList<Item>{
    private int n;        // number of elements on list
    private Node first;     // sentinel before first item

    public LinkedList() {
        first  = new Node();
    }

    // linked list node helper data type
    private class Node {
        private Item item;
        private Node next;
    }

    public boolean isEmpty()    { return n == 0; }
    public int size()           { return n;      }

    // add the item to the start of the list
    public void add(Item item) {
        Node x = new Node();
        x.item = item;
        x.next = first;
        first = x;
        n++;
    }

    public Item[] toArray() {
        Item[] a = (Item[]) new Object[n];
        Node current = first;
        int i = 0;

        while(current != null) {
            a[i] = current.item;
            i++;
            current = current.next;
        }

        return a;
    }
}
