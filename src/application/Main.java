package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import model.graphs.Graph;
import model.node.visual.CoordinateNode;
import singleton.Singleton;
import utility.AnimationSettings;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("mainView.fxml"));
			Scene scene = new Scene(root, 1280, 720);
			
			initData();
			// TODO: inserire l'icona dell'applicazione
			
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
	
	
	private void initData() {
		
		Singleton.getInstance().setCurrentGraph(new Graph<CoordinateNode>());
		Singleton.getInstance().animPrefs = new AnimationSettings();
	}
}