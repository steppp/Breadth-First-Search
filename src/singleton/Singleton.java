package singleton;

import java.util.AbstractMap.SimpleEntry;
import java.util.Set;
import java.util.Timer;

import application.MainController;
import model.graphs.*;
import model.node.visual.CoordinateNode;
import utility.AnimationSettings;
import utility.GraphDrawer;
import utility.Logger;

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
	
	/**
	 * Raggio dei nodi che vengono disegnati nel pannello del grafo.
	 */
	public static final double NODE_RADIUS = 20.0;
	
	public boolean graphLoaded;
	public boolean isAnimating;
	
	/**
	 * Grafo corrente visualizzato sul relativo pannello.
	 */
	Graph<CoordinateNode> currentGraph;
	
	
	/**
	 * Ritorna il grafo corrente.
	 * @return grafo corrente.
	 */
	public Graph<CoordinateNode> getCurrentGraph() {
		return currentGraph;
	}
	
	
	/**
	 * Imposta il grafo corrente.
	 * @param newGraph grafo corrente.
	 */
	public void setCurrentGraph(Graph<CoordinateNode> newGraph) {
		currentGraph = newGraph;
	}
	
	
	/**
	 * Oggetto che contiene le preferenze relative all'animazione dell'algoritmo.
	 */
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
	
	/**
	 * Wrapper che si occupa dell'output sull'area di testo
	 */
	public Logger logger;
	
	/**
	 * Timer per il delay dei passaggi dell'algoritmo
	 */
	public Timer timer = null;
	
	/**
	 * Riferimento al controller principale per accedervi anche dai metodi statici.
	 */
	public MainController mainViewController = null;
	
	/**
	 * Variabile che durante l'esecuzione dell'algoritmo contiene il nodo che si sta esaminando e
	 * la rispettiva lista di adiacenza, che viene via via ridotta mentre vengono esaminati gli
	 * archi verso tali nodi. Usata per una corretta visualizzazione dell'algoritmo.
	 */
	public SimpleEntry<Node<CoordinateNode>, Set<Node<CoordinateNode>>> currentNodeWithList = null;
	
	/**
	 * Oggetto che si occupa di disegnare un grafo o solamente i nodi o gli archi su un pannello specificato.
	 * Questo oggetto viene istanziato all'apertura della finestra principale, quindi quando servirà disegnare qualcosa,
	 * basterà richiamare l'apposito metodo su questo oggetto.
	 */
	public GraphDrawer drawingUtility = null;
	

	/**
	 * Indirizzo della pagina web che porta alla repo di github in cui è presente il codice del progetto.
	 */
	public static final String ABOUT_WEB_PAGE = "https://github.com/steppp/Breadth-First-Search/";
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	

	
	
	
	
	
	
	
	
	
	
	
}
