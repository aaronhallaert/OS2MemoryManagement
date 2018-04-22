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
	
	//zorgen dat in de default constructors de juiste dingen qua paging al geset zijn
	public static RAM ram=new RAM();
	public static VirtueelGeheugen virtueelGeheugen= new VirtueelGeheugen();
	
	//per uitgevoerde instructie wordt de klok met 1 opgehoogd
	public static int klok = 0;
	 
	
	
	//leest alles in
	public static void instantiate() {
	//String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
	String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
	
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
			iTemp.execute();
			
			
			
			//effectieve uitvoeren van de instructie
			/*case "Start":	System.out.println("start");
			
							aantalProcessenInRam = ram.getAantalProcessenAanwezig();
							switch(aantalProcessenInRam) {
							
							case 0:	//0 processen aanwezig in het RAM
									geefTwaalfPagesAanProces(ram, processen, proces);
									//voor de andere cases moet er hier nog info worden overgedragen
									//alloceer alle 12 frames aan het huidige process
									
									ram.setAantalProcessenAanwezig(aantalProcessenInRam+1);
									break;
							case 1:
									//neem de 6 minst recent gebruikte pages van het andere process
									//zet deze 6 in het virtuele geheugen
									//alloqueer deze 6 lege frames aan het nieuwe process
								
									break;
							case 2:
									//neem de 2 minst recent gebruikte pages van de andere 2 processen
									// zet deze 4 pages in het virtuele geheugen
									// alloqueer deze 4 lege frames aan het nieuwe process
								
									break;
							case 3:
									//neem van de 3 processen de minst recent gebruikte page
									//zet deze 3 pages naar het virtuele geheugen
									// alloqueer deze 3 lege frames aan het nieuwe process
								
									break;
							case 4: //RAM zit al vol, er kan geen nieuw process toegevoegd worden aan het ram
									
									break;
							default: 
									System.out.println("default");
									break;
							
							
							
							
							}
							// process toevoegen aan ram
							// pageTables aanpassen van de processen
							// 0->1 = alle 12 pages aan dit process
							// 1->2 = 6 van eerste process geven 
							break;
				
			default :	System.out.println("default");
						break;
				
			}
			
			*/
			
			
			
			
			
			
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

	//niet zeker als dit werkt, het schijnt wel beter / gezonder te zijn
	/*public RAM getRam() {
		return ram;
	}
	
	
	public VirtueelGeheugen getVM() {
		return virtueelGeheugen;
	}*/
	
	
	}


	
	


	


