package bfs;

import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import model.queue.Queue;
import java.util.Arrays;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

public class BfsVisit {

	public static void bfsVisit(Graph<CoordinateNode> g, Node<CoordinateNode> root) {
		
		int size = g.V().size();
		
		// creazione della coda ed inserimento dell'elemento radice
		Queue<Node<CoordinateNode>> s = new Queue<>(size);
		s.enque(root);
		
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
			
			Node<CoordinateNode> u = s.dequeue();
			
			// -------- MOSTRARE IL VETTORE VISITED ----------
			
			// TODO: ESAMINARE IL NODO U
			
			for (Node<CoordinateNode> v : g.adj(u)) {
				
				// TODO: ESAMINARE L'ARCO U-V
				
				int vPos = Arrays.asList(visited).indexOf(v);
				if (!visited.get(vPos)) {
					visited.set(vPos, true);
					s.enque(v);
					
					// --------- NODO INSERITO -----------
				} // --------- NODO GIA' VISTO -----------
			}			
		}
	}
}
