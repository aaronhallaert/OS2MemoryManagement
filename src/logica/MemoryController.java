package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import application.Main;
import entities.Frame;
import entities.Instructie;
import entities.PTEntry;
import entities.Page;
import entities.Proces;
import entities.RAM;
import entities.VirtueelGeheugen;
import io.DataProcessing;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import presentatie.HoofdMenuController;

/**
 * alle logica
 * @author aaron
 *
 */
public class MemoryController {
	public static int aantalKeerNaarMem;
	public static int aantalKeerNaarVM;
	
	public static LinkedList<Instructie> instructies=new LinkedList<Instructie>();
	
	public static Map<Integer, Proces> processen=new HashMap<Integer, Proces>();
	
	//zorgen dat in de default constructors de juiste dingen qua paging al geset zijn
	public static RAM ram=new RAM();
	
	
	//per uitgevoerde instructie wordt de klok met 1 opgehoogd
	public static int klok = 0;
	 
	
	
	/**
	 * inlezen van XML file
	 */
	public static void instantiate() {
	ComboBox comboBox= 	(ComboBox) Main.getScene().lookup("#instructieKeuze");
	String keuze= (String) comboBox.getValue();
		
	
	String pad=" ";
	if(keuze==null) {
		pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
		//String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
		
	}
	else if(keuze.equals("30 i, 3 p")) {
		pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";		
		//String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
		
	}
	else if(keuze.equals("20000 i, 4 p")) {
		pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_20000_4.xml";			
		//String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_20000_4.xml";
		
	}
	else if(keuze.equals("20000 i, 20 p")) {
		pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_20000_20.xml";			
		//String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_20000_20.xml";
		
	}
	
	//String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
	
	DataProcessing.findInstructies(pad, instructies, processen);	
	
	for(Instructie i: instructies) {
		System.out.println(i.toString());
		}
	//aanmaak virtueel geheugen
	}
	

	/**
	 * eerstvolgende instructie uitvoeren
	 */
	public static void voerVolgendeInstructieUit() {
		//als er nog instructies zijn
		if(!instructies.isEmpty()) {
			Instructie iTemp;
			iTemp= instructies.pollFirst();
			iTemp.execute();
			klok++;
			
		}
		
		
		else {
			Main.log(Level.WARNING, "Einde van instructieset bereikt");
		}
		
		
		}
	
	
	public static void voerAlleInstructiesUit() {
		//tijdelijke waarden van de instructie
		while(!instructies.isEmpty()) {
			Instructie iTemp;
		
			iTemp= instructies.pollFirst();
			
			iTemp.execute();
			}
			
			
			
			
			
			
			
			
			//nieuwe toestand van het geheugen
			
			//instructie uitgevoerd
			//klok 1 omhoog
			klok++;
			
		}
	
	
	public static List<Proces> getProcessenInRam(){
		List<Proces> aanwezigeProcessen= new ArrayList<Proces>();
		for(int i= 0; i<ram.getFrames().size(); i++) {
			if(!aanwezigeProcessen.contains(processen.get(ram.getFrame(i).getAanwezigProces()))){
				aanwezigeProcessen.add(processen.get(ram.getFrame(i).getAanwezigProces()));
			}
			
		}
		
		return aanwezigeProcessen;
		
		
		
	}


	public static Map<Integer, List<Integer>> getLRUFramesVanAlleProcessen(Map<Integer, List<Integer>> lruFramesPerProces, int aantal) {
		// alle processen die in ram zitten
		List<Proces> aanwezigeProcessen=getProcessenInRam();
		
		// overlopen van aanwezige processen
		for(int i=0;i < aanwezigeProcessen.size(); i++) {
			
			
			//page table opvragen
			List<PTEntry> pageTable= new ArrayList<PTEntry>(aanwezigeProcessen.get(i).getPageTable());	
			List<PTEntry> truePages= new ArrayList<PTEntry> ();
			// sorteren volgens LRU
			Collections.sort(pageTable, new Comparator<PTEntry>() {
				public int compare(PTEntry pt1, PTEntry pt2) {
					return Integer.compare(pt1.getLaatsteKeerGebruikt(), pt2.getLaatsteKeerGebruikt());
				}
			});	
			for(int x=0; x<pageTable.size(); x++) {
				if(pageTable.get(x).isPresent()) {
					truePages.add(pageTable.get(x));
				}
			}
			
			// lijst met (voor 1 proces aanwezig 6) # lru frames	
			List<Integer> lruFramesVanProces= new ArrayList<Integer>();
			
			lruFramesVanProces.add(aanwezigeProcessen.get(i).getPid());
			for(int j=0; j<aantal; j++) {
				lruFramesVanProces.add(truePages.get(j).getFrameNr());
			}
			lruFramesPerProces.put(i, lruFramesVanProces);
			
			
			
		}
		
		return lruFramesPerProces;
	}

