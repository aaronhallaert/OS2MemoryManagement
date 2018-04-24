package entities;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author tibo
 *
 *	frame is 4096 bytes groot
 *	van 0 tem byte 4095
 *
 *	
 *
 */
public class Frame {

	private boolean bevatPage;
	private Page huidigePage;
	int framenummer;
	private static final int grootte = 4096; //niet zeker als dit gaat nodig zijn
	
	// voorbeeld
	// virtueel adres: 0001 000000000001
	// page 1
	// offset 1
	
	// PTEntry 1: 0010 000000000001
	// => in ram frame nummer 2
	// => offset is 1
	
	
	// Integer is plaats in frame (offset in adres)
	//1e integer is het zoveelste geheugenplaats van het frame
	//2e integer is de waarde van deze geheugenplaats
	private Map<Integer, Integer> geheugenPlaatsen= new HashMap<>();
	
	//frame wordt aangemaakt
	public Frame() {
		bevatPage = false;
		huidigePage = null;
		//initialiseren van het geheugen als lege plaatsen
		for(int i=0; i<grootte; i++) {
			geheugenPlaatsen.put(i, -1);
		}
	}
	
	public void setFrameNummer(int nummer){
		this.framenummer=nummer;
	}

	public String getAanwezigProces() {
		if (bevatPage) {
			return Integer.toString(huidigePage.getProcessId());
		}
		else {
			return "//";
		}
	}

	public void setHuidigePage() {
		
		
	}
	
	/**
	 * inladen van een page van virtueel memory naar RAM
	 * @param p
	 */
	public void copyPage(Page p) {
		System.out.println("kopieren page");
		this.geheugenPlaatsen= new HashMap<>(p.getGeheugenPlaatsen());
	}
	
	
}
