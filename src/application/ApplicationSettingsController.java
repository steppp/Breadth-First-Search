package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import model.graphs.Node;
import model.node.visual.CoordinateNode;
import singleton.Singleton;

public class ApplicationSettingsController implements Initializable {
	
    @FXML
    private VBox mainWindow;
    
    @FXML
    private ChoiceBox<Integer> rootChoiceBox;

    @FXML
    private Slider speedSlider;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		// imposto i valori dello slider
		speedSlider.setMax(1.0);
		speedSlider.setMin(0.0);
		speedSlider.setMajorTickUnit(0.05);
		speedSlider.setValue(0.5);
		
		
		// imposto le opzioni del ChoiceBox
		Set<Node<CoordinateNode>> s = Singleton.getInstance().getCurrentGraph().V();
		
		// se non c'è alcun nodo nel grafo non eseguo operazioni
		if (s.size() != 0) {
			ObservableList<Integer> items = (ObservableList<Integer>)
					FXCollections.observableArrayList(this.getIndexListFromNodeSet(s));
			
			rootChoiceBox.setItems(items);
			rootChoiceBox.setValue(items.get(0));
			
			ApplicationSettingsController.setRoot(items.get(0));
		}
			
		// imposto il listener per l'evento del ChoiceBox se l'elemento selezionate cambia
		rootChoiceBox.getSelectionModel().selectedIndexProperty()
			.addListener(new ChangeListener<Number>() {

				@Override
				public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
					// http://www.baeldung.com/java-8-double-colon-operator
					// imposto il nodo radice da cui partirà l'algoritmo
					Function<Integer, Void> setRoot = ApplicationSettingsController::setRoot;
					setRoot.apply(newValue.intValue());
				}
				
			});
	}
	
	
	@SuppressWarnings("serial")
	private List<Integer> getIndexListFromNodeSet(Set<Node<CoordinateNode>> s) {
		
		// https://stackoverflow.com/questions/8892360/convert-set-to-list-without-creating-new-list
		// https://stackoverflow.com/questions/1958636/what-is-double-brace-initialization-in-java
		ArrayList<Node<CoordinateNode>> vertexes = new ArrayList<Node<CoordinateNode>>() {{
			// aggiungo alla lista tutti gli elementi del Set di Node<CoordinateNode> s
			addAll(s);
		}} ;
				
		// effettuo una trasformazione tramite la funzione map per ritornare una lista di interi
		// http://www.java67.com/2015/01/java-8-map-function-examples.html
		ArrayList<Integer> list = (ArrayList<Integer>) (vertexes.stream().map((node) -> {
			return node.getElement().getIndex();
		}).collect(Collectors.toList()));
		
		Collections.sort(list);
		
		return list;
	}
	
	
	/**
	 * Imposta il nodo radice da cui partirà l'algoritmo
	 * @param index indice del nodo da cui partire.
	 * @return void.
	 */
	private static Void setRoot(Integer index) {
		Node<CoordinateNode> root = Singleton.getInstance().getCurrentGraph().getNodeWithValue(new CoordinateNode(index, 0, 0));
		Singleton.getInstance().animPrefs.setRoot(root);
		
		return null;
	}

}