	/**
	 * verdelen van vrijgekomen frames over processen in ram
	 * @param vrijgekomenFrames
	 */
	public static void verdeelOverProcessen(List<Integer> vrijgekomenFrames) {
		
		
		
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
					p.getPageTable().get(mRUPage.getPageNummer()).setLaatsteKeerGebruikt(klok);
					p.setLaatsteKeerGebruikt(klok);
					p.getPageTable().get(mRUPage.getPageNummer()).setPresent(true);
					p.getPageTable().get(mRUPage.getPageNummer()).setFrameNr(vrijgekomenFramesVoorDitProces.get(x));
					
				}
				
				i++;
			}
			Main.log(Level.INFO, "Er zijn "+vrijgekomenFrames.size()+" frames vrijgekomen en werden verdeeld onder de aanwezige processen");
			
		}
		
		
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

	
	/**
	 * vertaling van decimaal virtueel adres naar pagenummer en offset (decimaal)
	 * met behulp van binaire conversie
	 * 
	 * @return List met pagenummer (0) en offset (1) decimaal
	 */
	public static List<Integer> splitsDecimaalAdresOp(int virtueelAdres) {
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
	

	public static int getLRUFrameVanProces(int pid) {
		
		//opvragen van alle frames van het huidige proces
		Proces p = processen.get(pid);
		
		//new maken omdat de pagetable niet gesorteerd zou worden
		List<PTEntry> pageTable = new ArrayList<>(p.getPageTable());
		
		Collections.sort(pageTable, new Comparator<PTEntry>() {
			public int compare(PTEntry pt1, PTEntry pt2) {
				return Integer.compare(pt1.getLaatsteKeerGebruikt(), pt2.getLaatsteKeerGebruikt());
			}
		});	
		
		List<PTEntry> truePages=new ArrayList<PTEntry>();
		
		for(int i=0; i<pageTable.size(); i++) {
			if(pageTable.get(i).isPresent()) {
				truePages.add(pageTable.get(i));
			}
		}
		
		
		
		//daaruit de frame halen die de laagste klokwaarde heeft
		return truePages.get(0).getFrameNr();
	}

	/**
	 * swappen van laatst gebruikt proces in ram met meegegeven proces
	 * @param proces
	 */
	public static void swapLRUProces(Proces proces) {
		Set<Proces> aanwezigInRamProcessen=ram.getAanwezigeProcessen();
		int i=Integer.MAX_VALUE;
		Proces lruProces=null;
		for(Proces p: aanwezigInRamProcessen) {
			if (p.getLaatsteKeerGebruikt()<i) {
				i=p.getLaatsteKeerGebruikt();
				lruProces=p;
			}
		}
		List<PTEntry> aanwezigEntries=new ArrayList<PTEntry>();
		for(int a= 0; a<lruProces.getPageTable().size(); a++) {
			if(lruProces.getPageTable().get(a).isPresent()) {
				aanwezigEntries.add(lruProces.getPageTable().get(a));
			}
		}
		
		
		for(PTEntry e: aanwezigEntries) {
			int framenummer=e.getFrameNr();
			int pagenummer= e.getPageNr();
			
			if(e.isModified()) {
				lruProces.schrijfNaarVM(ram.getFrame(framenummer).getGeheugenPlaatsen(), pagenummer);
			}
			
			ram.getFrame(framenummer).setBevatPage(false);
			ram.getFrame(framenummer).setAanwezigProces(-1);
			ram.getFrame(framenummer).getGeheugenPlaatsen().clear();
			
			ram.getFrame(framenummer).copyPage(proces.getPage(pagenummer));
			
		}
		
		
	}
	
	
	/**
	 * schrijf methode
	 * @param adres: lijst van pagenummer en offset
	 * @param virtueelAdres: volledig virtueel adres decimaal
	 * @param pagenummer: deel 1 van adres
	 * @param offset: deel 2 van adres
	 * @param virtueelAdresLabel: GUI label met virtueel adres 
	 * @param huidigProces: proces van de instructie
	 * @param pte: page table entry
	 * @param framenummer
	 * @param reeelAdresLabel: Gui label met reeel adres
	 * @param f: Frame waar naar geschreven moet worden
	 */
	public static void writeMethod(List<Integer> adres, int virtueelAdres, int pagenummer, int offset, Label virtueelAdresLabel, Proces huidigProces,
			PTEntry pte, int framenummer, Label reeelAdresLabel, Frame f) {

		
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
			
			reeelAdresLabel.setText("Frame "+framenummer+ "\nOffset "+ offset+"\n"+ frameEnOffsetNaarAdres(framenummer, offset));
			
			
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
			int frameNummerWeg = getLRUFrameVanProces(huidigProces.getPid());								
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
			
			reeelAdresLabel.setText("Frame "+framenummer+ "\nOffset "+ offset+"\n"+ frameEnOffsetNaarAdres(framenummer, offset));
			
			//schrijf op die offset het willekeurig gegenereerd getal
			f.schrijf(offset, (int)(Math.random()*50));
			huidigProces.getPageTable().get(pagenummer).setModified(true);
			
			
		}
		
		
	}
	
	public static void terminateProces(Proces huidigProces) {
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
		
		// verdelen van frames naar aanwezige processen
		verdeelOverProcessen(vrijgekomenFrames);
		
		
	}

	/**
	 * opstarten van proces wanneer meerdere processen aanwezig zijn
	 * @param aantalProcessenInRam
	 * @param aantalPagesAfstaanPerProces
	 * @param huidigProces
	 */
	public static void startProces(int aantalProcessenInRam, int aantalPagesAfstaanPerProces, Proces huidigProces) {
	
		
		List<Integer> lruFrames=new ArrayList<Integer>();
		
		// alle lijsten van lru frames per proces
		Map<Integer, List<Integer>> lruFramesPerProces= new HashMap<Integer, List<Integer>>();
		
		//van elk 4 paginas naar elk 3 paginas, dwz elk 1 pagina afstaan
		lruFramesPerProces=getLRUFramesVanAlleProcessen(lruFramesPerProces, aantalPagesAfstaanPerProces);
		
		// samenvoegen van alle lru frames
		for(int i= 0; i<lruFramesPerProces.size(); i++) {
			for(int j= 1; j<lruFramesPerProces.get(i).size();j++) {
				lruFrames.add(lruFramesPerProces.get(i).get(j));
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
					Main.log(Level.INFO, "Wegschrijven van aangepast frame "+ lruFramesVanProces.get(j));
					
					// frame meegeven en paginanummer
					p.schrijfNaarVM(ram.getFrame(lruFramesVanProces.get(j)).getGeheugenPlaatsen(), paginanummer);
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
			ptEntry2.setLaatsteKeerGebruikt(klok);
			huidigProces.setLaatsteKeerGebruikt(klok);
			// laad ide pagina van huidig proces in
			ram.laadPageIn(huidigProces.getPage(i), lruFrames.get(i));
		}
		
		
		
		
		
		
		ram.setAantalProcessenAanwezig((aantalProcessenInRam+1));
		
	
	}

	/**
	 * opstarten van proces wanneer nog geen processen in ram zitten
	 * @param huidigProces
	 */
	public static void startProcesLeegRam(Proces huidigProces) {
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
			ram.setAantalProcessenAanwezig((ram.getAantalProcessenAanwezig()+1));
	}
	
	public static int frameEnOffsetNaarAdres(int framenummer, int offset) {
		int adres= framenummer*4096+offset;
		
		return adres;
	}


	
	/**
	 * lees methode
	 * @param adres: lijst van pagenummer en offset
	 * @param pagenummer: deel 1 van adres
	 * @param offset: deel 2 van adres
	 * @param huidigProces: proces van de instructie
	 * @param pid
	 * @param virtueelAdres: volledig virtueel adres decimaal
	 * @param virtueelAdresLabel: GUI label met virtueel adres 
	 * @param pte: page table entry
	 * @param framenummer
	 * @param f: Frame waar naar geschreven moet worden
	 * @param reeelAdresLabel: Gui label met reeel adres
	 */
	public static void readMethod(List<Integer> adres, int pagenummer, int offset, Proces huidigProces, int pid, int virtueelAdres, 
			Label virtueelAdresLabel, PTEntry pte, int framenummer, Frame f, Label reeelAdresLabel) {
		
				adres = splitsDecimaalAdresOp(virtueelAdres);
				
				//paginanummer en offset van het adres dat we moeten schrijven
				pagenummer= adres.get(0);
				offset= adres.get(1);
				
				Main.log(Level.INFO,"Lezen van page "+pagenummer+" met offset "+ offset +" door proces "+pid);
				
				
				virtueelAdresLabel.setText("Page "+ pagenummer+ "\nOffset "+ offset);
				//checken als de pagina die we willen lezen van het huidige proces al aanwezig is in ram
				if(!huidigProces.getPageTable().get(pagenummer).isPresent()) {
					
					
					//als de pagina niet aanwezig is, eerst swappen voor de LRU pagina die wel al aanwezig is in het proces
					int frameNummerWeg = getLRUFrameVanProces(huidigProces.getPid());								
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
				
				
				
				reeelAdresLabel.setText("Frame "+framenummer+ "\nOffset "+ offset+"\n"+frameEnOffsetNaarAdres(framenummer, offset));
				
				//lees op die offset het willekeurig gegenereerd getal
				int a = f.lees(offset);
				huidigProces.getPageTable().get(pagenummer).setLaatsteKeerGebruikt(klok);
				huidigProces.setLaatsteKeerGebruikt(klok);
				
		
	}

}


	
	


	


