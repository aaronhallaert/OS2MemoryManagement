package entities;
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
	
	
	//frame wordt leeg aangemaakt
	public Frame() {
		bevatPage = false;
		huidigePage = null;
	}
	
	public void setFrameNummer(int nummer){
		this.framenummer=nummer;
	}
	
	
}
