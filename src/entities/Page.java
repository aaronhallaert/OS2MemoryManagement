package entities;

import java.util.HashMap;
import java.util.Map;

public class Page {
	
	private int procesId;
	private Map<Integer, Integer> geheugenPlaatsen= new HashMap<>();
	private static final int grootte= 4096;
	private int pagenummer;
	
	
	public Page() {
		procesId=-1;
		for(int i=0; i< grootte; i++) {
			geheugenPlaatsen.put(i, -1);
		}
		
	}
	
	public Page(int procesNummer) {
		procesId=procesNummer;
		for(int i=0; i< grootte; i++) {
			geheugenPlaatsen.put(i, -1);
		}
		
	}
	
	public void setPageNummer(int pagenummer) {
		this.pagenummer=pagenummer;
	}
	
	public int getPageNummer() {
		return pagenummer;
	}


	public int getProcessId() {
		return procesId;
	}
	
	


	public Map<Integer, Integer> getGeheugenPlaatsen() {
		return geheugenPlaatsen;
	}


	public void setGeheugenPlaatsen(Map<Integer, Integer> geheugenPlaatsen) {
		this.geheugenPlaatsen = geheugenPlaatsen;
	}
	
	
	
}
