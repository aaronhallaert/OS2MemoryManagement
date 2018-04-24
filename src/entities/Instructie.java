package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
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
						
						//paginanummer en offset van het adres dat we moeten schrijven
						int pagenummer= adres.get(0);
						int offset= adres.get(1);
						
						System.out.println("pagenummer: "+pagenummer);
						System.out.println("offset: "+offset);
						
						if(huidigProces.getPageTable().get(pagenummer).isPresent()) {
							System.out.println("huidige pagina is aanwezig");
							
							//huidig frame zoeken in de pageTable
							PTEntry pte = huidigProces.getPageTable().get(pagenummer);
							int framenummer = pte.getFrameNr();
							Frame f = ram.getFrame(framenummer);
							
							//schrijf op die offset het willekeurig gegenereerd getal
							f.schrijf(offset, (int)(Math.random()*50));
							
							//set modified en zet huidige tijd
							pte.setModified(true);
							pte.setLaatsteKeerGebruikt(klok);
							
						}
						
						else {
							//laadt de pagina in
							//swap de minst gebruikte pagina die aanwezig is van het huidig proces
							//met de pagina die opgevraagd wordt
							
							//daarna toch gewoon schrijven
						}
						huidigProces.printPageTable();
		
						break;
			
		case "Terminate":System.out.println("terminate");
						break;
			
		case "Start":	System.out.println("start");
						aantalProcessenInRam = MemoryController.ram.getAantalProcessenAanwezig();

					
						
						// er is reeds een proces aanwezig
						if(aantalProcessenInRam == 1) {
							System.out.println("er is al een proces aanwezig");
							List<Integer> lruFrames=new ArrayList<Integer>();
							
							// alle lijsten van lru frames per proces
							Map<Integer, List<Integer>> lruFramesPerProces= new HashMap<Integer, List<Integer>>();
							
							lruFramesPerProces=MemoryController.getLRUFramesVanAlleProcessen(lruFramesPerProces, 6);
							
							
							for(int i= 0; i<lruFramesPerProces.size(); i++) {
								for(int j= 1; j<lruFramesPerProces.get(0).size();j++) {
									lruFrames.add(lruFramesPerProces.get(0).get(j));
								}
							}
							
							
							
							// frames wegschrijven indien modified
						
							
							for(int i= 0; i<lruFramesPerProces.size(); i++) {
								List<Integer> lruFramesVanProces= lruFramesPerProces.get(i);
								Proces p= processen.get(lruFramesVanProces.get(0));
								for(int j=1; j<lruFramesVanProces.size();j++) {
									// paginanummer horend bij frame
									int paginanummer=p.getPageIdByFrameNummer(lruFramesVanProces.get(j));
									// page table entry van paginanummer
									PTEntry pteProces= p.getPageTable().get(paginanummer);
									
									// als frame is aangepast, schrijf frame naar pagina in vm
									if(pteProces.isModified()) {
										System.out.println("Wegschrijven van aangepaste frames");
										// frame meegeven en paginanummer
										p.schrijfNaarVM(ram.getFrame(lruFramesVanProces.get(i)).getGeheugenPlaatsen(), paginanummer);
									}
									else {
										System.out.println("Er hoeft niets weggeschreven te worden");
									}
									
									
									// page table aanpassen van uitgeworpen proces
									PTEntry ptEntry1;
									
									ptEntry1=p.getPageTable().get(paginanummer);
									ptEntry1.setFrameNr(-1);
									ptEntry1.setPresent(false);
									
									
								}
								
								
								
									
									
									
								
								
								
								
								
							}
							
							
							
							
							
							
							
							
							
							// inladen nieuw proces
						
							PTEntry ptEntry2;
							for(int i=0; i<lruFrames.size();i++) {
								ptEntry2=new PTEntry();
								ptEntry2.setFrameNr(lruFrames.get(i));
								ptEntry2.setPageNr(i);
								ptEntry2.setPresent(true);
								ptEntry2.setLaatsteKeerGebruikt(klok);
								huidigProces.getPageTable().add(ptEntry2);
								// laad ide pagina van huidig proces in
								ram.laadPageIn(huidigProces.getPage(i), lruFrames.get(i));
							}
							
							huidigProces.printPageTable();
							processen.get(0).printPageTable();
							
							
							
							
							ram.setAantalProcessenAanwezig((aantalProcessenInRam+1));
							
						}
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
									ptEntry.setLaatsteKeerGebruikt(klok);
									
									
									// toevoegen van pageTableEntry aan pagetable
									huidigProces.getPageTable().add(ptEntry);
									ram.laadPageIn(huidigProces.getPage(i), i);
								}
								huidigProces.printPageTable();
								
								
					
								
							//aantalprocessen in ram updaten
								ram.setAantalProcessenAanwezig((aantalProcessenInRam+1));
									
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
