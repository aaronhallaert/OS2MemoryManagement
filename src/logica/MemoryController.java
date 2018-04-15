package logica;

import java.util.ArrayList;

import entities.Instructie;
import entities.Proces;
import io.DataProcessing;


public class MemoryController {
	public ArrayList<Instructie> instructies=new ArrayList<Instructie>();
	public ArrayList<Proces> processen=new ArrayList<Proces>();
	 
	
	public void instantiate() {
	String pad="D:\\School\\Industriele Ingenieurswetenschappen\\iiw Ba3\\Semester2\\Besturingssystemen 2\\Memory Management Opdracht\\virtual memory\\Instructions_30_3.xml";
	DataProcessing dp= new DataProcessing();
	dp.findInstructies(pad, instructies, processen);	
	
	
	for(Instructie i: instructies) {
		System.out.println(i.toString());
	}
	
	
	
	}
}
