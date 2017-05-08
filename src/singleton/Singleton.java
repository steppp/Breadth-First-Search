package singleton;

import model.graphs.*;
import model.node.visual.CoordinateNode;

public class Singleton {

	private static Singleton sharedInstance = null;
	
	protected Singleton() {
		// TODO: - Verificare la correttezza dopo l'introduzione di nuove funzionalità
		this.graphLoaded = this.isAnimating = false;
		
		this.currentGraph = null;
	}
	
	/**
	 * Ritorna l'istanza del Singleton e, se non è già presente, la crea
	 * @return istanza del Singleton.
	 */
	public static Singleton getInstance() {
		if (sharedInstance == null)
			sharedInstance = new Singleton();
		
		return sharedInstance;
	}
	
	// --------- OTHER DATA ------------
	public final double NODE_RADIUS = 20.0;
	
	
	public boolean graphLoaded;
	public boolean isAnimating;
	
	// TODO: Trovare un modo per creare una variabile che contenga il grafo caricato, il problema è il tipo dei nodi del grafo
	// che non può essere stabilito a priori (creare un'interfaccia GraphInsertable?)
	Graph<CoordinateNode> currentGraph;
	
	public Graph<CoordinateNode> getCurrentGraph() {
		return currentGraph;
	}
	
	public void setCurrentGraph(Graph<CoordinateNode> newGraph) {
		currentGraph = newGraph;
	}
}
