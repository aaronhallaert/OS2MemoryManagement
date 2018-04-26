package entities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import application.Main;
import javafx.scene.control.Label;
import logica.MemoryController;
import presentatie.HoofdMenuController;

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
		
		
		
		Label x= (Label) Main.getScene().lookup("#huidigeInstructie");
		x.setText("execute: "+this.toString());
		
		
		Label reeelAdresLabel= (Label) Main.getScene().lookup("#reeelAdres");
		Label virtueelAdresLabel= (Label) Main.getScene().lookup("#virtueelAdres");
		
		//HoofdMenuController.getHuidigeInstructieLabel().setText("execute: "+ this.toString());
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
		
		
		if(!MemoryController.ram.getAanwezigeProcessen().contains(huidigProces)) {
			Main.log(Level.SEVERE, "ja lap, read en proces zit niet in ram");
		}
						// moet er niet ook vooraf gechechkt worden als het process uberhaupt aanwezig is?
						// wel voor de set van 20processen
		
						adres = splitsDecimaalAdresOp(virtueelAdres);
						
						//paginanummer en offset van het adres dat we moeten schrijven
						pagenummer= adres.get(0);
						offset= adres.get(1);
						
						Main.log(Level.INFO,"Lezen van page "+pagenummer+" met offset "+ offset +" door proces "+pid);
						
						
						virtueelAdresLabel.setText("Page "+ pagenummer+ "\nOffset "+ offset);
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
							huidigProces.setLaatsteKeerGebruikt(klok);
							huidigProces.getPageTable().get(pagenummer).setFrameNr(frameNummerWeg);
							
							Main.log(Level.INFO, "Page is niet aanwezig in RAM, werd geswapt met frame "+ frameNummerWeg+ "corresponderend met page "+pageNummerWeg +" van proces " + pid+ " (het huidig proces)");
							
								
						}
						else {
							Main.log(Level.INFO, "page is aanwezig in ram op frame "+ huidigProces.getPageTable().get(pagenummer).getFrameNr());
						}
						
						//3) de pagina zit nu sws in ram, de int lezen
						//huidig frame zoeken in de pageTable
						pte = huidigProces.getPageTable().get(pagenummer);
						framenummer = pte.getFrameNr();
						f = ram.getFrame(framenummer);
						
						
						
						reeelAdresLabel.setText("Frame "+framenummer+ "\nOffset "+ offset);
						
						//lees op die offset het willekeurig gegenereerd getal
						int a = f.lees(offset);
						
						huidigProces.getPageTable().get(pagenummer).setModified(true);
						
						break;
			
		case "Write":	System.out.println("write");
						
		if(!MemoryController.ram.getAanwezigeProcessen().contains(huidigProces)) {
			Main.log(Level.SEVERE, "ja lap, write en proces zit niet in ram");
		}
						adres= splitsDecimaalAdresOp(virtueelAdres);
						
						//paginanummer en offset van het adres dat we moeten schrijven
						pagenummer= adres.get(0);
						offset= adres.get(1);
						
						
						virtueelAdresLabel.setText("Page " +pagenummer+"\nOffset " + offset);
						
						Main.log(Level.INFO, "Proces "+ huidigProces.getPid()+ " write naar page "+pagenummer+" met offset "+ offset);
						
						if(huidigProces.getPageTable().get(pagenummer).isPresent()) {
							Main.log(Level.INFO, "Pagina "+pagenummer+" van proces "+huidigProces.getPid()+" is aanwezig");
							
							//huidig frame zoeken in de pageTable
							pte = huidigProces.getPageTable().get(pagenummer);
							framenummer = pte.getFrameNr();
							f = ram.getFrame(framenummer);
							
							reeelAdresLabel.setText("Frame " +framenummer+"\nOffset " + offset);
							
							
							//schrijf op die offset het willekeurig gegenereerd getal
							f.schrijf(offset, (int)(Math.random()*50));
							
							//set modified en zet huidige tijd
							pte.setModified(true);
							pte.setLaatsteKeerGebruikt(klok);
							huidigProces.setLaatsteKeerGebruikt(klok);
							
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
								huidigProces.setLaatsteKeerGebruikt(klok);
							
							
							
							
							//schrijven
							//huidig frame zoeken in de pageTable
							pte = huidigProces.getPageTable().get(pagenummer);
							framenummer = pte.getFrameNr();
							f = ram.getFrame(framenummer);
							
							reeelAdresLabel.setText("Frame " +framenummer+"\nOffset " + offset);
							
							//schrijf op die offset het willekeurig gegenereerd getal
							f.schrijf(offset, (int)(Math.random()*50));
							huidigProces.getPageTable().get(pagenummer).setModified(true);
							
							
						}
						
						
		
						break;
			
		case "Terminate":System.out.println("terminate");
						
		if(!MemoryController.ram.getAanwezigeProcessen().contains(huidigProces)) {
			Main.log(Level.SEVERE, "ja lap, terminate en proces zit niet in ram");
		}
		
						List<Integer> vrijgekomenFrames= new ArrayList<>();
						// proces eruit halen en frames verdelen onder andere processen
						int aantalFramesInRam= 0;
						for (int i =0 ;i<huidigProces.getPageTable().size();i++) {
							if (huidigProces.getPageTable().get(i).isPresent()) {
								aantalFramesInRam++;
								if(huidigProces.getPageTable().get(i).isModified()) {
									//wegschrijven frame naar vm
									Main.log(Level.INFO, "Frame "+huidigProces.getPageTable().get(i).getFrameNr()+" is aangepast en wordt weggeschreven naar page "+ huidigProces.getPageTable().get(i).getPageNr() );
									huidigProces.schrijfNaarVM(ram.getFrame(huidigProces.getPageTable().get(i).getFrameNr()).getGeheugenPlaatsen(), huidigProces.getPageTable().get(i).getPageNr());

								}
								
								
								vrijgekomenFrames.add(huidigProces.getPageTable().get(i).getFrameNr());
								ram.getFrame(huidigProces.getPageTable().get(i).getFrameNr()).getGeheugenPlaatsen().clear();
								ram.getFrame(huidigProces.getPageTable().get(i).getFrameNr()).setBevatPage(false);
								huidigProces.getPageTable().get(huidigProces.getPageTable().get(i).getPageNr()).setFrameNr(-1);
								huidigProces.getPageTable().get(huidigProces.getPageTable().get(i).getPageNr()).setPresent(false);
							}
						}
						
						ram.setAantalProcessenAanwezig(ram.getAantalProcessenAanwezig()-1);
						ram.getAanwezigeProcessen().remove(huidigProces);
						
						verdeelOverProcessen(vrijgekomenFrames);
						
						// verdelen van frames naar aanwezige processen
						break;
			
		case "Start":	System.out.println("start");
						ram.getAanwezigeProcessen().add(huidigProces);
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
							
								Main.log(Level.SEVERE, "ja lap, 5de proces wil erin rammen");
							
							
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
									huidigProces.setLaatsteKeerGebruikt(klok);
									
									
									
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

	private void verdeelOverProcessen(List<Integer> vrijgekomenFrames) {
		
		RAM ram=MemoryController.ram;
		
		int framesPerProces=0;
		if(ram.getAantalProcessenAanwezig()==1) {
			framesPerProces= vrijgekomenFrames.size();
			
		}
		if(ram.getAantalProcessenAanwezig()==2) {
			framesPerProces=vrijgekomenFrames.size()/2;
		}
		if(ram.getAantalProcessenAanwezig()==3) {
			framesPerProces=vrijgekomenFrames.size()/3;
		}
		
		if(ram.getAantalProcessenAanwezig()==0) {
			Main.log(Level.INFO, "Ram is helemaal leeg, frames hoeven niet verdeeld te worden");
		}
		else {
			int i=0;
			List<List<Integer>> parts = chopped(vrijgekomenFrames, framesPerProces);
			
			for(Proces p: ram.getAanwezigeProcessen()) {
				
				List<Integer> vrijgekomenFramesVoorDitProces= parts.get(i);
				for(int x=0; x<vrijgekomenFramesVoorDitProces.size(); x++) {
					
					// copy most recently used but not present page naar frame
					Page mRUPage= p.mostRUPageNotPresent();
					ram.getFrame(vrijgekomenFramesVoorDitProces.get(x)).copyPage(mRUPage);
					p.getPageTable().get(mRUPage.getPageNummer()).setLaatsteKeerGebruikt(MemoryController.klok);
					p.setLaatsteKeerGebruikt(MemoryController.klok);
					p.getPageTable().get(mRUPage.getPageNummer()).setPresent(true);
					p.getPageTable().get(mRUPage.getPageNummer()).setFrameNr(vrijgekomenFramesVoorDitProces.get(x));
					
				}
				
				i++;
			}
			Main.log(Level.INFO, "Er zijn "+vrijgekomenFrames.size()+" frames vrijgekomen en werden verdeeld onder de aanwezige processen");
			
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
	
	
	static <T> List<List<T>> chopped(List<T> list, final int L) {
	    List<List<T>> parts = new ArrayList<List<T>>();
	    final int N = list.size();
	    for (int i = 0; i < N; i += L) {
	        parts.add(new ArrayList<T>(
	            list.subList(i, Math.min(N, i + L)))
	        );
	    }
	    return parts;
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
				p.schrijfNaarVM(MemoryController.ram.getFrame(lruFramesVanProces.get(j)).getGeheugenPlaatsen(), paginanummer);
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
		huidigProces.setLaatsteKeerGebruikt(MemoryController.klok);
		// laad ide pagina van huidig proces in
		MemoryController.ram.laadPageIn(huidigProces.getPage(i), lruFrames.get(i));
	}
	
	
	
	
	
	
	MemoryController.ram.setAantalProcessenAanwezig((aantalProcessenInRam+1));
	
	
}
	
	
}

