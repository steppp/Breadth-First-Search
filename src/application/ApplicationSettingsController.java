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
		speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
			Integer interval = 5100 - (5000 * newVal.intValue() / 100);
			Singleton.getInstance().logger.log(interval.toString());
			Singleton.getInstance().animPrefs.setInterval(interval);
		});

		speedSlider.setMajorTickUnit(0.05);
		speedSlider.setValue(0.5);
		
		
		// imposto le opzioni del ChoiceBox
		Set<Node<CoordinateNode>> s = Singleton.getInstance().getCurrentGraph().V();
		
		// se non c'è alcun nodo nel grafo non eseguo operazioni
		if (s.size() != 0) {
			
			// carico gli elementi del ChoiceBox
			ObservableList<Integer> items = (ObservableList<Integer>)
					FXCollections.observableArrayList(this.getIndexListFromNodeSet(s));
			
			rootChoiceBox.setItems(items);
			
			// se root non è null imposto il suo valore come valore iniziale del ChoiceBox
			Node<CoordinateNode> root = Singleton.getInstance().animPrefs.getRoot();
			if (root != null) {
				rootChoiceBox.setValue(root.getElement().getIndex());
			} else {
				// altrimenti imposto come valore iniziale il primo elemento della collezione
				rootChoiceBox.setValue(items.get(0));
			}
			
			ApplicationSettingsController.setRoot(rootChoiceBox.getValue());
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
