package entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
	
	
	private Map<Integer, Frame> frames;
	
	//toestandsvariabelen van het ram (om eenvoudig te beredeneren)
	private int aantalProcessenAanwezig; // maximum 4
	
	//welke processen aanwezig? de ids bijhouden?
	
	public RAM() {
		
		frames = new HashMap<Integer, Frame>();
		aantalProcessenAanwezig = 0;
		
		// ram opvullen met frames
		for(int i=0; i<12 ; i++) {
			
			//frame constructor, zet ingevuld op false al
			Frame f = new Frame();
			
			//frame toevoegen aan ram
			frames.put(i, f);
			
		}	
	}


	public int getAantalProcessenAanwezig() {
		return aantalProcessenAanwezig;
	}


	public void setAantalProcessenAanwezig(int aantalProcessenAanwezig) {
		this.aantalProcessenAanwezig = aantalProcessenAanwezig;
	}


	public Frame getFrame(int i) {
		return frames.get(i);
	}
	
	
	
	/**
	 * laad geheugen page p in frame met framenummer
	 * @param p
	 * @param framenummer
	 */
	public void laadPageIn(Page p, int framenummer) {
		frames.get(framenummer).copyPage(p);
	}
	
	
	
	
}
