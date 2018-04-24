package entities;

import java.util.HashMap;
import java.util.Map;

public class Page {
	
	private int procesId;
	private Map<Integer, Integer> geheugenPlaatsen= new HashMap<>();
	private static final int grootte= 4096;
	
	
	public Page() {
		procesId=-1;
		for(int i=0; i< grootte; i++) {
			geheugenPlaatsen.put(i, -1);
		}
	}


	public int getProcessId() {
		return procesId;
	}
	
}
