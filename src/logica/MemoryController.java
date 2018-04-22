package logica;

import java.util.ArrayList;

import entities.Instructie;
import entities.Proces;
import io.DataProcessing;


public class MemoryController {
	
	
	public static ArrayList<Instructie> instructies=new ArrayList<Instructie>();
	
	public static ArrayList<Proces> processen=new ArrayList<Proces>();
	
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
	
	//tijdelijke waarden van de instructie
		int processId;
		String operation;
		int vmAdres;

	//instructies en processen overlopen?
	for(Instructie i: instructies) {
	
		//inlezen van de instructie
		processId = i.getPid();
		operation = i.getOperatie();
		vmAdres   = i.getVirtueelAdres();
		
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
	}
	
	}
}
