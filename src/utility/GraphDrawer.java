package utility;

import application.MainController;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import model.arrow.Arrow;
import model.graphs.Edge;
import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import singleton.Singleton;

/**
 * Classe che permette di disegnare il grafo con i propri nodi ed archi nel pannello apposito
 * @author stefanoandriolo
 *
 */
public class GraphDrawer {

	Pane currentPane;
	
	/**
	 * Crea un'istanza di GraphDrawer utilizzando il parametro passato come area su cui disegnare il grafo
	 * @param pane pannello su cui disegnare il grafo.
	 */
	public GraphDrawer(Pane pane) {
		this.currentPane = pane;
	}
	
	/**
	 * Crea un'istanza di GraphDrawer utilizzando il parametro passato come area su cui disegnare il grafo,
	 * e disegna immediatamente il grafo passato
	 * @param pane pannello su cui diesgnare il grafo.
	 * @param graph grafo da disegnare immediatamente.
	 */
	public GraphDrawer(Pane pane, Graph<CoordinateNode> graph) {
		this(pane);
		this.drawGraph(graph);
	}

	/**
	 * @return the currentPane
	 */
	public Pane getCurrentPane() {
		return currentPane;
	}

	/**
	 * @param currentPane the currentPane to set
	 */
	public void setCurrentPane(Pane currentPane) {
		this.currentPane = currentPane;
	}
	
	
	/**
	 * Disegna il grafo passato come parametro sul pannello attuale.
	 * @param graph grafo da disegnare.
	 * @return true se il grafo è stato disegnato, false altrimenti.
	 */
	public boolean drawGraph(Graph<CoordinateNode> graph) {
		if (graph == null) {
			Singleton.getInstance().logger.log("Impossibile creare il grafo.");
			return false;
		}
		
		// disegno tutti i nodi
		for (Node<CoordinateNode> node : graph.V()) {
			this.drawNode(node);
		}
		
		// disegno tutti gli archi
		for (Node<CoordinateNode> node : graph.V()) {
			for (Node<CoordinateNode> otherNode : graph.adj(node)) {
				this.drawEdge(new Edge<CoordinateNode>(node, otherNode));
			}
		}
		
		return true;
	}
	
	
	/**
	 * Disegna un nodo sul pannello utilizzando i dati passati.
	 * @param index indice da visualizzare sul nodo disegnato.
	 * @param xPos posizione in cui disegnare il nodo sull'asse x.
	 * @param yPos posizione in cui disegnare il nodo sull'asse y.
	 */
	public void drawNode(Integer index, double xPos, double yPos) {
		CoordinateNode coordN = new CoordinateNode(index, xPos, yPos);
		this.drawNode(new Node<CoordinateNode>(coordN));
	}
	
	
	/**
	 * Disegna sul pannello corrente il nodo passato come parametro. 
	 * @param node nodo da disegnare sul pannello.
	 */
	public void drawNode(Node<CoordinateNode> node) {
		double radius = Singleton.NODE_RADIUS;
		
		Circle c = this.createCircle();
		Text t = this.createText(node.getElement().toString());
		
		StackPane sp = new StackPane();
		sp.setLayoutX(node.getElement().getxPos() - radius);
		sp.setLayoutY(node.getElement().getyPos() - radius);
		
		sp.getChildren().addAll(c, t);
		
		sp.setOnMouseClicked((MouseEvent e) -> {
			
			// primo click su un nodo già esistente -> faccio partire l'azione per collegare due nodi
			if (MainController.currentNode == null) {
				
				MainController.currentNode = new CoordinateNode(node.getElement().getIndex(),
						node.getElement().getxPos(), node.getElement().getyPos());
				
				// comunico all'utente che il nodo sorgente dell'arco è stato impostato
				Singleton.getInstance().logger.log("Nodo sorgente selezionato: " + node.getElement().toString() +
						", in attesa del nodo di destinazione..");
			} else {
				// secondo click su un altro nodo -> creo il collegamento tra i due se non si tratta dello stesso nodo
				
				// stesso nodo, non faccio niente
				if (node.getElement().getIndex() == MainController.currentNode.getIndex()) return;
				
	    		// creazione del vertice tra i due nodi
	    		Graph<CoordinateNode> graph = Singleton.getInstance().getCurrentGraph();
	    		Node<CoordinateNode> source = graph.getNodeWithValue(MainController.currentNode);
	    		Node<CoordinateNode> target = graph.getNodeWithValue(new CoordinateNode(node.getElement().getIndex(),
	    				node.getElement().getxPos(), node.getElement().getyPos()));
	    		
	    		// inserisco l'arco nel grafo
	    		Edge<CoordinateNode> edge = new Edge<CoordinateNode>(source, target);
	    		
	    		// se non è stato possibile creare il vertice non faccio nulla
	    		if (!Singleton.getInstance().getCurrentGraph().insertEdge(edge.getSource(), edge.getTarget())) return;
	    		
	    		// disegno l'arco
	    		this.drawEdge(edge);
			}
		});
		
		sp.setOnMouseEntered((MouseEvent e) -> {
			MainController.mouseOverNode = true;
		});
		
		sp.setOnMouseExited((MouseEvent e) -> {
			MainController.mouseOverNode = false;
		});
		
		this.currentPane.getChildren().add(sp);
	}
	
	
	/**
	 * Disegna sul pannello corrente l'arco passato come parametro.
	 * @param edge arco da disegnare sul pannello corrente.
	 */
	public void drawEdge(Edge<CoordinateNode> edge) {
		
		Arrow arrow = new Arrow(edge.getSource().getElement().getxPos(), edge.getSource().getElement().getyPos(),
				edge.getTarget().getElement().getxPos(), edge.getTarget().getElement().getyPos(), 5.0);
		
		// calcolo il punto finale effettivo considerando il raggio del nodo ed il suo contorno
		arrow.calculateEndWithOffset(Singleton.NODE_RADIUS + 3.0);
		arrow.setSourceLabel(edge.getSource().getElement().getIndex() + "");
		arrow.setTargetLabel(edge.getTarget().getElement().getIndex() + "");
		
		
		this.currentPane.getChildren().add(0, arrow);
		
		// comunico che l'arco è stato creato
		Singleton.getInstance().logger.log("L'arco tra il nodo con indice " + arrow.getSourceLabel() + " e quello con indice " +
				arrow.getTargetLabel() + " è stato creato.");
		
		
		// pongo a null currentNode per segnalare che non si stanno più collegando due nodi
		MainController.currentNode = null;
	}
	
