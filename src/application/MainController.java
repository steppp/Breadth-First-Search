package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import bfs.GraphVisiter;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.graphs.Edge;
import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import singleton.Singleton;
import utility.AnimationSettings;
import utility.Logger;
import model.arrow.Arrow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class MainController implements Initializable {
	
	static CoordinateNode currentNode = null;
	static boolean mouseOverNode = false;
	
	
	// -------------- CAMPI FXML ------------------
	
	@FXML
	private Pane graphPane;
	
	@FXML
	private TextArea outputTextArea;
	
	@FXML
	private TextField pseudocodeTextArea;

    @FXML
    private Text coordinateLabel;
    
    @FXML
    private MenuItem runMenuItem;

    @FXML
    private MenuItem sbsMenuItem;

    @FXML
    private MenuItem stopMenuItem;

    @FXML
    private MenuItem stepMenuItem;
    
    // -------------------------------------------
	
	
	@FXML
	private void handleButtonClick_GraphPane(MouseEvent e) {
		
		// se mi trovo già su un nodo questo metodo non deve essere eseguito
		if (mouseOverNode) return;
		
		Singleton s = Singleton.getInstance();
		
		// se clicco su uno spazio vuoto mentro sto collegando due nodi annullo l'operazione ed esco dal metodo
		if (currentNode != null) {
			
			s.logger.log("Task canceled.");
			currentNode = null;
			
			return;
		}
		
		if (!s.graphLoaded) {	// non c'è ancora nessun grafo visualizzato, quindi gestisco il click
			
			double xPos = e.getX();
			double yPos = e.getY();
			
			Integer index = 0;
			
			// ricavo l'indice massimo tra i nodi del grafo
			Node<CoordinateNode> maxIndexKey = s.getCurrentGraph().getMaxKey();
			if (maxIndexKey != null) {
				index = ((CoordinateNode) maxIndexKey.getElement()).getIndex() + 1;
			}
				
			// disegno il nodo come un intero (indice del nodo) all'interno di un cerchio
			this.drawNode(index, xPos, yPos);
			
			// inserisco il nuovo nodo nel grafo
			s.getCurrentGraph().insertNode(new Node<CoordinateNode>(new CoordinateNode(index, xPos, yPos)));
		}
	}
	
	
	private void drawNode(Integer index, double xPos, double yPos) {
		
		// se currentNode non è null vuol dire che mi trovo già su un nodo, quindi non faccio nulla
		if (MainController.currentNode != null) return;
		
		double radius = Singleton.getInstance().NODE_RADIUS;
		
		Circle c = this.createCircle();
		Text t = this.createText(index.toString());
		
		StackPane sp = new StackPane();
		sp.setLayoutX(xPos - radius);
		sp.setLayoutY(yPos - radius);
		
		sp.getChildren().addAll(c, t);
		
		sp.setOnMouseClicked((MouseEvent e) -> {
			
			// primo click su un nodo già esistente -> faccio partire l'azione per collegare due nodi
			if (currentNode == null) {
				
				MainController.currentNode = new CoordinateNode(index, xPos, yPos);
				
				// comunico all'utente che il nodo sorgente dell'arco è stato impostato
				Singleton.getInstance().logger.log("Source node selected: " + currentNode.toString() +
						", waiting for the target node..");
			} else {
				// secondo click su un altro nodo -> creo il collegamento tra i due se non si tratta dello stesso nodo
				
				// stesso nodo, non faccio niente
				if (index == currentNode.getIndex()) return;
	    		
				
	    		// creazione del vertice tra i due nodi
	    		Graph<CoordinateNode> graph = Singleton.getInstance().getCurrentGraph();
	    		Node<CoordinateNode> source = graph.getNodeWithValue(currentNode);
	    		Node<CoordinateNode> target = graph.getNodeWithValue(new CoordinateNode(index, xPos, yPos));
				
	    		// se non è stato possibile creare il vertice non faccio nulla
				if (!graph.insertEdge(source, target)) return;
	    		
	    		Arrow edge = new Arrow(currentNode.getxPos(), currentNode.getyPos(), xPos, yPos, 5.0);
	    		// calcolo il punto finale effettivo considerando il raggio del nodo ed il suo contorno
	    		edge.calculateEndWithOffset(radius + 3.0);
	    		edge.setSourceLabel(currentNode.getIndex() + "");
	    		edge.setTargetLabel(index + "");
	    		
	    		
	    		graphPane.getChildren().add(0, edge);
	    		
	    		// cmomunico che l'arco è stato creato
	    		Singleton.getInstance().logger.log("Edge from " + edge.getSourceLabel() + " to " +
	    				edge.getTargetLabel() + " has been created.");
	    		
	    		
	    		// pongo a null currentNode per segnalare che non si stanno più collegando due nodi
	    		currentNode = null;
			}
		});
		
		sp.setOnMouseEntered((MouseEvent e) -> {
			mouseOverNode = true;
		});
		
		sp.setOnMouseExited((MouseEvent e) -> {
			mouseOverNode = false;
		});
		
		this.graphPane.getChildren().add(sp);
	}
	
	
	private Circle createCircle() {
		
		Singleton s = Singleton.getInstance();
		
		Circle c = new Circle();
		c.setFill((Color) graphPane.getBackground().getFills().get(0).getFill());
		c.setStroke(Color.BLACK);
		c.setStrokeWidth(3);
		
		c.setRadius(s.NODE_RADIUS);
		
		return c;
	}
	
	
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
	private void centerText(Text t) {
		double radius = Singleton.getInstance().NODE_RADIUS;
		
		double w = t.getBoundsInLocal().getWidth();
		double h = t.getBoundsInLocal().getHeight();
		
		t.relocate(radius - (w / 2), radius - (h / 2));
	}
	
	
	@FXML
    private void handleMenuItem_OpenFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		this.configureFileChooser(fc);
		
		// apre un nuovo FileDialog usando lo stage corrente come responsabile
		// il file che verrà scelto sarà salvato nella variabile file
		File file = fc.showOpenDialog(graphPane.getScene().getWindow());
		
		// TODO: - Gestire l'apertura del file: "pulire" il grafo già esistente e validare il file appena aperto
		String filePath = file.getAbsolutePath();
		
		Singleton.getInstance().logger.log(filePath);
	}
	
	
	private void configureFileChooser(final FileChooser fc) {
		fc.setTitle("Open graph from json file");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All files", "*.*"),
				new FileChooser.ExtensionFilter("JSON", "*.json"));
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.graphPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
		
		this.coordinateLabel.setText("0.0, 0.0");
		
		// creo l'oggetto Logger nel Singleton per scrivere nella TextArea
		Singleton.getInstance().logger = new Logger(this.outputTextArea);
		
		// salvo una copia dell'istanza di questo controller nel Singleton
		// per accedervi nei metodi statici
		Singleton.getInstance().mainViewController = this;

		// imposto lo stato iniziale dei menu
		setMenuItemState(false);
	}
	
	
    @FXML
    private void handleMenuItem_Delete(ActionEvent event) {
    	
    	Singleton.getInstance().currentNodeAndList = null;
    	Singleton.getInstance().setCurrentGraph(new Graph<CoordinateNode>());
    	graphPane.getChildren().clear();
    	
    	Singleton.getInstance().logger.log("Graph cleared\n");
    }
    
    
    @FXML
    private void handleMenuItem_Debug(ActionEvent event) {
    	
    	Graph<CoordinateNode> g = Singleton.getInstance().getCurrentGraph();
    	
    	if (g != null && !g.V().isEmpty()) {
	    	Singleton.getInstance().logger.log("\n-----------------------------\nHere is the graph:\n\n" + g.toString() + "-----------------------------\n");
    	} else {
    		Singleton.getInstance().logger.log("No graph loaded!");
    	}
    }
    
    
    @FXML
    private void handleMouseMove_GraphPane(MouseEvent event) {
    	coordinateLabel.setText(event.getX() + ", " + event.getY());
    }
    
    
    // TODO: inserire metodo per eliminare un nodo graficamente
    // TODO: inserire metodo per eliminare un vertice graficamente
    
    
    @FXML
    void handleMenuItem_AnimationSettings(ActionEvent event) {
    	
    	try {
    		Parent root = FXMLLoader.load(getClass().getResource("applicationSettingsView.fxml"));
    		Stage stage = new Stage();
    		stage.setTitle("Animation Settings");
    		stage.setScene(new Scene(root, 400, 150));
    		stage.show();
    		stage.setResizable(false);
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    
    @SuppressWarnings("deprecation")
	@FXML
    void handleMenuItem_Stop(ActionEvent event) {
    	Singleton.getInstance().currentNodeAndList = null;
    	
    	// interrompo il thread dell'animazione
    	Thread bfs = Singleton.getInstance().getThreadByName(AnimationSettings.THREAD_NAME);
    	if (bfs != null && bfs.isAlive())
    		// deprecato, ma non è stato possibile fare altrimenti
    		bfs.stop();
    	
    	// resetto il colore originale del grafo
    	resetGraphColor(this.graphPane, Color.BLACK);
    }
    
    
    public static void resetGraphColor(final Pane graphPane, final Color c) {
    	for (javafx.scene.Node n : graphPane.getChildren()) {
    		if (n instanceof Arrow)
    			((Arrow) n).setColor(c);	// coloro la freccia di nero
    		else {
    			StackPane sp = ((StackPane) n);
    			
    			// coloro il cerchio ed il testo di nero
				((Shape) sp.getChildren().get(0)).setStroke(c);
				((Shape) sp.getChildren().get(1)).setStroke(c);
    		}
    	}
    }
    
    
	@FXML
    void handleMenuItem_RunAnimation(ActionEvent event) {
		
		// resetto il colore originale del grafo
    	resetGraphColor(this.graphPane, Color.BLACK);
    	
    	MainController.run(event);
    }


    /**
     * Metodo che imposta tutte le funzioni che devono essere eseguite durante l'avanzamento
     * dell'algotitmo
     * @param bfs oggetto di tipo GraphVisiter di cui impostare le funzioni.
     */
	private static void completeAnimationSetup(GraphVisiter bfs) {
		
		bfs.setOnANode((currentNode) -> {
			Singleton s = Singleton.getInstance();
			
			// ripristino il colore del nodo precedente se esiste
			if (s.currentNodeAndList != null)
				paintNode(s.getCurrentGraph(), s.currentNodeAndList.getKey(), Color.GREEN);
			
			// coloro il nodo specificato
			paintNode(s.getCurrentGraph(), currentNode, Color.CORAL);
			s.currentNodeAndList = new SimpleEntry<>(
							s.getCurrentGraph().getNodeWithValue(currentNode.getElement()),
							s.getCurrentGraph().adj(currentNode));
			
			Singleton.getInstance().logger.log(">> On the node " + currentNode.getElement().getIndex());
			return null;
		});
		
		
		bfs.setExaminingEdge((edge) -> {
			paintEdge(Singleton.getInstance().getCurrentGraph(), edge, Color.DARKGRAY);
			
			Singleton.getInstance().logger.log("Examining edge from "
					+ edge.getSource().getElement().getIndex()
					+ " to " + edge.getTarget().getElement().getIndex());
			return null;
		});
		
		
		bfs.setNodeInserted((edge) -> {
			Singleton s = Singleton.getInstance();
			
			paintNode(s.getCurrentGraph(), edge.getTarget(), Color.GREEN);
			paintEdge(s.getCurrentGraph(), edge, Color.BLACK);
			
			// rimuovo gli elementi dalla lista di adiacenza della copia del nodo sorgente
			// e, se una volta fatto ciò si svuota, coloro il nodo con il colore precedente
			s.currentNodeAndList.getValue().remove(edge.getTarget());
			if (s.currentNodeAndList.getValue().isEmpty())
				paintNode(Singleton.getInstance().getCurrentGraph(), edge.getSource(), Color.GREEN);
			
			Singleton.getInstance().logger.log("++ Node " + edge.getTarget().getElement().getIndex() + " inserted");
			return null;
		});
		
		
		bfs.setNodeNotInserted((edge) -> {
			Singleton s = Singleton.getInstance();
			
			paintNode(Singleton.getInstance().getCurrentGraph(), edge.getTarget(), Color.GREEN);
			paintEdge(Singleton.getInstance().getCurrentGraph(), edge, Color.DARKGRAY);
			
			// vedi metodo precedente
			s.currentNodeAndList.getValue().remove(edge.getTarget());
			if (s.currentNodeAndList.getValue().isEmpty())
				paintNode(Singleton.getInstance().getCurrentGraph(), edge.getSource(), Color.GREEN);
			
			Singleton.getInstance().logger.log("-- Node " + edge.getTarget().getElement().getIndex() + " not inserted");
			return null;
		});
		
		
		bfs.setShowVisited((visited) -> {
			return null;
		});
		
		
		bfs.setFunctionEnded((Void) -> {
			// ripristino il colore del nodo precedente se esiste
			if (Singleton.getInstance().currentNodeAndList != null)
				paintNode(Singleton.getInstance().getCurrentGraph(), Singleton.getInstance().currentNodeAndList.getKey(), Color.GREEN);
			
			Singleton.getInstance().logger.log("Animation ended!");
			setMenuItemState(false);
			
			return null;
		}); 
	}
	
	
	/**
	 * Imposta lo stato dei controlli nel menu per eseguire l'algoritmo
	 * @param animationIsRunning true se è in corso o sta per iniziare un'animazione, false altrimenti.
	 */
	private static void setMenuItemState(final Boolean animationIsRunning) {
		
		Singleton.getInstance().isAnimating = animationIsRunning;

		// ottengo l'istanza del controller dal Singleton
		MainController c = Singleton.getInstance().mainViewController;

    	// imposto lo stato dei controlli
    	c.runMenuItem.setDisable(animationIsRunning);
    	c.sbsMenuItem.setDisable(animationIsRunning);
    	c.stopMenuItem.setDisable(!animationIsRunning);
    	c.stepMenuItem.setDisable(!animationIsRunning);
	}
	
	
    @FXML
    void handleMenuItem_NextStep(ActionEvent event) {
    	MainController.nextStep(event);
    }
    
    
    /**
     * Esegue uno step dell'animazione
     * @param e	evento per cui è stato richiamato il metodo.
     * @return Void
     */
    public static Void nextStep(Event e) {
    	if (!Singleton.getInstance().isAnimating)
    		return null;
    	
    	// ottengo il thread
    	GraphVisiter bfs = (GraphVisiter) Singleton.getInstance().getThreadByName(AnimationSettings.THREAD_NAME);
    	
    	// se il thread esiste ed è ancora in esecuzione, lo riprendo
    	if (bfs != null && bfs.isAlive()) {
    		synchronized (bfs) {
    			bfs.notify();
    		}
    	}
    	
    	return null;
    }
    
    
	public static Void run(Event e) {
    	
    	Singleton s = Singleton.getInstance();
 
    	// se il grafo non contiene nodi
    	if (s.getCurrentGraph().V().size() == 0) {
    		s.logger.log("No graph loaded.");
    		return null;
    	}
    	
    	Graph<CoordinateNode> currentGraph = s.getCurrentGraph();
		Node<CoordinateNode> root = s.animPrefs.getRoot();
		
		// se il nodo radice è null interrompo l'operazione
		if (root == null) {
			String errMsg = "Source node not set, aborting.";
			s.logger.log(errMsg);

	    	return null;
		}
    	
    	// se esiste già un Thread con il nome predefinito (è già in corso un'animazione)
    	// allora lo interrompo prima di avviare questo
    	Thread existingThread = s.getThreadByName(AnimationSettings.THREAD_NAME);
    	if (existingThread != null) {
    		// TODO: avvisare dell'interruzione del Thread
    		existingThread.interrupt();
    	}
    	
    	// controllo quale bottone è stato cliccato e, se è quello step-by-step, la variabile diventa true
    	// controllo aggiuntivo nel caso sia stato chiamato in seguito ad un comando da tastiera
    	boolean stepByStep = false;
    	if (e.getSource() instanceof MenuItem) {
    		stepByStep = ((MenuItem) e.getSource()).getId().equals("stepbystep");
    	} else {	// evento partito dalla tastiera
    		stepByStep = ((KeyEvent) e).getCode() == KeyCode.S;
    	}
    	
    	// imposto lo stato dei controlli
    	setMenuItemState(true);
		
		// creo l'oggetto GraphVisiter e setto il nome del thread 
		GraphVisiter bfs = new GraphVisiter(currentGraph, root);
		bfs.setName(AnimationSettings.THREAD_NAME);
    	
		// imposto le funzioni da richiamare durante l'esecuzione dell'algoritmo
    	completeAnimationSetup(bfs);
    	
    	//avvio il thread
    	bfs.start();
    	
    	// https://stackoverflow.com/a/14742290/5684086
    	// se l'esecuzione non è step-by-step, avvio un timer che ad ogni tick, il cui intervallo è indicato
    	// dal campo interval nel Singleton, se il thread dell'animazione è ancora attivo, lo riprende
    	if (!stepByStep) {
        	Singleton.getInstance().timer = new Timer();
        	long interval = Singleton.getInstance().animPrefs.getInterval();
        	
        	Singleton.getInstance().timer.scheduleAtFixedRate(new TimerTask() {
    			@Override
    			public void run() {
    				if (bfs.isAlive())
    		    		synchronized (bfs) {
    			    		bfs.notify();
    			    	}
    			}
        	}, interval, interval);
    	}
    	
    	return null;
    }
    
    
	/***
	 * Colora il nodo indicato dal parametro node del grafo g del colore c
	 * @param g grafo in cui si trova il nodo.
	 * @param node nodo da colorare.
	 * @param c colore da applicare al nodo
	 */
    private static void paintNode(Graph<CoordinateNode> g, final Node<CoordinateNode> node, final Color c) {
    	MainController mainC = Singleton.getInstance().mainViewController;
    	
    	StackPane visualNode = null;
    	Text nodeValue = null;
    	
    	// per ogni nodo figlio del pannello
    	for (javafx.scene.Node n : mainC.graphPane.getChildren()) {
    		if (n instanceof StackPane) {	// se trovo uno StackPane
    			visualNode = (StackPane) n;	// cast esplicito
    			
    			// per ogni figlio dello StackView
    			for (javafx.scene.Node possibleText : visualNode.getChildren()) {
    				if (possibleText instanceof Text) {	// se il nodo è un Testo
    					nodeValue = (Text) possibleText;
    					
    					// se il testo trovato è uguale all'indice del nodo passato come parametro
    					if (nodeValue.getText().equals(node.getElement().toString())) {
    						nodeValue.setStroke(c);		// lo coloro

    	    				((Shape) visualNode.getChildren().get(0)).setStroke(c);
    	    				((Shape) visualNode.getChildren().get(1)).setStroke(c);
    					}
    				}
    			}
    		}
    	}
    }
    
    
    /**
     * Colora il vertice edge del grafo g del colore c
     * @param g grafo in cui si trova il vertice.
     * @param edge vertice da considerare.
     * @param c colore da applicare.
     */
    private static void paintEdge(Graph<CoordinateNode> g, final Edge<CoordinateNode> edge, final Color c) {
    	MainController mainC = Singleton.getInstance().mainViewController;
    	Arrow a = null;
    	
    	// per ogni nodo figlio del pannello
    	for (javafx.scene.Node n : mainC.graphPane.getChildren()) {
    		
    		// se il nodo è un'istanza di Arrow
    		if (n instanceof Arrow) {
    			a = (Arrow) n;	// lo converto in Arrow
    			
    			// se il vertice passato corrisponde alla freccia
    			// cioè origine della freccia == origine del vertice
    			// & destinazione della freccia == destinazione del vertice
    			if (a.getSourceLabel().equals(edge.getSource().getElement().toString()) &&
    					a.getTargetLabel().equals(edge.getTarget().getElement().toString())) {
					a.setColor(c);
    			}
    		}
    	}
    }
}






























