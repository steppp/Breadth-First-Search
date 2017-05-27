package application;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import model.graphs.Graph;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import singleton.Singleton;
import model.arrow.Arrow;

public class MainController implements Initializable {
	
	static CoordinateNode currentNode = null;
	static boolean mouseOverNode = false;
	
	@FXML
	private Pane graphPane;
	
	@FXML
	private TextArea outputTextArea;
	
	@FXML
	private TextField pseudocodeTextArea;

    @FXML
    private Text coordinateLabel;
	
	
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
		System.out.println(file.getAbsolutePath());
	}
	
	
	private void configureFileChooser(final FileChooser fc) {
		fc.setTitle("Open graph from json file");
		fc.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("All files", "*.*"),
				new FileChooser.ExtensionFilter("JSON", "*.json"));
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		graphPane.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
	}
	
    @FXML
    private void handleMenuItem_Delete(ActionEvent event) {
    	
    	Singleton.getInstance().setCurrentGraph(new Graph<CoordinateNode>());
    	graphPane.getChildren().clear();
    	
    	outputTextArea.appendText("Graph cleared");
    }
    
    
    @FXML
    private void handleMenuItem_Debug(ActionEvent event) {
    	
    	Graph<CoordinateNode> g = Singleton.getInstance().getCurrentGraph();
    	
    	if (g != null) {
	    	outputTextArea.appendText("\n-----------------------------\nHere is the graph:\n\n");
	    	outputTextArea.appendText(g.toString());
	    	outputTextArea.appendText("-----------------------------\n");
    	} else {
    		outputTextArea.appendText("No graph loaded!\n");
    	}
    }
    
    
    @FXML
    private void handleMouseMove_GraphPane(MouseEvent event) {
    	coordinateLabel.setText(event.getX() + ", " + event.getY());
    }
}






























