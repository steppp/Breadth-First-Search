package bfs;

import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import model.queue.Queue;
import java.util.Arrays;

public class BfsVisit {

	public static void bfsVisit(Graph<CoordinateNode> g, Node<CoordinateNode> root) {
		
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
			
			// TODO: ESAMINARE IL NODO U
			
			for (Node<CoordinateNode> v : g.adj(u)) {
				
				// TODO: ESAMINARE L'ARCO U-V
				
				int vPos = Arrays.asList(visited).indexOf(v);
				if (!visited[vPos]) {
					visited[vPos] = true;
					s.enque(v);
				}
			}			
		}
	}
}
