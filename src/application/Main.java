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
import javafx.scene.image.Image;
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
			primaryStage.setOnCloseRequest(MainController::performCleanUp);
			
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
		
		// imposto l'icona dell'applicazione
		// non funziona ancora madonna postina
		stage.getIcons().add(new Image("file:resources/images/logo.png"));
		
		// gestore per gli eventi scatenati dalla pressione di un tasto della tastiera
		stage.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			
			if (!event.isControlDown())
				return;
			
			Function<Event, Void> handler = null;
						
			switch(event.getCode()) {
			case N:
				handler = MainController::nextStep;
				handler.apply(event);
				break;
				
			// sia nel caso S che nel caso R chiamo il metodo run
			// sarà poi compito di quel metodo di discriminare i due casi
			case S:
			case R:
				handler = MainController::run;
				handler.apply(event);
				break;
				
			case P:
				handler = MainController::showPrefWindow;
				handler.apply(event);
				
			case X:
				handler = MainController::stop;
				handler.apply(event);
				break;
				
			default:
				break;
			}
		});
	}
}