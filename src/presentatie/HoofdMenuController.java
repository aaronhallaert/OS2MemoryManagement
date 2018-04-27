package presentatie;



import java.io.BufferedReader;
import java.io.Console;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import com.sun.media.jfxmedia.logging.Logger;

import application.Main;
import entities.Instructie;
import entities.Proces;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
	 public Label huidigeInstructie;
	 
	 
	 @FXML 
	 private BorderPane borderPane;
	 @FXML 
	 private Button volgendeInstructie;
	 
	 @FXML
	 private Button alleInstructiesEx;
	 
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
	 
	 @FXML 
	 private ComboBox instructieKeuze;
	 
	 @FXML
	 Accordion pageTables;
	 
	 //visualisatie van de klok
	 @FXML
	 Text klok;
	 @FXML
	 Text naarRam;
	 @FXML
	 Text naarVM;
	 @FXML
	 Text totaalWrites;
	
	
	 
	 public HoofdMenuController() {
		
		
	 }
	 
	 
	
	 public void initialize() {
		 instructieKeuze.getItems().addAll("30 i, 3 p","20000 i, 4 p","20000 i, 20 p");
	 }
	 List<Instructie> instructies;
	@FXML
	public void setButtonLees(ActionEvent e) {
		huidigeInstructie.setText("huidige instructie komt hier");
       
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
		instructies= new ArrayList<Instructie>(MemoryController.instructies);
		
		readXML.setDisable(true);
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
		if(instructies.size()!=0) {
			int pid= instructies.get(0).getPid();
			pageTables.setExpandedPane((TitledPane)pageTables.getChildrenUnmodifiable().get(pid));
			updateVisualisatieRam();
			updateWrites();
			klok.setText(Integer.toString(MemoryController.klok));
			for(int i=0; i<MemoryController.processen.size();i++) {
				MemoryController.processen.get(i).printPageTable();
				
			}
			
			instructies.remove(0);
		}
		
		// update logger
		updateLogTextArea();
		
		console.setScrollTop(Double.MAX_VALUE);
	}
	
	
	@FXML
	public void setButtonAlleInstructies(ActionEvent e) {
		
		while(instructies.size()!=0 ) {
		// volgende instructie
		MemoryController.voerVolgendeInstructieUit();
		
		
		
		// update logger
		//updateLogTextArea();
		instructies.remove(0);
		
		}
		updateLogTextArea();
		updateVisualisatieRam();
		console.setScrollTop(Double.MAX_VALUE);
		klok.setText(Integer.toString(MemoryController.klok));
		for(int j=0; j<MemoryController.processen.size();j++) {
			MemoryController.processen.get(j).printPageTable();
		}
		
		
		System.out.println("aantal keer naar VM "+ MemoryController.aantalKeerNaarVM );
		System.out.println("aantal keer naar Mem "+ MemoryController.aantalKeerNaarMem );
		updateWrites();
	}

	private void updateWrites() {
		 naarRam.setText(Integer.toString(MemoryController.aantalKeerNaarMem));
		 naarVM.setText(Integer.toString(MemoryController.aantalKeerNaarVM));
		 int alleWritesCount=MemoryController.aantalKeerNaarMem+MemoryController.aantalKeerNaarVM;
		 totaalWrites.setText(Integer.toString(alleWritesCount));
	}
	
	
	private void updateLogTextArea() {
		try
        {
            FileReader reader = new FileReader("logger.log");
            //FileReader reader = new FileReader("C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\logger.log");
            BufferedReader in = new BufferedReader(reader);
            String line = in.readLine();
            
            StringBuilder sb= new StringBuilder();
            while(line != null)
            {
            	if(!line.contains("application.Main")) {
              sb.append(line+"\n");
            	}
              line = in.readLine();
            }
            in.close();
            
            console.setText(sb.toString());
            // scrol naar beneden
            console.setScrollTop(Double.MAX_VALUE);
        }
        catch(Exception e) { 
        	e.printStackTrace();
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
	
	



	
	
	
	
	
	
}
