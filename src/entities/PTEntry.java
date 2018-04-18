package entities;
public class PTEntry {
	
	
    private int pageNr;
    private boolean present;
    private boolean modified;
    private int laatsteKeerGebruikt;
    private int frameNr;
	
	
	
    
    // constructoren
	public PTEntry() {
		
	}

	public PTEntry(int pageNr, boolean present, boolean modified, int laatsteKeerGebruikt, int frameNr) {
		super();
		this.pageNr = pageNr;
		this.present = present;
		this.modified = modified;
		this.laatsteKeerGebruikt = laatsteKeerGebruikt;
		this.frameNr = frameNr;
	}



	// getters en setters
	public int getPageNr() {
		return pageNr;
	}
	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}

	public boolean isPresent() {
		return present;
	}
	public void setPresent(boolean present) {
		this.present = present;
	}
	
	public boolean isModified() {
		return modified;
	}
	public void setModified(boolean modified) {
		this.modified = modified;
	}
	
	public int getLaatsteKeerGebruikt() {
		return laatsteKeerGebruikt;
	}
	public void setLaatsteKeerGebruikt(int laatsteKeerGebruikt) {
		this.laatsteKeerGebruikt = laatsteKeerGebruikt;
	}

	public int getFrameNr() {
		return frameNr;
	}
	public void setFrameNr(int frameNr) {
		this.frameNr = frameNr;
	}
	
	
}