	/**
	 * Elimina tutti i nodi figli all'interno del pannello in cui è visualizzato il grafo
	 */
	public void reset() {
		this.currentPane.getChildren().clear();
	}

	/**
	 * Ritorna il cerchio che racchiuderà l'indice del nodo
	 * @return cerchio che indicherà il nodo.
	 */
	private Circle createCircle() {
				
		Circle c = new Circle();
		c.setFill((Color) currentPane.getBackground().getFills().get(0).getFill());
		c.setStroke(Color.BLACK);
		c.setStrokeWidth(3);
		
		c.setRadius(Singleton.NODE_RADIUS);
		
		return c;
	}
	
	
	/**
	 * Ritorna il crea il componente grafico testo utilizzando la stringa passata come parametro
	 * @param s testo da visualizzare
	 * @return testo creato come componente grafico.
	 */
	private Text createText(String s) {
		Text t = new Text();
		
		t.setText(s);
		t.setFont(new Font(16));
		t.setBoundsType(TextBoundsType.VISUAL);
		t.setStroke(Color.BLACK);
		this.centerText(t);
		
		return t;
	}
	
	
	// http://stackoverflow.com/questions/17437411/how-to-put-a-text-into-a-circle-object-to-display-it-from-circles-center
	/**
	 * Centra il testo rispetto al cerchio in cui è inserito
	 * @param t	testo da centrare
	 */
	private void centerText(Text t) {
		double radius = Singleton.NODE_RADIUS;
		
		double w = t.getBoundsInLocal().getWidth();
		double h = t.getBoundsInLocal().getHeight();
		
		t.relocate(radius - (w / 2), radius - (h / 2));
	}
}
