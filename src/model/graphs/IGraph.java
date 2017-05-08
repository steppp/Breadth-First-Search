package model.graphs;

import java.util.Set;

public interface IGraph<T extends Comparable<T>> {

    public boolean insertNode(Node<T> u) ;

    public void deleteNode(Node<T> u) ;

    public boolean insertEdge(Node<T> u, Node<T> v) ;

    public void deleteEdge(Node<T> u, Node<T> v) ;

    public Set<Node<T>> adj(Node<T> u) ;

    public Set<Node<T>> V() ;


    public void print() ;
}
