package model.graphs;

import java.util.*;
import java.util.Map.Entry;

/**
 * Created by stefanoandriolo on 19/04/17.
 */
public class Graph<T extends Comparable<T>> implements IGraph<T> {

    HashMap<Node<T>, LinkedList<Node<T>>> vertexes;


    public Graph() {
        this.vertexes = new HashMap<>();
    }

    @Override
    public void insertNode(Node<T> u) {

        if (!this.vertexes.containsKey(u)) {
            this.vertexes.put(u, new LinkedList<>());
        }

    }

    @Override
    public void deleteNode(Node<T> u) {
        // TODO: completare, cancellare il nodo e tutti gli archi adiacenti e incidenti
    }

    @Override
    // NOTA: Assumiamo che u e v esistano nel grafo
    public void insertEdge(Node<T> u, Node<T> v) {
        if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
            this.vertexes.get(u).add(v);
    }

    @Override
    public void deleteEdge(Node<T> u, Node<T> v) {
        if (this.vertexes.containsKey(u) && this.vertexes.containsKey(v))
            this.vertexes.get(u).remove(v);
    }

    @Override
    public Set<Node<T>> adj(Node<T> u) {

        HashSet<Node<T>> adjSet = new HashSet<Node<T>>();

        if (this.vertexes.containsKey(u))
            adjSet.addAll(this.vertexes.get(u));

        return adjSet;
    }

    @Override
    public Set<Node<T>> V() {
        return null;
    }

    @Override
    public void print() {
        for (Entry<Node<T>, LinkedList<Node<T>>> e :
                this.vertexes.entrySet()) {
            System.out.print(e.getKey() + " : ");

            for (Node<T> l :
                    e.getValue()) {
                System.out.print(l + " ");
            }

            System.out.println();
        }
    }
    
    
    // ritorna una copia del vettore dei vertici, accessibile solamente alle classi derivate
    // serve per riuscire a costruire il disegno del grafico
    protected HashMap<Node<T>, LinkedList<Node<T>>> getVertexes() {
    	return new HashMap<Node<T>, LinkedList<Node<T>>>(this.vertexes);
    }
}
