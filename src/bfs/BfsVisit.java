package bfs;

import model.graphs.Edge;
import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import model.queue.Queue;
import java.util.Arrays;
import java.util.function.Function;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

// TODO: rinominare in BfsVisiter
public class BfsVisit {
	
	// proprietà che descrivono le funzioni da applicare durante il progresso dell'algoritmo
	Function<ObservableList<Boolean>, Void> showVisited;
	Function<Node<CoordinateNode>, Void> examiningNode;
	Function<Edge<CoordinateNode>, Void> examiningEdge;
	Function<Node<CoordinateNode>, Void> insertedNode;
	Function<Node<CoordinateNode>, Void> notInsertedNode;

	// TODO: estrarre g e root dal metodo ed impostarle come proprietà della classe
	/**
	 * Effettua una visita in ampiezza sul grafo indicato a partire dal nodo passato come parametro
	 * @param g grafo da visitare.
	 * @param root nodo del grafo da cui partire.
	 */
	public void bfsVisit(Graph<CoordinateNode> g, Node<CoordinateNode> root) {
		
		int size = g.V().size();
		
		// creazione della coda ed inserimento dell'elemento radice
		Queue<Node<CoordinateNode>> s = new Queue<>(size);
		s.enque(root);
		
		// TODO: valutare se sia necessario l'utilizzo di un'ObservableList
		// inizializzazione di una ObservableList che indica se un certo nodo è già stato visitato
		ObservableList<Boolean> visited = FXCollections.observableArrayList();
		for (int i = 0; i < size; i++) {
			visited.set(i, false);
		}

		visited.addListener(new ListChangeListener<Boolean>() {

			@Override
			public void onChanged(javafx.collections.ListChangeListener.Change<? extends Boolean> c) {
				
			}
			
		});
		
		
		// nodo radice visitato
		visited.set(visited.indexOf(root), true);
		
		while (!s.isEmpty()) {
			
			// -------- MOSTRARE IL VETTORE VISITED ----------
			showVisited.apply(visited);
			
			Node<CoordinateNode> u = s.dequeue();
			
			// -------- MOSTRARE IL VETTORE VISITED ----------
			showVisited.apply(visited);
			
			// -------- ESAMINARE IL NODO U --------
			examiningNode.apply(u);
			
			for (Node<CoordinateNode> v : g.adj(u)) {
				
				// -------- ESAMINARE L'ARCO U-V --------
				examiningEdge.apply(new Edge<CoordinateNode>(u, v));
				
				int vPos = Arrays.asList(visited).indexOf(v);
				if (!visited.get(vPos)) {
					visited.set(vPos, true);
					s.enque(v);
					
					// --------- NODO INSERITO -----------
					// TODO: forse occorre passare tutto il vertice
					insertedNode.apply(v);
				} else {
					// --------- NODO GIA' VISTO -----------
					// TODO: forse occorre passare tutto il vertice
					notInsertedNode.apply(v);
				}
			}			
		}
	}
}
