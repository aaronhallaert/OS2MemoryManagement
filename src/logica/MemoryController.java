package logica;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import application.Main;
import entities.Instructie;
import entities.PTEntry;
import entities.Proces;
import entities.RAM;
import entities.VirtueelGeheugen;
import io.DataProcessing;
import presentatie.HoofdMenuController;

/**
 * alle logica
 * @author aaron
 *
 */
public class MemoryController {
	
	
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
	//String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
	String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
	
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

	
	
	}


	
	


	


