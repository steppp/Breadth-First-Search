package application;
	
import java.util.function.Function;

import javafx.application.Application;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.graphs.Graph;
import model.node.visual.CoordinateNode;
import singleton.Singleton;
import utility.AnimationSettings;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
			Scene scene = new Scene(root, 1280, 720);
			
			initData(primaryStage);
			// TODO: inserire l'icona dell'applicazione
			
			// rilascio tutte le risorse allocate prima della chiusura dell'applicazione
			primaryStage.setOnCloseRequest(Main::performOnClosingCleanUp);
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Inizializzazione dei dati del Singleton
	 */
	private void initData(Stage stage) {
		
		Singleton.getInstance().setCurrentGraph(new Graph<CoordinateNode>());
		Singleton.getInstance().animPrefs = new AnimationSettings();
		
		// gestore per gli eventi scatenati dalla pressione di un tasto della tastiera
		stage.addEventHandler(KeyEvent.KEY_PRESSED, (event) -> {
			
			if (event.getCode() == KeyCode.N) {
				if (event.isControlDown()) {
					Function<Event, Void> handler = MainController::nextStep;
					handler.apply(event);
				}
			}
		});
	}
	
	/**
	 * Metodo che si preoccupa di rilasciare tutte le risorse allocate prima della chiusura dell'applicazione
	 * @param we istanza dell'evento sulla finestra.
	 */
	private static void performOnClosingCleanUp(WindowEvent we) {
		
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