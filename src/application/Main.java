package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import logica.MemoryController;
import presentatie.HoofdMenu;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			HoofdMenu hoofdMenu=new HoofdMenu();
		
			MemoryController mc= new MemoryController();
			hoofdMenu.setButtonLees(mc);
			
			
			
			
			
			
			
			
			hoofdMenu.getScene().getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(hoofdMenu.getScene());
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
