package presentatie;

import application.Main;
import io.DataProcessing;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import logica.MemoryController;

public class HoofdMenu {
	Scene hoofdmenu;
	Button button;
	VBox main;
	
	public HoofdMenu() {	
		
		
		// layout
		main= new VBox();
		
		// children
		button= new Button("Lees instructies uit xml file");
		
		
		
		// set children
		main.getChildren().add(button);
		
		// make scene
		hoofdmenu=new Scene(main, 500,500);
		
	}

	public void setButtonLees(MemoryController mc) {
		button.addEventHandler(ActionEvent.ACTION, (e) -> {
			mc.instantiate();
		});
		
	}
	
	//getters en setters
	public Button getButton() {
		return button;
	}
	public void setButton(Button button) {
		this.button = button;
	}

	public VBox getMain() {
		return main;
	}
	public void setMain(VBox main) {
		this.main = main;
	}
	
	public Scene getScene() {
		return hoofdmenu;
	}
	
	
}
