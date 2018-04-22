package entities;

import java.util.HashMap;
import java.util.Map;

public class VirtueelGeheugen {

	//adressen 0 tot en met 65536 = 2^16 bytes
	//opdelen in pages of niet?
	
	private Map<Integer, Page> pages = new HashMap<>();
	private static final int aantalPages = 16;
	
	public VirtueelGeheugen() {
		for(int i=0; i<aantalPages; i++) {
			Page page= new Page();
			pages.put(i, page);
		}
	}
	
	//we kozen om geen frames te maken in het vm
	// als we een frame van het virtueel geheugen terug naar het ram moeten laden, kunnen we
	
}
