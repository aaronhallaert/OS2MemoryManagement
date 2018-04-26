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
import entities.Instructie;
import entities.PTEntry;
import entities.Page;
import entities.Proces;
import entities.RAM;
import entities.VirtueelGeheugen;
import io.DataProcessing;
import javafx.scene.control.ComboBox;
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
		List<Proces> aanwezigeProcessen=MemoryController.getProcessenInRam();
		
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
	
	}


	
	


	


