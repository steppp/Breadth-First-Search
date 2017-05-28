package bfs;

import model.graphs.Edge;
import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import model.queue.Queue;
import java.util.function.Function;

/**
 * La classe GraphVisiter descrive degli oggetti che effettuano una visita (BFS) su un certo grafo
 * a partire da un certo nodo. Questa classe estende la classe Thread in modo che la propria esecuzione
 * possa essere controllata mettendola in pausa e facendola ripartire, in modo da offrire due modalità:
 * quella automatica, in cui non ci sono interruzioni, e si arriva direttamente al risultato, e quella
 * step-by-step, in cui è possibile esaminare ciò che succede durante l'avanzamento dell'algoritmo.
 * @author stefanoandriolo
 *
 */
public class GraphVisiter extends Thread {
	
	// la classe sfrutta i Thread per gestire la sopsensione e la ripresa dell'esecuzione dell'algoritmo
	// VEDI QUI -> http://www.codejava.net/java-core/concurrency/how-to-use-threads-in-java-create-start-pause-interrupt-and-join
	// probabilmente, quando il thread viene sospeso con this.interrupt(), l'esecuzione torna al thread principale
	// da lì sarà poi possibile riprendere l'esecuzione del thread BFS_VISIT
	
	Graph<CoordinateNode> g;
	Node<CoordinateNode> root;
	
	// proprietà che descrivono le funzioni da applicare durante il progresso dell'algoritmo
	Function<Boolean[], Void> showVisited = null;
	Function<Node<CoordinateNode>, Void> onANode = null;
	Function<Edge<CoordinateNode>, Void> examiningEdge = null;
	Function<Edge<CoordinateNode>, Void> nodeInserted = null;
	Function<Edge<CoordinateNode>, Void> nodeNotInserted = null;
	
	
	public GraphVisiter(Graph<CoordinateNode> g, Node<CoordinateNode> root) {
		this.g = g;
		this.root = root;
	}
	

	/**
	 * @param showVisited the showVisited to set
	 */
	public void setShowVisited(Function<Boolean[], Void> showVisited) {
		this.showVisited = showVisited;
	}


	/**
	 * @param onANode the onANode to set
	 */
	public void setOnANode(Function<Node<CoordinateNode>, Void> onANode) {
		this.onANode = onANode;
	}


	/**
	 * @param examiningEdge the examiningEdge to set
	 */
	public void setExaminingEdge(Function<Edge<CoordinateNode>, Void> examiningEdge) {
		this.examiningEdge = examiningEdge;
	}


	/**
	 * @param nodeInserted the nodeInserted to set
	 */
	public void setNodeInserted(Function<Edge<CoordinateNode>, Void> nodeInserted) {
		this.nodeInserted = nodeInserted;
	}


	/**
	 * @param nodeNotInserted the nodeNotInserted to set
	 */
	public void setNodeNotInserted(Function<Edge<CoordinateNode>, Void> nodeNotInserted) {
		this.nodeNotInserted = nodeNotInserted;
	}


	/**
	 * Effettua una visita in ampiezza sul grafo indicato a partire dal nodo passato come parametro
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

		// TODO: aggiornare la lista dei padri
		//LinkedList<Node<CoordinateNode>> parentList = new LinkedList<>();
		
		// nodo radice visitato
		visited[root.getElement().getIndex()] = true;
		
		while (!s.isEmpty()) {
			
			Node<CoordinateNode> u = s.dequeue();
			
			// TODO: mostrare anche l'array dei padri
			// -------- MOSTRARE IL VETTORE VISITED ----------
			if (showVisited != null) {
				showVisited.apply(visited);
			}
			
			// TODO: verificare se this.interrupt() è il metodo migliore da utilizzare
			
			// -------- ESAMINARE IL NODO U --------
			if (onANode != null) {
				onANode.apply(u);
				
				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
			
			for (Node<CoordinateNode> v : g.adj(u)) {
				
				// -------- ESAMINARE L'ARCO U-V --------
				Edge<CoordinateNode> currentEdge = new Edge<CoordinateNode>(u, v);
				if (examiningEdge != null) {
					examiningEdge.apply(currentEdge);
					
					synchronized (this) {
						try {
							this.wait();
						} catch (InterruptedException e) {
							Thread.currentThread().interrupt();
						}
					}
				}
				
				int vPos = v.getElement().getIndex();
				if (!visited[vPos]) {
					visited[vPos] = true;
					s.enque(v);
					
					// --------- NODO INSERITO -----------
					if (nodeInserted != null) {
						nodeInserted.apply(currentEdge);
					}
				} else {
					// --------- NODO GIA' VISTO -----------
					if (nodeNotInserted != null) {
						nodeNotInserted.apply(currentEdge);
					}
				}

				synchronized (this) {
					try {
						this.wait();
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}			
		}
	}
	
	
	public void run() {
		
		// inizio solamente se il grafo ed il nodo di partenza non sono nulli
		if (this.g != null && this.root != null) {
			this.bfsVisit();
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
