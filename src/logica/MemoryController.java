package logica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import entities.Instructie;
import entities.Proces;
import entities.RAM;
import entities.VirtueelGeheugen;
import io.DataProcessing;

/**
 * alle logica
 * @author aaron
 *
 */
public class MemoryController {
	
	
	public static LinkedList<Instructie> instructies=new LinkedList<Instructie>();
	
	public static Map<Integer, Proces> processen=new HashMap<Integer, Proces>();
	
	public static RAM ram=new RAM();
	
	public static VirtueelGeheugen virtueelGeheugen= new VirtueelGeheugen();
	
	
	// virtual memory definieren
	
	//hier moeten ook nog een ram, en een virtueel geheugen aangemaakt worden
	//zorgen dat in de default constructors de juiste dingen qua paging al geset zijn
	
	//per uitgevoerde instructie wordt de klok met 1 opgehoogd
	static int klok = 0;
	 
	
	
	//leest alles in
	public static void instantiate() {
	String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
	//String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
	
	DataProcessing.findInstructies(pad, instructies, processen);	
	
	for(Instructie i: instructies) {
		System.out.println(i.toString());
	}
	
	//aanmaak virtueel geheugen
	
	}
	

	
	public static void voerVolgendeInstructieUit() {
		//tijdelijke waarden van de instructie
		if(!instructies.isEmpty()) {
			Instructie iTemp;
		
			iTemp= instructies.pollFirst();
			
			//inlezen van de instructie
			int processId = iTemp.getPid();
			String operation = iTemp.getOperatie();
			int vmAdres   = iTemp.getVirtueelAdres();
			
			//effectieve uitvoeren van de instructie
			switch(operation) {
			case "Read" :	System.out.println("read");
							break;
				
			case "Write":	System.out.println("write");
							break;
				
			case "Terminate":System.out.println("terminate");
							break;
				
			case "Start":	System.out.println("start");
			
							
							break;
				
			default :
				
			}
			
			
			
			
			
			
			
			
			//nieuwe toestand van het geheugen
			
			//instructie uitgevoerd
			//klok 1 omhoog
			klok++;
			System.out.println("klok = "+klok);
		}
		else {
			System.out.println("einde bereikt");
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
			System.out.println("klok = "+klok);
		}
		
	}
	
	


	


