package aoc.utils;

public class CircularBuffer {

    private Node first;
    private Node current;

    public CircularBuffer(int data) {
        first = new Node(data, null, null);
        first.left = first;
        first.right = first;
        current = first;
    }

    public void add(int data, int pos) {

        if (pos >= 0)
            for (int i = 0; i < pos; i++)
                current = current.right;
        else
            for (int i = 0; i >= pos; i--)
                current = current.left;

        current = new Node(data, current, current.right);
        current.left.right = current;
        current.right.left = current;
    }

    public int remove(int pos) {

        if (pos >= 0)
            for (int i = 0; i < pos; i++)
                current = current.right;
        else
            for (int i = -1; i > pos; i--)
                current = current.left;

        var rem = current.left;
        rem.right.left = rem.left;
        rem.left.right = rem.right;
        return rem.data;
    }

    public void print() {
        var n = first;
        do {
            if (n == current) System.out.print("(" + n.data + ")");
            else System.out.print(" " + n.data + " ");
            n = n.right;
        } while (n != first);
        System.out.println();
    }

    private class Node {
        int data;
        Node left;
        Node right;

        public Node(int data, Node left, Node right) {
            this.data = data;
            this.left = left;
            this.right = right;
        }
    }
}

