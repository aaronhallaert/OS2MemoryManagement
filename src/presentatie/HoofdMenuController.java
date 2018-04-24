package presentatie;



import java.io.Console;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import application.Main;
import entities.Proces;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import logica.MemoryController;

public class HoofdMenuController {
	MemoryController mc;
	List<Text> processFrames = new ArrayList<Text>();
	 @FXML
	 private Button readXML;
	 
	 @FXML 
	 private BorderPane borderPane;
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
	 
	 @FXML
	 private TextArea console;
	 private PrintStream log;
	 
	 @FXML
	 Accordion pageTables;
	 
	 //visualisatie van de klok
	 @FXML
	 Text klok;
	
	 public HoofdMenuController() {
		
		 
	 }
	 
	 public void initialize() {
	        log = new PrintStream(new Console(console)) ;
	  }
	 
	 
	
	@FXML
	public void setButtonLees(ActionEvent e) {
		// XML file inlezen
		MemoryController.instantiate();
		setFrames();
		for(int i=0; i<MemoryController.processen.size();i++)
		{
			
			AnchorPane newPanelContent = new AnchorPane();
			Label x= new Label("---");
			x.setId("PTproces"+i);
	        newPanelContent.getChildren().add(x);
			TitledPane tp= new TitledPane("", newPanelContent);
			tp.setText("proces"+i);
			pageTables.getPanes().add(tp);
			
			
			
		}
			
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
		klok.setText(Integer.toString(MemoryController.klok));
		for(int i=0; i<MemoryController.processen.size();i++) {
			MemoryController.processen.get(i).printPageTable();
		}
	}
	
	
	private void updateVisualisatieRam() {
		for(int i=0; i<12;i++) {//i stelt framenummer voor
		
			String visualisatie;
			//als een proces in de frame zit, gaan we in de if
			if (MemoryController.ram.getFrame(i).isBevatPage()){
				
				//neemt de id van de
				int pID = MemoryController.ram.getFrame(i).getAanwezigProces();
				Proces p = MemoryController.processen.get(pID);
				int pageNummer = p.getPageIdByFrameNummer(i);
				visualisatie = "proces " + pID+", page "+pageNummer;
			}
			else {
				visualisatie ="--";
			}
			
			processFrames.get(i).setText(visualisatie);
		}
		
		
	}
	
	
	public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }

	
}
