package bfs;

import model.graphs.Node;
import model.node.visual.CoordinateNode;

/**
 * Interfaccia che permetterà di definire dei Gestori di Eventi che saranno richiamati
 * nell'esecuzione dell'algoritmo sulla ricerca BFS.
 * @author stefanoandriolo
 *
 */
@FunctionalInterface
interface BfsActionListener {
	
	@SuppressWarnings("unchecked")
	void actionPerformed(ActionType a, Node<CoordinateNode>...involvedNodes);
}
