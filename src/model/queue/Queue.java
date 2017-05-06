package model.queue;

import java.util.Arrays;

public class Queue<T> {

    private T[] values;
    private Integer length, head, maxLength;

    @SuppressWarnings("unchecked")
	public Queue(Integer maxLength) {
        this.maxLength = maxLength;
        this.head = 0;
        this.length = 0;

        // creo un vettore di Object di dimensione maxLength e con un cast esplicito lo converto nel tipo T
        this.values = (T[]) new Object[maxLength];
    }

    
    public void enque(T item) {
        this.values[(head + length) % maxLength] = item;
        this.length++;
    }


    public T dequeue() {

        T tmp = this.values[this.head];

        this.head = (head + 1) % maxLength;
        this.length--;

        return tmp;
    }


    public String toString() {
        return Arrays.toString(this.values);
    }
}
