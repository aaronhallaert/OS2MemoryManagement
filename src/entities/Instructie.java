package entities;

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
		int aantalProcessenInRam;
		String operatie=this.getOperatie();
		int pid= this.getPid();
		int virtueelAdres= this.getVirtueelAdres();
		VirtueelGeheugen virtueelGeheugen = MemoryController.virtueelGeheugen;
		RAM ram = MemoryController.ram;
		int klok = MemoryController.klok;
		Map<Integer, Proces> processen = MemoryController.processen;
		
		//effectieve uitvoeren van de instructie
		switch(operatie) {
		case "Read" :	System.out.println("read");
						break;
			
		case "Write":	System.out.println("write");
						
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
						
						int pagenummer = Integer.parseInt(binair.substring(0, 4), 2);
						int offset = Integer.parseInt(binair.substring(4),2);
						System.out.println(pagenummer);
						System.out.println(offset);

			
		
		
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
							
							//1) process: de page table aanpassen
								PTEntry ptEntry;
								for(int i =0; i<12; i++) {
									ptEntry=new PTEntry();
									ptEntry.setFrameNr(i);
									ptEntry.setPageNr(i);
									ptEntry.setPresent(true);
									processen.get(pid).getPageTable().add(ptEntry);
								}
								
							//2) effectief de pages naar het ram inladen 
								//enkel nodig als we swappen en dit doen we hier eigelijk niet
								
							//3) aantalprocessen in ram updaten
								ram.setAantalProcessenAanwezig(aantalProcessenInRam++);
									
						}
						break;
			
		default :
						System.out.println("execute: "+ this.toString());
						break;
	}
	
	
	}
}
