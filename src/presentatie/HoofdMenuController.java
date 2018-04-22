package presentatie;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import logica.MemoryController;

public class HoofdMenuController {
	MemoryController mc;
	
	 @FXML
	 private Button readXML;
	 
	 @FXML 
	 private Button volgendeInstructie;
	
	 public HoofdMenuController() {
		 
	 }
	 
	 
	 public HoofdMenuController(MemoryController mc) {
		 this.mc=mc;
	 }
	
	@FXML
	public void setButtonLees(ActionEvent e) {
		// XML file inlezen
		MemoryController.instantiate();
		
			
	}
	
	@FXML
	public void setButtonVolgendeInstructieUit(ActionEvent e) {
		// volgende instructie
		MemoryController.voerVolgendeInstructieUit();
	}
	
	
	
}
