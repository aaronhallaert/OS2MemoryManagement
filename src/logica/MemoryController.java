package logica;

import java.util.ArrayList;
import java.util.LinkedList;

import entities.Instructie;
import entities.Proces;
import io.DataProcessing;


public class MemoryController {
	
	
	public static LinkedList<Instructie> instructies=new LinkedList<Instructie>();
	
	public static ArrayList<Proces> processen=new ArrayList<Proces>();
	
	//hier ram definieren
	// virtual memory definieren
	
	//hier moeten ook nog een ram, en een virtueel geheugen aangemaakt worden
	//zorgen dat in de default constructors de juiste dingen qua paging al geset zijn
	
	//per uitgevoerde instructie wordt de klok met 1 opgehoogd
	static int klok = 0;
	 
	
	
	//leest alles in
	public static void instantiate() {
	//String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
	String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
	
	DataProcessing.findInstructies(pad, instructies, processen);	
	
	for(Instructie i: instructies) {
		System.out.println(i.toString());
		}
	
	//aanmaak RAM
	//aanmaak virtueel geheugen
	
	}
	

	
	public static void voerVolgendeInstructieUit() {
		//tijdelijke waarden van de instructie

			Instructie iTemp;
		
			iTemp= instructies.getFirst();
			
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
	
	
	}

	


