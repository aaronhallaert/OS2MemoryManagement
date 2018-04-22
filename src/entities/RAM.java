package entities;

import java.util.ArrayList;

/**
 * reele adresruimte = 48 kbyte
 * 
 * 12 frames : nummers 0 tot en met 11
 * 
 * elk frame is 4096 bytes groot : nummers 0 tem 4095
 * 
 * ram voorstellen met byte adressen : bijna niet te doen
 * ram voortellen door 12 frames  in een lijst
 * bit voor opgevuld of niet
 * tijdstip voor LRU
 */
public class RAM {
	
	
	private ArrayList<Frame> ram;
	
	private int aantalProcessenAanwezig; // maximum 4
	
	
	public RAM() {
		
		ram = new ArrayList<Frame>();
		
		
		// ram opvullen met frames
		for(int i=0; i<12 ; i++) {
			
			//frame constructor, zet ingevuld op false al
			Frame f = new Frame();
			
			//framenummer instellen
			f.setFrameNummer(i);
			
			//frame toevoegen aan ram
			ram.add(f);
			
		}
		

		
	}
	
	
	
	
	
}
