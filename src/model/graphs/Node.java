package model.graphs;

public class Node<T extends Comparable<T>> implements Comparable<Node<T>> {

    private T element;


    public Node(T el) {
        this.element = el;
    }


    public T getElement() {
        return element;
    }


    @Override
    public String toString() {
        return this.element.toString();
    }

    @Override
    public int compareTo(Node<T> o) {
        return this.element.compareTo(o.element);
    }
}

