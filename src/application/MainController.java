package application;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.AbstractMap.SimpleEntry;
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
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.graphs.Edge;
import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import model.randomGraph.RandomGraph;
import singleton.Singleton;
import utility.AnimationSettings;
import utility.GraphDrawer;
import utility.Logger;
import model.JSONFiles.JSONFileReader;
import model.JSONFiles.JSONFileWriter;
import model.arrow.Arrow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class MainController implements Initializable {
	
	public static CoordinateNode currentNode = null;
	public static boolean mouseOverNode = false;
	
	
	// -------------- CAMPI FXML ------------------
	
	@FXML
	private Pane graphPane;
	
	@FXML
	private TextArea outputTextArea;
	
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
    
    @FXML
    private ScrollPane scrollPaneVisited;

    @FXML
    private VBox vBoxVisited;

    @FXML
    private ScrollPane scrollPaneParents;

    @FXML
    private VBox vBoxParents;
    
    @FXML
    private SplitMenuButton splitMenuItem;
    
    // -------------------------------------------
	
	
	@FXML
	private void handleButtonClick_GraphPane(MouseEvent e) {
		
		// se mi trovo già su un nodo questo metodo non deve essere eseguito
		if (mouseOverNode) return;
		
		Singleton s = Singleton.getInstance();
		
		// se clicco su uno spazio vuoto mentro sto collegando due nodi annullo l'operazione ed esco dal metodo
		if (currentNode != null) {
			
			s.logger.log("Operazione annullata.");
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
			
			// se currentNode non è null vuol dire che mi trovo già su un nodo, quindi non faccio nulla
			if (MainController.currentNode != null) return;
			
			Singleton.getInstance().drawingUtility.drawNode(index, xPos, yPos);
			
			// inserisco il nuovo nodo nel grafo
			s.getCurrentGraph().insertNode(new Node<CoordinateNode>(new CoordinateNode(index, xPos, yPos)));
		}
	}
	
	
	@FXML
    private void handleMenuItem_OpenFile(ActionEvent event) {
		FileChooser fc = new FileChooser();
		this.configureFileChooser(fc);
		
		// apre un nuovo FileDialog usando lo stage corrente come responsabile
		// il file che verrà scelto sarà salvato nella variabile file
		File file = fc.showOpenDialog(graphPane.getScene().getWindow());
		
		// se non è stato scelto alcun file termino il metodo
		if (file == null) {
			return;
		}
		
		// pulisco la scena
		this.handleMenuItem_Delete(null);
		
		String filePath = file.getAbsolutePath();

		// carico il grafo dal file json
    	JSONFileReader jsonReader = new JSONFileReader(filePath);
    	Graph<CoordinateNode> g = jsonReader.readGraphFromJSONFilereader();
    	
    	// imposto il grafo come grafo corrente e lo disegno
    	Singleton.getInstance().setCurrentGraph(g);
    	
    	if (Singleton.getInstance().drawingUtility.drawGraph(g))
    		Singleton.getInstance().logger.log("Il file \"" + filePath + "\" è stato caricato.");
	}
	
	
    @FXML
    void handleMenuItem_RandomGraph(ActionEvent event) {
    	
    	// pulisco la scena
    	this.handleMenuItem_Delete(null);
    	
    	RandomGraph<CoordinateNode> rg = null;
    	Integer size = Singleton.nodesNumber;
    	
    	if (size == null || size == 0)
    		rg = new RandomGraph<CoordinateNode>();
    	else rg = new RandomGraph<CoordinateNode>(size);
    	
    	if (Singleton.getInstance().drawingUtility.drawGraph(rg.getGraph())) {
        	Singleton.getInstance().setCurrentGraph(rg.getGraph());
        	Singleton.getInstance().logger.log("Grafo casuale caricato");
    	}
    }
	
	
	/**
	 * Imposta i parametri del FileChooser passato come parametro
	 * @param fc costnte di tipo FileChooser di cui impostare i parametri.
	 */
	private void configureFileChooser(final FileChooser fc) {
		fc.setTitle("Open graph from json file");
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
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
		
		// creo l'oggetto con cui si potrà disegnare il grafo sul pannello
		Singleton.getInstance().drawingUtility = new GraphDrawer(this.graphPane);

		// imposto lo stato iniziale dei menu
		setAnimationIsRunning(false);

		scrollPaneParents.setFitToWidth(true);
		scrollPaneVisited.setFitToWidth(true);
		
		
		splitMenuItem.getItems().clear();
		splitMenuItem.setText("0");
		
		for (int i = 0; i <= 20; i++) {
			MenuItem mi = new MenuItem(i + "");
			mi.setOnAction((event) -> {
				try {
					Integer value = Integer.parseInt(((MenuItem) event.getSource()).getText());
					Singleton.nodesNumber = value;
					splitMenuItem.setText(value + "");
				} catch (Exception e) {
					Singleton.nodesNumber = null;
				}
			});
			
			splitMenuItem.getItems().add(mi);
		}
	}
	
	
    @FXML
    private void handleMenuItem_Delete(ActionEvent event) {
    	
    	// fermo l'animazione
    	MainController.stop(event);
    	
    	Singleton s = Singleton.getInstance();
    	
    	// pulisco tutto lo spazio di lavoro
    	s.animPrefs = new AnimationSettings();
    	s.currentNodeWithList = null;
    	s.setCurrentGraph(new Graph<CoordinateNode>());
    	graphPane.getChildren().clear();
    	
    	Singleton.filePath = null;
    	
    	this.vBoxParents.getChildren().clear();
    	this.vBoxVisited.getChildren().clear();
    	
    	s.logger.clear();
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
    private void handleMenuItem_Close(ActionEvent event) {
    	
        // https://stackoverflow.com/questions/11468800/javafx2-closing-a-stage-substage-from-within-itself#comment21432693_11476162
    	Stage primaryStage = (Stage) this.graphPane.getScene().getWindow();
    	
    	// chiudo la finestra ma prima richiamo il gestore per l'evento di chiusura
    	// che altrimenti non si attiverebbe
    	primaryStage.getOnCloseRequest().handle(null);
    	primaryStage.close();
    }
    
    
    /**
     * Gestisce il salvataggio normale
     * @param event
     */
    @FXML
    void handleMenuItem_Save(ActionEvent event) {
    	if (Singleton.getInstance().getCurrentGraph() == null)
    		return;
    	
    	if (Singleton.filePath == null)
    		this.handleMenuItem_SaveAs(event);
    	else {
    		this.save(Singleton.filePath, Singleton.getInstance().getCurrentGraph());
    	}
    }
    
    
	/**
	 * Gestisce il salvataggio "personalizzato"
	 * @param event
	 */
    @FXML
    void handleMenuItem_SaveAs(ActionEvent event) {
    	if (Singleton.getInstance().getCurrentGraph() == null)
    		return;
    	
		FileChooser fc = new FileChooser();
		this.configureFileChooser(fc);
		
		File file = fc.showSaveDialog(graphPane.getScene().getWindow());
		
		if (file != null)
			this.save(file.getAbsolutePath(), Singleton.getInstance().getCurrentGraph());
    }
    
    
    /**
     * Salva il grafo g su disco in formato .json utilizzando la classe apposita
     * @param filePath percorso in cui salvare il file.
     * @param g grafo sa salvare.
     */
    private void save(String filePath, Graph<CoordinateNode> g) {
    	if (g == null)
    		return;
    	
    	if (filePath != null) {
			JSONFileWriter jsonWriter = new JSONFileWriter();
			jsonWriter.writeFileWithJSONFileWriter(filePath, g);
			
			Singleton.getInstance().logger.log("File salvato nel percorso \""
					+ filePath + "\"");
			
			Singleton.filePath = filePath;
    	}
    }
    
    
    @FXML
    private void handleMouseMove_GraphPane(MouseEvent event) {
    	coordinateLabel.setText(event.getX() + ", " + event.getY());
    }
    
    
    @FXML
    void handleMenuItem_AnimationSettings(ActionEvent event) {
    	MainController.showPrefWindow(event);
    }
    
    
    /**
     * Mostra la finestra per impostare le preferenze dell'animazione.
     * @param e evento scatenante.
     * @return Void.
     */
    public static Void showPrefWindow(Event e) {

    	try {
    		Parent root = FXMLLoader.load(Singleton.getInstance().mainViewController.getClass().getResource("applicationSettingsView.fxml"));
    		Stage stage = new Stage();
    		stage.setTitle("Animation Settings");
    		stage.setScene(new Scene(root, 400, 150));
    		stage.show();
    		stage.setResizable(false);
    	} catch (IOException exc) {
    		exc.printStackTrace();
    	}
    	
    	return null;
    }
    

	@FXML
    void handleMenuItem_Stop(ActionEvent event) {
		MainController.stop(event);
    }
	
	
	/**
	 * Arresta l'animazione preoccupandosi di ripristinare lo stato dell'applicazione a come era prima dell'esecuzione
	 * dell'animazione
	 * @param e evento scatenante.
	 * @return Void.
	 */
	public static Void stop(Event e) {
    	Singleton.getInstance().currentNodeWithList = null;

    	// elimina il thread ed il timer
    	MainController.performCleanUp(null);
    	
    	// imposto i controlli indicando che l'animazione non sta proseguendo
    	setAnimationIsRunning(false);
    	
    	// resetto il colore originale del grafo
    	resetGraphColor(Singleton.getInstance().mainViewController.graphPane, Color.BLACK);
    	
    	return null;
	}
	
	
    @FXML
    void handleMenuItem_About(ActionEvent event) {
    	try {
        	MainController.openWebPage(new URL(Singleton.ABOUT_WEB_PAGE));
    	} catch (Exception e) {
    		e.printStackTrace();
			Singleton.getInstance().logger.log("Impossibile aprire il browser. Aprirlo manualmente ed andare all'indirizzo "
					+ Singleton.ABOUT_WEB_PAGE);
    	}
    }
    
    
    /**
     * Apre la pagina web indicata dal parametro uri nel browser predefinito
     * @param uri pagina web da aprire.
     * @throws Exception se l'uri non è valido.
     */
    private static void openWebPage(URI uri)
    					throws Exception {
    	Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
    	
    	if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			desktop.browse(uri);
    	}
    }
    
    
    /**
     * Apre la pagina web indicata dal parametro url nel browser predefinito.
     * @param url pagina web da aprire.
     * @throws Exception se l'url non è valido.
     */
    private static void openWebPage(URL url)
    					throws Exception {
		openWebPage(url.toURI());
    }
    
    
    /**
     * Imposta il colore c come colore di tutti i figli del pannello passato
     * @param graphPane padre dei componenti grafici da colorare.
     * @param c colore da applicare agli elementi
     */
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
			if (s.currentNodeWithList != null)
				paintNode(s.getCurrentGraph(), s.currentNodeWithList.getKey(), Color.LIMEGREEN);
			
			// coloro il nodo specificato
			paintNode(s.getCurrentGraph(), currentNode, Color.CORAL);
			s.currentNodeWithList = new SimpleEntry<>(
							s.getCurrentGraph().getNodeWithValue(currentNode.getElement()),
							s.getCurrentGraph().adj(currentNode));
			
			Singleton.getInstance().logger.log(">> Mi trovo sul nodo con indice " + currentNode.getElement().getIndex());
			return null;
		});
		
		
		bfs.setExaminingEdge((edge) -> {
			paintEdge(Singleton.getInstance().getCurrentGraph(), edge, Color.DARKGRAY);
			
			Singleton.getInstance().logger.log("?? Esamino l'arco tra il nodo con indice "
					+ edge.getSource().getElement().getIndex()
					+ " e quello con indice " + edge.getTarget().getElement().getIndex());
			return null;
		});
		
		
		bfs.setNodeInserted((edge) -> {
			Singleton s = Singleton.getInstance();
			
			paintNode(s.getCurrentGraph(), edge.getTarget(), Color.LIMEGREEN);
			paintEdge(s.getCurrentGraph(), edge, Color.BLACK);
			
			// rimuovo gli elementi dalla lista di adiacenza della copia del nodo sorgente
			// e, se una volta fatto ciò si svuota, coloro il nodo con il colore precedente
			s.currentNodeWithList.getValue().remove(edge.getTarget());
			if (s.currentNodeWithList.getValue().isEmpty())
				paintNode(Singleton.getInstance().getCurrentGraph(), edge.getSource(), Color.LIMEGREEN);
			
			Singleton.getInstance().logger.log("++ Nodo con indice " + edge.getTarget().getElement().getIndex() + " inserito");
			return null;
		});
		
		
		bfs.setNodeNotInserted((edge) -> {
			Singleton s = Singleton.getInstance();
			
			paintNode(Singleton.getInstance().getCurrentGraph(), edge.getTarget(), Color.LIMEGREEN);
			paintEdge(Singleton.getInstance().getCurrentGraph(), edge, Color.DARKGRAY);
			
			// vedi metodo precedente
			s.currentNodeWithList.getValue().remove(edge.getTarget());
			if (s.currentNodeWithList.getValue().isEmpty())
				paintNode(Singleton.getInstance().getCurrentGraph(), edge.getSource(), Color.LIMEGREEN);
			
			Singleton.getInstance().logger.log("-- Nodo con indice " + edge.getTarget().getElement().getIndex() + " non inserito");
			return null;
		});
		

		// http://www.baeldung.com/java-8-double-colon-operator
		bfs.setShowVisited(MainController::showVisited);
		
		
		bfs.setFunctionEnded((Void) -> {
			// ripristino il colore del nodo precedente se esiste
			if (Singleton.getInstance().currentNodeWithList != null)
				paintNode(Singleton.getInstance().getCurrentGraph(), Singleton.getInstance().currentNodeWithList.getKey(), Color.LIMEGREEN);
			
			Singleton.getInstance().logger.log("## Animazione terminata!");
			setAnimationIsRunning(false);
			
			return null;
		}); 
	}
	
	
	/**
	 * Imposta lo stato dei controlli nel menu per eseguire l'algoritmo
	 * @param animationIsRunning true se è in corso o sta per iniziare un'animazione, false altrimenti.
	 */
	private static void setAnimationIsRunning(final Boolean animationIsRunning) {
		
		// imposto lo stato dell'animazione
		Singleton.getInstance().isAnimating =
				Singleton.getInstance().graphLoaded =
				animationIsRunning;

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
    
    
    /**
     * Fa partire l'animazione se tutti i controlli hanno successo. Viene invocato sia nel caso l'animazione desiderata
     * sia automatice sia passo-passo, discriminando i due casi.
     * @param e evento scatenante.
     * @return Void.
     */
	public static Void run(Event e) {
    	
    	Singleton s = Singleton.getInstance();
 
    	// se il grafo non contiene nodi
    	if (s.getCurrentGraph().V().size() == 0) {
    		s.logger.log("Nessun grafo caricato.");
    		return null;
    	}
    	
    	Graph<CoordinateNode> currentGraph = s.getCurrentGraph();
		Node<CoordinateNode> root = s.animPrefs.getRoot();
		
		// se il nodo radice è null interrompo l'operazione
		if (root == null) {
			String errMsg = "Nodo sorgente non impostato. Avvio annullato.";
			s.logger.log(errMsg);

	    	return null;
		}
    	
    	// se esiste già un Thread con il nome predefinito (è già in corso un'animazione)
    	// allora lo interrompo prima di avviare questo
    	Thread existingThread = s.getThreadByName(AnimationSettings.THREAD_NAME);
    	if (existingThread != null) {
    		s.logger.log("## Animazione interrotta");
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
    	
    	Singleton.getInstance().logger.log("## Animazione avviata, nodo radice: " + root.toString());
    	
    	// imposto lo stato dei controlli
    	setAnimationIsRunning(true);
    	
    	// rilascio le risorse utilizzate
    	MainController.performCleanUp(null);
    	
    	// non c'è nessun nodo corrente
    	s.currentNodeWithList = null;
    	
    	// resetto il colore originale del grafo
    	resetGraphColor(s.mainViewController.graphPane, Color.BLACK);
		
		// creo l'oggetto GraphVisiter e setto il nome del thread 
		GraphVisiter bfs = new GraphVisiter(currentGraph, root);
		bfs.setName(AnimationSettings.THREAD_NAME);
    	
		// imposto le funzioni da richiamare durante l'esecuzione dell'algoritmo
    	completeAnimationSetup(bfs);
    	
    	// creo le celle e le inserisco nel VBox apposito
    	Object[] temp = new Boolean[s.getCurrentGraph().V().size()];
    	Arrays.fill(temp, false);
    	createCells(temp);
    	createCells(new Integer[temp.length]);
    	
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
    
    
    /**
     * Visualizza sull'interfaccia il vettore dei nodi visitati e padri.
     * @param array array dei nodi visitati o padri.
     * @return Void
     */
    public static Void showVisited(final Object[] array) {
    	Singleton s = Singleton.getInstance();
    	VBox vBox = null;
    	
    	// vedi metodo sottostante
    	if (array instanceof Boolean[]) {
    		vBox = s.mainViewController.vBoxVisited;
    	} else {
    		vBox = s.mainViewController.vBoxParents;
    	}
    	
    	HBox cell = null;

    	// itero su tutti gli elementi del VBox
    	for (Integer i = 0; i < array.length; i++) {
    		
    		// aggiorno il valore del secondo componente della cella
    		cell = (HBox) vBox.getChildren().get(i);
    		((Text) cell.getChildren().get(1)).setText(array[i] != null ? array[i].toString() : "null");
    	}
    	
    	return null;
    }
    
    
    /**
     * Crea le celle per visualizzare i due vettori e le inizializza.
     * @param array array che contiene le informazioni sui nodi visitati e padri.
     */
    private static void createCells(Object[] array) {
    	Singleton s = Singleton.getInstance();
    	ScrollPane sp = null;
    	VBox vBox = s.mainViewController.vBoxVisited;
    	
    	// ho passato il vettore visited
    	if (array instanceof Boolean[]) {
    		sp = s.mainViewController.scrollPaneVisited;
    		vBox = s.mainViewController.vBoxVisited;
    	} else {
    		// ho passato il vettore dei padri
    		sp = s.mainViewController.scrollPaneParents;
    		vBox = s.mainViewController.vBoxParents;
    	}
    	
    	vBox.getChildren().clear();
    	
    	final double width = sp.getWidth();
    	final double cellHeight = sp.getHeight() / 10;
    	
    	for (Integer i = 0; i < array.length; i++) {
        	
        	HBox cell = new HBox();
        	cell.getStyleClass().add("scrollPaneCell");
        	
        	// se quello che sto esaminando è il nodo sorgente allora lo evidenzio
        	if (s.animPrefs.getRoot().getElement().getIndex() == i) {
        		cell.getStyleClass().add("rootNodeCell");
        	}
    		
    		// incremento l'altezza del VBox
    		vBox.setPrefHeight(vBox.getHeight() + cellHeight);
    		
    		// creo la cella e la aggiungo al VBox
        	cell.setPrefSize(width, cellHeight);
        	cell.getChildren().add(new Text(i.toString()));
        	cell.getChildren().add(new Text(array[i] != null ? array[i].toString() : "null"));
        	cell.setAlignment(Pos.CENTER_LEFT);
        	
        	vBox.getChildren().add(cell);
    	}
    }
	
	/**
	 * Metodo che si preoccupa di rilasciare tutte le risorse allocate prima della chiusura dell'applicazione
	 * @param we istanza dell'evento sulla finestra.
	 */
	public static void performCleanUp(WindowEvent we) {
		
		// annullo il timer per l'esecuzione animata dell'algoritmo
		if (Singleton.getInstance().timer != null) {
			Singleton.getInstance().timer.cancel();
		}
		
		// termino il thread relativo all'esecuzione dell'algoritmo BFS
		Thread t = Singleton.getInstance().getThreadByName(AnimationSettings.THREAD_NAME);
		if (t != null) {
			t.interrupt();
		}
	}
}






























