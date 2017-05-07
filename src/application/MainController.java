package application;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import singleton.Singleton;

public class MainController {
	
	@FXML
	private Pane graphPane;
	
	@FXML
	private TextField outputTextArea;
	
	@FXML
	private TextField pseudocodeTextArea;
	
	
	@FXML
	private void handleButtonClick_GraphPane(MouseEvent e) {
		Singleton s = Singleton.getInstance();
		
		if (!s.graphLoaded) {
			
		}
	}
	
	
	private void drawCircle(Integer index, int xPos, int yPos) {
		
	}
}
