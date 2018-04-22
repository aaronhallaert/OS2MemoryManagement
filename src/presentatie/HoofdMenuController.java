package presentatie;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import logica.MemoryController;

public class HoofdMenuController {
	MemoryController mc;
	
	 @FXML
	 private Button readXML;
	
	 public HoofdMenuController() {
		 
	 }
	 
	 
	 public HoofdMenuController(MemoryController mc) {
		 this.mc=mc;
	 }
	
	@FXML
	public void setButtonLees(ActionEvent e) {
			MemoryController.voerVolgendeInstructieUit();
			//als we hier de methode om de volgende instructie uit te
			//zou het telkens de 1e instructie zijn die wordt uitgevoerd
			
	}
	
	
	
}
