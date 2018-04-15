package entities;

public class RAMEntry {
	private int frameNr;
	private int pid;
	private int pageNr;
	
	
	// constructoren
	public RAMEntry() {
		
	}

	public RAMEntry(int frameNr, int pid, int pageNr) {
		super();
		this.frameNr = frameNr;
		this.pid = pid;
		this.pageNr = pageNr;
	}

	

	// getters en setters
	public int getFrameNr() {
		return frameNr;
	}
	public void setFrameNr(int frameNr) {
		this.frameNr = frameNr;
	}

	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getPageNr() {
		return pageNr;
	}
	public void setPageNr(int pageNr) {
		this.pageNr = pageNr;
	}
	
	
	
}
