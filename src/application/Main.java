package application;
	
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import logica.MemoryController;
import presentatie.HoofdMenuController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * start applicatie, set view
 * @author aaron
 *
 */
public class Main extends Application {
	public static Scene scene;
	
	private final static Logger LOGGER = Logger.getLogger(Main.class.getName());
	//alles temaken met de fxml
	@Override
	public void start(Stage primaryStage) {
		try {
			
			
			FXMLLoader loader = new FXMLLoader();
			
			BorderPane root= (BorderPane) loader.load(getClass().getResource("HoofdMenu.fxml"));
			root.setPrefHeight(1000);
			root.setPrefWidth(1000);
			scene = new Scene(root, 1000,1000);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		
		   
		    FileHandler fh;  

		    try {  

		      
		        fh = new FileHandler("logger.log");  
		        LOGGER.addHandler(fh);
		        SimpleFormatter formatter = new SimpleFormatter();  
		        fh.setFormatter(formatter);  
		        

		       

		    } catch (SecurityException e) {  
		        e.printStackTrace();  
		    } catch (IOException e) {  
		        e.printStackTrace();  
		    }  

		    
		
		
		//lanceert het inladen van de data
		launch(args);
		
		
		
		
	}
	
	private static Logger getLogger(){
	    if(LOGGER== null){
	        new Main();
	    }
	    return LOGGER;
	}
	public static void log(Level level, String msg){
		
	    getLogger().log(level, msg);
	   
	}
	
	public static Scene getScene() {
		return scene;
	}
}
