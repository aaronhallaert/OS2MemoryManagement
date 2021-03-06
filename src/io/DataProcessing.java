package io;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import application.Main;
import entities.Instructie;
import entities.PTEntry;
import entities.Proces;

public class DataProcessing {
	//xml file analyseren en instructies eruit halen
			public static void findInstructies(String pad, LinkedList<Instructie> instructies, Map<Integer,Proces> processen) {

			try {
				Set<Integer> procesNumbers= new HashSet<>();
				File xmlFile = new File(pad);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
				doc.getDocumentElement().normalize();

				// oplijsten van alle processen
				NodeList instructieList = doc.getElementsByTagName("instruction");
				
				for (int i = 0; i < instructieList.getLength(); i++) {
					// System.out.print(i+")");
					Node nNode = instructieList.item(i);
					// System.out.println(" Current Element: "+ nNode.getNodeName());
					if (nNode.getNodeType() == Node.ELEMENT_NODE) {
						Element eElement = (Element) nNode;
						int PID = Integer.parseInt(eElement.getElementsByTagName("processID").item(0).getTextContent());
						String operatie = eElement.getElementsByTagName("operation").item(0).getTextContent();
						int adres = Integer
								.parseInt(eElement.getElementsByTagName("address").item(0).getTextContent());
						instructies.addLast(new Instructie(PID,operatie, adres));
						procesNumbers.add(PID);
					}
				}
				
				
				// alle processen aanmaken met een pagetable
				for(int procesNumber: procesNumbers) {
					processen.put(procesNumber, new Proces(procesNumber));
				}
			} catch (Exception e) {
				e.printStackTrace();
				
				Main.log(Level.SEVERE, "exception: dataprocessing.java lijn 60, pad niet gevonden: memorycontroller lijn 45");
				
			}
		}
}
