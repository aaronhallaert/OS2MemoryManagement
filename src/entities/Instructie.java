package entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logica.MemoryController;

public class Instructie {
	
	private int pid;
	private String operatie;
	private int virtueelAdres;
	
	// constructoren
	public Instructie() {
		pid=0;
		operatie=null;
		virtueelAdres=0;
	}
	
	public Instructie(int pid, String operatie, int virtueelAdres) {
		super();
		this.pid = pid;
		this.operatie = operatie;
		this.virtueelAdres = virtueelAdres;
	}

	
	
	// getters en setters
	public int getPid() {
		return pid;
	}
	public String getOperatie() {
		return operatie;
	}	
	public int getVirtueelAdres() {
		return virtueelAdres;
	}

	@Override
	public String toString() {
		return "Instructie [pid=" + pid + ", operatie=" + operatie + ", virtueelAdres=" + virtueelAdres + "]";
	}
	
	public void execute() {
		
		// belangrijk bij opstarten proces
		int aantalProcessenInRam;
		
		
		// eigenschappen van instructie
		String operatie=this.getOperatie();
		int pid= this.getPid();
		int virtueelAdres= this.getVirtueelAdres();
		
		
		// algemene variabelen
		RAM ram = MemoryController.ram;
		int klok = MemoryController.klok;
		Map<Integer, Proces> processen = MemoryController.processen;
		
		
		Proces huidigProces= processen.get(pid);
		
		//effectieve uitvoeren van de instructie
		switch(operatie) {
		case "Read" :	System.out.println("read");
						break;
			
		case "Write":	System.out.println("write");
						
		
						List<Integer> adres= splitsDecimaalAdresOp(virtueelAdres);
						int pagenummer= adres.get(0);
						int offset= adres.get(1);
						System.out.println("pagenummer: "+pagenummer);
						System.out.println("offset: "+offset);
						

			
		
		
						break;
			
		case "Terminate":System.out.println("terminate");
						break;
			
		case "Start":	System.out.println("start");
						aantalProcessenInRam = ram.getAantalProcessenAanwezig();

						if(aantalProcessenInRam == 1) {}
						else if (aantalProcessenInRam == 2) {}
						else if (aantalProcessenInRam ==3) {}
						else if (aantalProcessenInRam ==4) {}
						else if (aantalProcessenInRam ==0) {
							//alle pages van het ram worden aan het process toegekend
							
							//process: de page table aanpassen
								PTEntry ptEntry;
								for(int i =0; i<ram.grootte; i++) {
									ptEntry=new PTEntry();
									ptEntry.setFrameNr(i);
									ptEntry.setPageNr(i);
									ptEntry.setPresent(true);
									
									
									// toevoegen van pageTableEntry aan pagetable
									huidigProces.getPageTable().add(ptEntry);
									ram.laadPageIn(huidigProces.getPage(i), i);
								}
								
								
					
								
							//aantalprocessen in ram updaten
								ram.setAantalProcessenAanwezig(aantalProcessenInRam++);
									
						}
						break;
			
		default :
						System.out.println("execute: "+ this.toString()+ " bevat geen operatie");
						break;
	}
	
	
	}

	/**
	 * vertaling van decimaal virtueel adres naar pagenummer en offset (decimaal)
	 * met behulp van binaire conversie
	 * 
	 * @return List met pagenummer (0) en offset (1) decimaal
	 */
	private List<Integer> splitsDecimaalAdresOp(int virtueelAdres) {
		// virtueel adres omzetten in een paginanummer + offset in de pagina
		String binair = Integer.toBinaryString(virtueelAdres);
		
		//nullen bijzetten tot we 12 characters hebben
		StringBuilder sb = new StringBuilder();
		for(int i=0 ; i< (16-binair.length()); i++) {
			sb.append("0");
		}
		sb.append(binair);
		binair = sb.toString();
		
		System.out.println("binair final =" + binair);
		System.out.println(binair.substring(0,4));
		System.out.println(binair.substring(4));
		
		
		List<Integer> list= new ArrayList<Integer>();
		list.add(Integer.parseInt(binair.substring(0, 4), 2));
		list.add(Integer.parseInt(binair.substring(4),2));
		
		return list;
		
	}
}
