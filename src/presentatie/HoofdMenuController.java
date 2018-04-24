package presentatie;


import java.util.ArrayList;
import java.util.List;

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
	List<Text> processFrames = new ArrayList<Text>();
	 @FXML
	 private Button readXML;
	 
	 @FXML 
	 private Button volgendeInstructie;
	 
	 //visualisatie van het ram
	 @FXML
	 Text procesFrame0;
	 @FXML
	 Text procesFrame1;
	 @FXML
	 Text procesFrame2;
	 @FXML
	 Text procesFrame3;
	 @FXML
	 Text procesFrame4;
	 @FXML
	 Text procesFrame5;
	 @FXML
	 Text procesFrame6;
	 @FXML
	 Text procesFrame7;
	 @FXML
	 Text procesFrame8;
	 @FXML
	 Text procesFrame9;
	 @FXML
	 Text procesFrame10;
	 @FXML
	 Text procesFrame11;
	
	 public HoofdMenuController() {
		 
		 
	 }
	 
	 
	 
	
	@FXML
	public void setButtonLees(ActionEvent e) {
		// XML file inlezen
		MemoryController.instantiate();
		setFrames();
		
		
			
	}
	
	public void setFrames() {
		processFrames.add(procesFrame0);
		 processFrames.add(procesFrame1);
		 processFrames.add(procesFrame2);
		 processFrames.add(procesFrame3);
		 processFrames.add(procesFrame4);
		 processFrames.add(procesFrame5);
		 processFrames.add(procesFrame6);
		 processFrames.add(procesFrame7);
		 processFrames.add(procesFrame8);
		 processFrames.add(procesFrame9);
		 processFrames.add(procesFrame10);
		 processFrames.add(procesFrame11);
	}
	
	@FXML
	public void setButtonVolgendeInstructieUit(ActionEvent e) {
		// volgende instructie
		MemoryController.voerVolgendeInstructieUit();
		updateVisualisatieRam();
	}
	
	
	private void updateVisualisatieRam() {
		for(int i=0; i<12;i++) {
			processFrames.get(i).setText(MemoryController.ram.getFrame(i).getAanwezigProces());
			
		}
		
		
	}
	
}
