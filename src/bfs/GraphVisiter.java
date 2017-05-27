package bfs;

import model.graphs.Edge;
import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import model.queue.Queue;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.Function;


public class GraphVisiter {
	

	// ======================================================================================
	// ======================================================================================
	// =																					=
	// =   SI POTREBBERO UTILIZZARE I THREAD PER CONTROLLARE L'AVANZAMENTO DELL'ALGORITMO   =
	// =																					=
	// ======================================================================================
	// ======================================================================================
	
	
	
	Graph<CoordinateNode> g;
	Node<CoordinateNode> root;
	
	// proprietà che descrivono le funzioni da applicare durante il progresso dell'algoritmo
	Function<Boolean[], Void> showVisited;
	Function<Node<CoordinateNode>, Void> onANode;
	Function<Edge<CoordinateNode>, Void> examiningEdge;
	Function<Edge<CoordinateNode>, Void> nodeInserted;
	Function<Edge<CoordinateNode>, Void> nodeNotInserted;
	
	// lista dei padri
	LinkedList<Node<CoordinateNode>> parentList = new LinkedList<>();
	
	
	public GraphVisiter(Graph<CoordinateNode> g, Node<CoordinateNode> root) {
		this.g = g;
		this.root = root;
	}
	

	/**
	 * Effettua una visita in ampiezza sul grafo indicato a partire dal nodo passato come parametro
	 * @param g grafo da visitare.
	 * @param root nodo del grafo da cui partire.
	 */
	public void bfsVisit() {
		
		int size = g.V().size();
		
		// creazione della coda ed inserimento dell'elemento radice
		Queue<Node<CoordinateNode>> s = new Queue<>(size);
		s.enque(root);
		
		// inizializzazione di un vettore che indica se un certo nodo è già stato visitato
		Boolean[] visited = new Boolean[size];
		for (int i = 0; i < size; i++) {
			visited[i] = false;
		}
		
		
		// nodo radice visitato
		visited[root.getElement().getIndex()] = true;
		
		while (!s.isEmpty()) {
			
			Node<CoordinateNode> u = s.dequeue();
			
			// TODO: forse mostrare anche l'array dei padri
			// -------- MOSTRARE IL VETTORE VISITED ----------
			showVisited.apply(visited);
			
			// -------- ESAMINARE IL NODO U --------
			onANode.apply(u);
			
			for (Node<CoordinateNode> v : g.adj(u)) {
				
				// -------- ESAMINARE L'ARCO U-V --------
				Edge<CoordinateNode> currentEdge = new Edge<CoordinateNode>(u, v);
				examiningEdge.apply(currentEdge);
				
				int vPos = Arrays.asList(visited).indexOf(v);
				if (!visited[vPos]) {
					visited[vPos] = true;
					s.enque(v);
					
					// --------- NODO INSERITO -----------
					nodeInserted.apply(currentEdge);
				} else {
					// --------- NODO GIA' VISTO -----------
					nodeNotInserted.apply(currentEdge);
				}
			}			
		}
	}
}
