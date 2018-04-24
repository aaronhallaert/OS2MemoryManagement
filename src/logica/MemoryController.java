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
	public static VirtueelGeheugen virtueelGeheugen= new VirtueelGeheugen();
	
	//per uitgevoerde instructie wordt de klok met 1 opgehoogd
	public static int klok = 0;
	 
	
	
	/**
	 * inlezen van XML file
	 */
	public static void instantiate() {
	String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
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


	
	


	


