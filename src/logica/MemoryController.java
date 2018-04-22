package logica;

import java.util.ArrayList;

import entities.Instructie;
import entities.Proces;
import io.DataProcessing;


public class MemoryController {
	public static ArrayList<Instructie> instructies=new ArrayList<Instructie>();
	public static ArrayList<Proces> processen=new ArrayList<Proces>();
	 
	//leest alles in
	public static void instantiate() {
	//String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
	String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
	
	DataProcessing.findInstructies(pad, instructies, processen);	
	
	
	for(Instructie i: instructies) {
		System.out.println(i.toString());
	}
	
	//instructies en processen overlopen?
	
	
	}
}
