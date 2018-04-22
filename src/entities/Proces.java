package entities;

import java.util.ArrayList;
import java.util.List;

public class Proces {

	private List<PTEntry> pageTable;
	private int pid;
	
	// constructoren
	
	/**
	 * de pagetable kan maximaal 16 entries bevatten
	 * pid ? nog toe te voegen
	 */
	public Proces() {
		pageTable = new ArrayList<PTEntry>(16);
	}
	
	
	public Proces(int pid) {
		pageTable = new ArrayList<PTEntry>(16);
		this.pid=pid;
	}
	
	
	
	
	// gebruiken we niet
	public Proces(List<PTEntry> pageTable, int pid) {
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
