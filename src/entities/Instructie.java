package entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import application.Main;
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
		Main.log(Level.INFO, "execute: "+ this.toString());
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
		List<Integer> adres;
		int pagenummer;
		int offset;
		int framenummer;
		Frame f;
		PTEntry pte;
		
		//effectieve uitvoeren van de instructie
		switch(operatie) {
		case "Read" :	System.out.println("read");
		
						// moet er niet ook vooraf gechechkt worden als het process uberhaupt aanwezig is?
						//wel voor de set van 20processen
		
						adres = splitsDecimaalAdresOp(virtueelAdres);
						
						//paginanummer en offset van het adres dat we moeten schrijven
						pagenummer= adres.get(0);
						offset= adres.get(1);
		
						//checken als de pagina die we willen lezen van het huidige proces al aanwezig is in ram
						if(!huidigProces.getPageTable().get(pagenummer).isPresent()) {
							
							
							
							//als de pagina niet aanwezig is, eerst swappen voor de LRU pagina die wel al aanwezig is in het proces
							int frameNummerWeg = MemoryController.getLRUFrameVanProces(huidigProces.getPid());								
							int pageNummerWeg = huidigProces.getPageIdByFrameNummer(frameNummerWeg);
							
							//bij het wegschrijven moet deze pagina enkel upgedate worden naar VM als hij aangepast is
							if(huidigProces.getPageTable().get(pageNummerWeg).isModified()) {
								//oude pagina wegschrijven
								huidigProces.schrijfNaarVM(ram.getFrame(frameNummerWeg).getGeheugenPlaatsen(), pageNummerWeg);
							}
							
							//preprocessing voor we van vm --> ram schrijven
							huidigProces.getPageTable().get(pageNummerWeg).setPresent(false);
							huidigProces.getPageTable().get(pageNummerWeg).setFrameNr(-1);
							
							//schrijf de page van vm --> ram
							ram.laadPageIn(huidigProces.getPage(pagenummer), frameNummerWeg);

							
							// pas waardes aan
							huidigProces.getPageTable().get(pagenummer).setPresent(true);
							huidigProces.getPageTable().get(pagenummer).setLaatsteKeerGebruikt(klok);
							huidigProces.getPageTable().get(pagenummer).setFrameNr(frameNummerWeg);
							
								
								
						}
						
						//3) de pagina zit nu sws in ram, de int lezen
						//huidig frame zoeken in de pageTable
						pte = huidigProces.getPageTable().get(pagenummer);
						framenummer = pte.getFrameNr();
						f = ram.getFrame(framenummer);
						
						//schrijf op die offset het willekeurig gegenereerd getal
						int a = f.lees(offset);
						huidigProces.getPageTable().get(pagenummer).setModified(true);
						
						break;
			
		case "Write":	System.out.println("write");
						
		
						adres= splitsDecimaalAdresOp(virtueelAdres);
						
						//paginanummer en offset van het adres dat we moeten schrijven
						pagenummer= adres.get(0);
						offset= adres.get(1);
						
						Main.log(Level.INFO, "Proces "+ huidigProces.getPid()+ " write naar page "+pagenummer+" met offset "+ offset);
						
						if(huidigProces.getPageTable().get(pagenummer).isPresent()) {
							Main.log(Level.INFO, "Pagina "+pagenummer+" van proces "+huidigProces.getPid()+" is aanwezig");
							
							//huidig frame zoeken in de pageTable
							pte = huidigProces.getPageTable().get(pagenummer);
							framenummer = pte.getFrameNr();
							f = ram.getFrame(framenummer);
							
							//schrijf op die offset het willekeurig gegenereerd getal
							f.schrijf(offset, (int)(Math.random()*50));
							
							//set modified en zet huidige tijd
							pte.setModified(true);
							pte.setLaatsteKeerGebruikt(klok);
							
						}
						
						
						//als de pagina niet aanwezig is --> swapping nodig
						else {
							
							
							//**************
							// oude page van proces eruit halen
							//***************
							//page en framenummer van het proces dat weg moet
							int frameNummerWeg = MemoryController.getLRUFrameVanProces(huidigProces.getPid());								
							int pageNummerWeg = huidigProces.getPageIdByFrameNummer(frameNummerWeg);
								
							if(huidigProces.getPageTable().get(pageNummerWeg).isModified()) {
								//oude pagina wegschrijven
								huidigProces.schrijfNaarVM(ram.getFrame(frameNummerWeg).getGeheugenPlaatsen(), pageNummerWeg);
							}
							huidigProces.getPageTable().get(pageNummerWeg).setPresent(false);
							huidigProces.getPageTable().get(pageNummerWeg).setFrameNr(-1);
							
							
							//schrijf de page van vm --> ram
							ram.laadPageIn(huidigProces.getPage(pagenummer), frameNummerWeg);
							
							//als de pagetable al een entry heeft gehad : de pagina is al gealloceerd geweest in RAM
									//  ---> pagetable aanpassen
							
								// pas waardes aan
								huidigProces.getPageTable().get(pagenummer).setPresent(true);
								huidigProces.getPageTable().get(pagenummer).setLaatsteKeerGebruikt(klok);
								huidigProces.getPageTable().get(pagenummer).setFrameNr(frameNummerWeg);
								
							
							
							
							
							//schrijven
							//huidig frame zoeken in de pageTable
							pte = huidigProces.getPageTable().get(pagenummer);
							framenummer = pte.getFrameNr();
							f = ram.getFrame(framenummer);
							
							//schrijf op die offset het willekeurig gegenereerd getal
							f.schrijf(offset, (int)(Math.random()*50));
							huidigProces.getPageTable().get(pagenummer).setModified(true);
							
							
						}
						
						
		
						break;
			
		case "Terminate":System.out.println("terminate");
						break;
			
		case "Start":	System.out.println("start");
						aantalProcessenInRam = MemoryController.ram.getAantalProcessenAanwezig();

						// er is reeds een proces aanwezig
						if(aantalProcessenInRam == 1) {
							startProces(1, 6, huidigProces);
							
						}
						else if (aantalProcessenInRam == 2) {
							startProces(2, 2, huidigProces);
							
						}
						else if (aantalProcessenInRam ==3) {
							
							startProces(3, 1, huidigProces);
							
							
							
						}
						else if (aantalProcessenInRam ==4) {
							System.out.println("4 processen reeds aanwezig");
							// swap 1 proces volledig uit , laadt 1 process volledig in
							// welk proces???
							
						}
						else if (aantalProcessenInRam ==0) {
							//alle pages van het ram worden aan het process toegekend
							
							//process: de page table aanpassen
								PTEntry ptEntry;
								for(int i =0; i<ram.grootte; i++) {
									ptEntry=huidigProces.getPageTable().get(i);
									ptEntry.setFrameNr(i);
									
									ptEntry.setPresent(true);
									ptEntry.setLaatsteKeerGebruikt(klok);
									
									
									
									
									ram.laadPageIn(huidigProces.getPage(i), i);
								}
								huidigProces.printPageTable();
								
								
					
								
							//aantalprocessen in ram updaten
								ram.setAantalProcessenAanwezig((aantalProcessenInRam+1));
									
						}
						break;
			
						default :
						Main.log(Level.SEVERE, "execute: "+ this.toString()+ " bevat geen operatie");
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
		/*
		System.out.println("binair final =" + binair);
		System.out.println(binair.substring(0,4));
		System.out.println(binair.substring(4));
		*/
		
		List<Integer> list= new ArrayList<Integer>();
		list.add(Integer.parseInt(binair.substring(0, 4), 2));
		list.add(Integer.parseInt(binair.substring(4),2));
		
		return list;
		
	}
	
	

