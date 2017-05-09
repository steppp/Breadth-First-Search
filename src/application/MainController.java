package application;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.FileChooser;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import singleton.Singleton;

public class MainController {
	
	static CoordinateNode currentNode = null;
	static CoordinateNode sourceNode = null;
	
	@FXML
	private Pane graphPane;
	
	@FXML
	private TextField outputTextArea;
	
	@FXML
	private TextField pseudocodeTextArea;
	
	
	@FXML
	private void handleButtonClick_GraphPane(MouseEvent e) {
		
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
		
		sp.setOnMouseEntered((MouseEvent e) -> {
			
			// TODO: Risolvere il bug
			// -------- BUG ----------
			// non viene rilevato l'evento se è già in corso l'evento "mousePressed" del pannello
			
			MainController.currentNode = new CoordinateNode(index, xPos, yPos);
			System.out.println("Entering node " + index);
			System.out.println(currentNode);
		});
		
		sp.setOnMouseExited((MouseEvent e) -> {
			MainController.currentNode = null;
			System.out.println("Exiting node " + index);
			System.out.println(currentNode);
		});
		
		this.graphPane.getChildren().add(sp);
	}
	
	
	private Circle createCircle() {
		
		Singleton s = Singleton.getInstance();
		
		Circle c = new Circle();
		c.setFill(Color.TRANSPARENT);
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
	
	
    @FXML
    void handleMousePressed_GraphPane(MouseEvent event) {
    	System.out.println(MainController.currentNode);
    	if (MainController.currentNode != null) {
    		// iniziare a disegnare la linea
    		
    		MainController.sourceNode = MainController.currentNode;
			System.out.println("Source node " + sourceNode.getIndex());
    	}
    }

    @FXML
    void handleMouseReleased_GraphPane(MouseEvent event) {
    	System.out.println(MainController.currentNode);
    	if (MainController.currentNode != null) {
    		
    		CoordinateNode target = MainController.currentNode, source = MainController.sourceNode;
    		
			System.out.println("Target node " + target.getIndex());
			
    		Double radius = Singleton.getInstance().NODE_RADIUS;

    		Line edge = new Line(source.getxPos() + radius, source.getyPos() + radius, target.getxPos() + radius, target.getyPos() + radius);
    		edge.setStroke(Color.BLACK);
    		edge.setStrokeWidth(3);
    		
    		graphPane.getChildren().add(edge);
    		
    		MainController.sourceNode = null;
    	}
    }
}






























