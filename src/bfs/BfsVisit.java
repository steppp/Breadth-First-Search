package bfs;

import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import model.queue.Queue;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class BfsVisit {

	public static void bfsVisit(Graph<Node<CoordinateNode>> g, Node<CoordinateNode> root) {
		
		int size = g.V().size();
		
		// creazione della coda ed inserimento dell'elemento radice
		Queue<Node<CoordinateNode>> s = new Queue<>(size);
		s.enque(root);
		
		// inizializzazione del vettore che indica se un certo nodo è già stato visitato
		Boolean[] visited = new Boolean[size];
		for (int i = 0; i < size; i++) {
			visited[i] = false;
		}
		
		// nodo radice visitato
		visited[Arrays.asList(visited).indexOf(root)] = true;
		
		while (!s.isEmpty()) {
			Node<CoordinateNode> u = s.dequeue();
			
			// ESAMINO IL NODO U
			
			TreeSet<Node<CoordinateNode>> ts = g.adj(u);
			
		}
	}
}
