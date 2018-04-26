package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import application.Main;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import logica.MemoryController;
import presentatie.HoofdMenuController;

public class Instructie {
	
	private int pid;
	private String operatie;
	private int virtueelAdres;
	
	// constructoren
	public Instructie() {
		pid=0;
		operatie=null;
		virtueelAdres=0;
	}
	
	public Instructie(int pid, String operatie, int virtueelAdres) {
		super();
		this.pid = pid;
		this.operatie = operatie;
		this.virtueelAdres = virtueelAdres;
	}

	
	
	// getters en setters
	public int getPid() {
		return pid;
	}
	public String getOperatie() {
		return operatie;
	}	
	public int getVirtueelAdres() {
		return virtueelAdres;
	}

	@Override
	public String toString() {
		return "Instructie [pid=" + pid + ", operatie=" + operatie + ", virtueelAdres=" + virtueelAdres + "]";
	}
	
	public void execute() {
		Main.log(Level.INFO, "execute: "+ this.toString());
		
		
		
		Label x= (Label) Main.getScene().lookup("#huidigeInstructie");
		x.setText("execute: "+this.toString());
		
		
		Label reeelAdresLabel= (Label) Main.getScene().lookup("#reeelAdres");
		Label virtueelAdresLabel= (Label) Main.getScene().lookup("#virtueelAdres");
		
		//HoofdMenuController.getHuidigeInstructieLabel().setText("execute: "+ this.toString());
		// belangrijk bij opstarten proces
		int aantalProcessenInRam;
		
		
		// eigenschappen van instructie
		String operatie=this.getOperatie();
		int pid= this.getPid();
		int virtueelAdres= this.getVirtueelAdres();
		
		
		// algemene variabelen
		RAM ram = MemoryController.ram;
		int klok = MemoryController.klok;
		Map<Integer, Proces> processen = MemoryController.processen;
		
		
		Proces huidigProces= processen.get(pid);
		List<Integer> adres=null;
		int pagenummer=0;
		int offset=0;
		int framenummer=0;
		Frame f=null;
		PTEntry pte=null;
		
		//effectieve uitvoeren van de instructie
		switch(operatie) {
		case "Read" :	System.out.println("read");
		
		
						if(!MemoryController.ram.getAanwezigeProcessen().contains(huidigProces)) {
							Main.log(Level.SEVERE, "Read en proces zit niet in ram");
							MemoryController.swapLRUProces(huidigProces);
						}
						else {
						MemoryController.readMethod(adres, pagenummer, offset, huidigProces, pid, virtueelAdres, virtueelAdresLabel, pte, framenummer, f, virtueelAdresLabel);
						}

						break;
			
		case "Write":	System.out.println("write");
						
						if(!MemoryController.ram.getAanwezigeProcessen().contains(huidigProces)) {
							Main.log(Level.SEVERE, "Write en proces zit niet in ram");
							MemoryController.swapLRUProces(huidigProces);
						}
						else {
						MemoryController.writeMethod(adres, virtueelAdres, pagenummer, offset, virtueelAdresLabel, huidigProces,
								pte,  framenummer, reeelAdresLabel, f);
						}
						
						
						break;
			
		case "Terminate":System.out.println("terminate");
		
						reeelAdresLabel.setText("");	
						virtueelAdresLabel.setText("");	
						if(!MemoryController.ram.getAanwezigeProcessen().contains(huidigProces)) {
							Main.log(Level.SEVERE, "Terminate en proces zit niet in ram");
							MemoryController.swapLRUProces(huidigProces);
						}
						else {
		
						MemoryController.terminateProces(huidigProces);
						}
						break;
			
		case "Start":	System.out.println("start");
						reeelAdresLabel.setText("");	
						virtueelAdresLabel.setText("");	
						
						ram.getAanwezigeProcessen().add(huidigProces);
						
						aantalProcessenInRam = MemoryController.ram.getAantalProcessenAanwezig();

						// er is reeds een proces aanwezig
						if(aantalProcessenInRam == 1) {
							MemoryController.startProces(1, 6, huidigProces);
						}
						else if (aantalProcessenInRam == 2) {
							MemoryController.startProces(2, 2, huidigProces);
						}
						else if (aantalProcessenInRam ==3) {
							MemoryController.startProces(3, 1, huidigProces);
						}
						else if (aantalProcessenInRam ==4) {
							Main.log(Level.SEVERE, "5de proces wil starten, al reeds 4 processen aanwezig");
							MemoryController.swapLRUProces(huidigProces);
						}
						else if (aantalProcessenInRam ==0) {
							
							MemoryController.startProcesLeegRam(huidigProces);
							
									
						}
						break;
			
						default :
						Main.log(Level.SEVERE, "execute: "+ this.toString()+ " bevat geen operatie");
						break;
	}
	
	
	}

	

	
	
	

	


	
	
}

