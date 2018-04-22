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
			MemoryController.inlezenInstructies();
			MemoryController.voerVolgendeInstructieUit();
	}
	
	
	
}
