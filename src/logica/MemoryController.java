package logica;

import java.util.ArrayList;
import java.util.Stack;

import entities.Instructie;
import entities.Proces;
import io.DataProcessing;


public class MemoryController {
	public static Stack<Instructie> instructies=new Stack<Instructie>();
	public static ArrayList<Proces> processen=new ArrayList<Proces>();
	 
	//leest alles in
	public static void inlezenInstructies() {
	String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
	//String pad="C:\\Users\\tibo\\Documents\\OS2MemoryManagement\\Instructions_30_3.xml";
	
	DataProcessing.findInstructies(pad, instructies, processen);	
	
	for(Instructie i: instructies) {
		System.out.println(i.toString());
	}
	}
	
	

	public static void voerVolgendeInstructieUit() {
		instructies.pop().execute();
	}
	
	
	
}