private void startProces(int aantalProcessenInRam, int aantalPagesAfstaanPerProces, Proces huidigProces) {
	
	
	List<Integer> lruFrames=new ArrayList<Integer>();
	
	// alle lijsten van lru frames per proces
	Map<Integer, List<Integer>> lruFramesPerProces= new HashMap<Integer, List<Integer>>();
	
	//van elk 4 paginas naar elk 3 paginas, dwz elk 1 pagina afstaan
	lruFramesPerProces=MemoryController.getLRUFramesVanAlleProcessen(lruFramesPerProces, aantalPagesAfstaanPerProces);
	
	// samenvoegen van alle lru frames
	for(int i= 0; i<lruFramesPerProces.size(); i++) {
		for(int j= 1; j<lruFramesPerProces.get(i).size();j++) {
			lruFrames.add(lruFramesPerProces.get(i).get(j));
		}
	}
	
	
	// frames wegschrijven indien modified

	
	for(int i= 0; i<lruFramesPerProces.size(); i++) {
		List<Integer> lruFramesVanProces= lruFramesPerProces.get(i);
		Proces p= MemoryController.processen.get(lruFramesVanProces.get(0));
		for(int j=1; j<lruFramesVanProces.size();j++) {
			// paginanummer horend bij frame
			int paginanummer=p.getPageIdByFrameNummer(lruFramesVanProces.get(j));
			// page table entry van paginanummer
			PTEntry pteProces= p.getPageTable().get(paginanummer);
			
			// als frame is aangepast, schrijf frame naar pagina in vm
			if(pteProces.isModified()) {
				Main.log(Level.INFO, "Wegschrijven van aangepast frame "+ lruFramesVanProces.get(j));
				// frame meegeven en paginanummer
				p.schrijfNaarVM(MemoryController.ram.getFrame(lruFramesVanProces.get(i)).getGeheugenPlaatsen(), paginanummer);
			}
			else {
				Main.log(Level.INFO, "Er hoeft niets weggeschreven te worden, page " +pteProces.getPageNr()+ " van proces "+ huidigProces.getPid() +" in frame "+ pteProces.getFrameNr()+" werd niet aangepast");
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
		ptEntry2=huidigProces.getPageTable().get(i);
		ptEntry2.setFrameNr(lruFrames.get(i));
		
		ptEntry2.setPresent(true);
		ptEntry2.setLaatsteKeerGebruikt(MemoryController.klok);
		
		// laad ide pagina van huidig proces in
		MemoryController.ram.laadPageIn(huidigProces.getPage(i), lruFrames.get(i));
	}
	
	
	
	
	
	
	MemoryController.ram.setAantalProcessenAanwezig((aantalProcessenInRam+1));
	
	
}
	
	
}

