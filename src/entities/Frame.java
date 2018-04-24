package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import com.sun.media.jfxmedia.logging.Logger;

import application.Main;
import logica.MemoryController;

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

	// ingevuld of niet
	private boolean bevatPage;
	
	// procesnummer waartoe page behoort
	int procesnummer;
	
	// framenummer
	int framenummer;
	
	// grootte van frame
	private static final int grootte = 4096; 
	
	// Integer is plaats in frame (offset in adres)
	//1e integer is het zoveelste geheugenplaats van het frame
	//2e integer is de waarde van deze geheugenplaats
	private Map<Integer, Integer> geheugenPlaatsen= new HashMap<>();
	
	
	
	
	/**
	 * constructor frame
	 */
	public Frame() {
		
		bevatPage = false;
		procesnummer=-1;
		
		//initialiseren van het geheugen als lege plaatsen
		for(int i=0; i<grootte; i++) {
			geheugenPlaatsen.put(i, -1);
		}
	}
	
	
	
	public void setFrameNummer(int nummer){
		this.framenummer=nummer;
	}
	
	
	public Map<Integer, Integer> getGeheugenPlaatsen(){
		return geheugenPlaatsen;
	}

	
	
	
	public int getAanwezigProces() {
		return procesnummer;
	}

	
	
	
	
	public boolean isBevatPage() {
		return bevatPage;
	}



	public void setBevatPage(boolean bevatPage) {
		this.bevatPage = bevatPage;
	}



	/**
	 * inladen van een page van virtueel memory naar RAM
	 * @param p
	 */
	public void copyPage(Page p) {
		Main.log(Level.INFO, "Page " + p.getPageNummer() +" van proces "+p.getProcessId()+" wordt gekopieerd naar frame "+this.framenummer);
		this.geheugenPlaatsen= new HashMap<>(p.getGeheugenPlaatsen());
		this.procesnummer=p.getProcessId();
		bevatPage=true;
	}



	public void schrijf(int offset, int i) {
		//geheugenplek schrijven
		geheugenPlaatsen.put(offset, i);
	}



	public int lees(int offset) {
		return geheugenPlaatsen.get(offset);
	}
	
	
}
