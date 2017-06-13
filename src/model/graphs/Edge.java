package model.graphs;

/**
 * Classe che servirà solamente per passare il vertice come unico parametro di una funzione
 * @author stefanoandriolo
 *
 */
public class Edge<T extends Comparable<T>> {

	Node<T> source;
	Node<T> target;
	
	public Edge(Node<T> source, Node<T> target) {
		this.source = source;
		this.target = target;
	}

	/**
	 * @return nodo sorgente.
	 */
	public Node<T> getSource() {
		return source;
	}

	/**
	 * @param source il nodo da impostare come sorgente.
	 */
	public void setSource(Node<T> source) {
		this.source = source;
	}

	/**
	 * @return il nodo di destinazione.
	 */
	public Node<T> getTarget() {
		return target;
	}

	/**
	 * @param target il nodo da impostare come destinazione.
	 */
	public void setTarget(Node<T> target) {
		this.target = target;
	}
	
	
}
