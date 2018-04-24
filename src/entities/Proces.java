package entities;

import java.util.ArrayList;
import java.util.List;

public class Proces {

	private VirtueelGeheugen vm;
	private List<PTEntry> pageTable;
	private int pid;
	
	// constructoren
	
	/**
	 * de pagetable kan maximaal 16 entries bevatten
	 * pid ? nog toe te voegen
	 */
	public Proces() {
		pageTable = new ArrayList<PTEntry>(16);
		vm= new VirtueelGeheugen();
	}
	
	
	public Proces(int pid) {
		pageTable = new ArrayList<PTEntry>(16);
		this.pid=pid;
		vm= new VirtueelGeheugen(pid);
	}
	
	
	public Page getPage(int pagenummer) {
		return vm.getPage(pagenummer);
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


	public int getPageIdByFrameNummer(int i) {
		// deze methode wordt aangesproken, we zijn zeker dat het process
		// een page in het ram heeft op frame i. We willen gewoon weten welke frame
		
		//pageTable doorlopen
		int frameNummer=-1;
		
		for(PTEntry pte : pageTable) {
			if (pte.getFrameNr()==i) {
				frameNummer = i;
			}
		}
		
		if(frameNummer == -1) {System.out.println("dit process bevat geen overeenkomstige page");
								System.out.println("process::getPageIdByFrameNummer");}
		
		return frameNummer;
		
	}
	
	
	public void printPageTable() {
		System.out.println("----------------------------- PageTabel proces "+ this.getPid()+" ----------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println("\t Page Nummer \t || Frame Nummer \t || Last Time Accessed");
		for(int i=0; i<this.getPageTable().size();i++) {
			System.out.println("\t "+this.getPageTable().get(i).getPageNr()+" \t\t || " + this.getPageTable().get(i).getFrameNr()+" \t\t\t || " + this.getPageTable().get(i).getLaatsteKeerGebruikt());
		}
		
		System.out.println("-----------------------------------------------------------------------------------------");
		System.out.println("-----------------------------------------------------------------------------------------");
	}
	
}
