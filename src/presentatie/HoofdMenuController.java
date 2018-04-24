package presentatie;


import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import logica.MemoryController;

public class HoofdMenuController {
	MemoryController mc;
	
	 @FXML
	 private Button readXML;
	 
	 @FXML 
	 private Button volgendeInstructie;
	 
	 @FXML 
	 private Scene root;
	
	 public HoofdMenuController() {
		 root= Main.scene;
		 setProcesInFrame(1, 0);
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
	
	
	public void setProcesInFrame(int proces, int frame) {
		StringBuilder sb= new StringBuilder();
		sb.append("#procesFrame").append(Integer.toString(frame));
		System.out.println(sb.toString());
		Text textview= (Text) root.lookup(sb.toString());
		textview.setText(Integer.toString(proces));
	}
	
	
}
