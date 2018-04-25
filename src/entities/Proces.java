package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import application.Main;
import javafx.scene.control.Label;
import logica.MemoryController;

public class Proces {

	private VirtueelGeheugen vm;
	private List<PTEntry> pageTable;
	private int pid;
	
	private int laatsteKeerGebruikt;
	
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
		for(int i=0; i<16; i++) {
			pageTable.add(new PTEntry(i, false, false, -1, -1));
		}
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
	
	
	
	
	
	
	public int getLaatsteKeerGebruikt() {
		return laatsteKeerGebruikt;
	}


	public void setLaatsteKeerGebruikt(int laatsteKeerGebruikt) {
		this.laatsteKeerGebruikt = laatsteKeerGebruikt;
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
		int pageNummer=-1;
		
		for(PTEntry pte : pageTable) {
			if (pte.getFrameNr()==i) {
				pageNummer = pte.getPageNr();
			}
		}
		
		if(pageNummer == -1) {
			Main.log(Level.SEVERE, "proces "+this.getPid()+ " bevat geen overeenkomstige page, process::getPageIdByFrameNummer" );
		}
		
		return pageNummer;
		
	}
	
	
	public void printPageTable() {
		Label x= (Label) Main.getScene().lookup("#PTproces"+this.getPid());
		StringBuilder sb=new StringBuilder();
		
		sb.append("----------------------------- PageTabel proces "+ this.getPid()+" -------------------------------------------------- \n");
		sb.append("--------------------------------------------------------------------------------------------------- \n");
		sb.append("\t Page Nummer \t || Frame Nummer \t || Last Time Accessed \t || Present  \t || Modified \n");
		for(int i=0; i<this.getPageTable().size();i++) {
			sb.append("\t "+this.getPageTable().get(i).getPageNr()+"\t\t\t\t || " + this.getPageTable().get(i).getFrameNr()+"\t\t\t\t || "
		+ this.getPageTable().get(i).getLaatsteKeerGebruikt()+ "\t\t\t\t\t || " + this.getPageTable().get(i).isPresent()+ "\t\t || " + this.getPageTable().get(i).isModified()+"\n");
		}
		
		sb.append("--------------------------------------------------------------------------------------------------- \n");
		sb.append("--------------------------------------------------------------------------------------------------- \n");
		
		
		
		x.setText(sb.toString());
	}


	public Page mostRUPageNotPresent() {
		List<PTEntry> pageTableCopyNotPresent= new ArrayList<PTEntry> ();
		List<PTEntry> pageTableCopy= new ArrayList<PTEntry> (pageTable);
		Collections.sort(pageTableCopy, new Comparator<PTEntry>() {
			public int compare(PTEntry pt1, PTEntry pt2) {
				return Integer.compare(pt2.getLaatsteKeerGebruikt(), pt1.getLaatsteKeerGebruikt());
			}
		});	
		for(PTEntry e: pageTableCopy) {
			if(!e.isPresent()) {
				pageTableCopyNotPresent.add(e);
			}
		}
		return this.getPage(pageTableCopyNotPresent.get(0).getPageNr());
	}
	
	public void schrijfNaarVM(Map<Integer, Integer> geheugenPlaatsen, int paginanummer) {
		MemoryController.aantalKeerNaarVM++;
		this.vm.getPage(paginanummer).setGeheugenPlaatsen(new HashMap<Integer, Integer>(geheugenPlaatsen));
		
	}
	
}
