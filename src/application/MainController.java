package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
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
		
		// se clicco su uno spazio vuoto mentro sto collegando due nodi annullo l'operazione ed esco dal metodo
		if (currentNode != null) {
			currentNode = null;
			return;
		}
				
		Singleton s = Singleton.getInstance();
		
		if (!s.graphLoaded) {	// non c'è ancora nessun grafo visualizzato, quindi gestisco il click
			
			double xPos = e.getX();
			double yPos = e.getY();
			
			Integer index = 0;
			
			Node<CoordinateNode> maxIndexKey = s.getCurrentGraph().getMaxKey();
			if (maxIndexKey != null) {
				index = ((CoordinateNode) maxIndexKey.getElement()).getIndex() + 1;
			}
				
			// disegno il nodo come un intero (indice del nodo) all'interno di un cerchio
			this.drawNode(index, xPos, yPos);
			
			// inserisco il nuovo nodo nel grafo
			s.getCurrentGraph().insertNode(new Node<CoordinateNode>(new CoordinateNode(index, xPos, yPos)));
			
			// s.getCurrentGraph().print();
		}
	}
	
	
	private void drawNode(Integer index, double xPos, double yPos) {
		
		// se currentNode non è null vuol dire che mi trovo gia su un nodo, quindi non faccio nulla
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
				System.out.println("Clicked node " + index);
				System.out.println(currentNode);
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
				
				System.out.println("Target node " + index);
	    		
	    		Arrow edge = new Arrow(currentNode.getxPos(), currentNode.getyPos(), xPos, yPos, 5.0);
	    		// calcolo il punto finale effettivo considerando il raggio del nodo ed il suo contorno
	    		edge.calculateEndWithOffset(radius + 3.0);
	    		edge.setSourceLabel(currentNode.getIndex() + "");
	    		edge.setTargetLabel(index + "");
	    		
	    		
	    		graphPane.getChildren().add(0, edge);
	    		
	    		
	    		// pongo a null currentNode per segnalare che non si stanno più collegando due nodi
	    		currentNode = null;
	    		
	    		System.out.println(edge);
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
    
    
    @FXML
    void handleMenuItem_Stop(ActionEvent event) {

    }
    
    
	@FXML
    void handleMenuItem_RunAnimation(ActionEvent event) {
    	
    	MainController.run(event);
    }


    /**
     * Metodo che imposta tutte le funzioni che devono essere eseguite durante l'avanzamento
     * dell'algotitmo
     * @param bfs oggetto di tipo GraphVisiter di cui impostare le funzioni.
     */
	private static void completeAnimationSetup(GraphVisiter bfs) {
		
		bfs.setOnANode((currentNode) -> {
			Node<CoordinateNode> n = (Node<CoordinateNode>) currentNode;
			
			Singleton.getInstance().logger.log(">> On the " + n.getElement().getIndex() + " node");
			return null;
		});
		
		
		bfs.setExaminingEdge((edge) -> {
			Edge<CoordinateNode> e = (Edge<CoordinateNode>) edge;
			
			Singleton.getInstance().logger.log("Examining edge from "
					+ e.getSource().getElement().getIndex()
					+ " to " + e.getTarget().getElement().getIndex());
			return null;
		});
		
		
		bfs.setNodeInserted((edge) -> {
			Edge<CoordinateNode> e = (Edge<CoordinateNode>) edge;
			
			Singleton.getInstance().logger.log("++ Node " + e.getTarget().getElement().getIndex() + " inserted");
			return null;
		});
		
		
		bfs.setNodeNotInserted((edge) -> {
			Edge<CoordinateNode> e = (Edge<CoordinateNode>) edge;
			
			Singleton.getInstance().logger.log("-- Node " + e.getTarget().getElement().getIndex() + " not inserted");
			return null;
		});
		
		
		bfs.setShowVisited((visited) -> {
			return null;
		});
		
		
		bfs.setFunctionEnded((Void) -> {
			Singleton.getInstance().logger.log("Animation ended!");
			setMenuItemState(false);
			
			return null;
		}); 
	}
	
	
	private static void setMenuItemState(Boolean animationIsRunning) {
		
		Singleton.getInstance().isAnimating = animationIsRunning;

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
    
    
    public static Void nextStep(Event e) {
    	if (!Singleton.getInstance().isAnimating)
    		return null;
    	
    	GraphVisiter bfs = (GraphVisiter) Singleton.getInstance().getThreadByName(AnimationSettings.THREAD_NAME);
    	
    	if (bfs != null && bfs.isAlive()) {
    		synchronized (bfs) {
    			bfs.notify();
    		}
    	}
    	
    	return null;
    }
    
    
    @SuppressWarnings("unchecked")
	public static Void run(Event e) {
    	
    	Singleton s = Singleton.getInstance();
    	
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
    	
    	Graph<CoordinateNode> currentGraph = s.getCurrentGraph();
		Node<CoordinateNode> root = s.animPrefs.getRoot();
		
		// se il nodo radice è null ne scelgo uno a caso tra i presenti
		if (root == null) {
			String warningMsg = "Warning: source node not set, a random one will be chosen.";
			s.logger.log(warningMsg);

	    	root = (Node<CoordinateNode>) currentGraph.V().toArray()[0];
			s.animPrefs.setRoot(root);
		}
		
		// creo l'oggetto GraphVisiter e setto il nome del thread 
		GraphVisiter bfs = new GraphVisiter(currentGraph, root);
		bfs.setName(AnimationSettings.THREAD_NAME);
    	
		// imposto le funzioni da richiamare durante l'esecuzione dell'algoritmo
    	completeAnimationSetup(bfs);
    	
    	//avvio il thread
    	bfs.start();
    	
    	// TODO: aggiornare il commento
    	// https://stackoverflow.com/a/14742290/5684086
    	// ogni volta che l'esecuzione passa su questo thread (main), se il
    	// thread secondario è attivo allora lo faccio ripartire, in questo modo avrò
    	// un'esecuzione continua, ma solo nel caso in cui non sia attiva l'esecuzione Step-by-Step
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
}






























