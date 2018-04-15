package entities;

import java.util.List;

public class Proces {

	private List<PTEntry> pageTable;
	private int pid;
	
	// constructoren
	public Proces() {
		
	}
	public Proces(List<PTEntry> pageTable, int pid) {
		super();
		this.pageTable = pageTable;
		this.pid = pid;
	}
	
	

	// getters en setters
	public List<PTEntry> getPageTable() {
		return pageTable;
	}
	public void setPageTable(List<PTEntry> pageTable) {
		this.pageTable = pageTable;
	}
	public int getPid() {
		return pid;
	}
	public void setPid(int pid) {
		this.pid = pid;
	}
	
	
}
