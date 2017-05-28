package singleton;

import model.graphs.*;
import model.node.visual.CoordinateNode;
import utility.AnimationSettings;

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
	
	Graph<CoordinateNode> currentGraph;
	
	public Graph<CoordinateNode> getCurrentGraph() {
		return currentGraph;
	}
	
	public void setCurrentGraph(Graph<CoordinateNode> newGraph) {
		currentGraph = newGraph;
	}
	
	
	public AnimationSettings animPrefs;
	
	
	/**
	 * Effettua una ricerca tra tutti i Thread attivi e ritorna quello il cui nome corrisponde
	 * alla stringa passata, o se non ne esiste nessuno che soddisfi questa condizione, null.
	 * @param name nome del Thread desiderato.
	 * @return Thread con il nome specificato se presente, altrimenti null. 
	 */
	public Thread getThreadByName(String name) {
		for (Thread t : Thread.getAllStackTraces().keySet()) {
			if (t.getName().equals(name))
				return t;
		}
		
		return null;
	}
}
