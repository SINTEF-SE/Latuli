package geocoding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.opencsv.CSVWriter;

import utilities.StringUtilities;

public class Deduplication {

	public static void main(String[] args) {


		String coordinates = "./files/COORDINATES/coordinates.csv";
		String hubs = "./files/DATASETS/FilteredByColumns/hubreconstructionlocations_allFields.csv";
		String neo4jHubs = "./files/DATASETS/FilteredByColumns/hubreconstructionlocations_neo4j.csv";
		String parties = "./files/DATASETS/FilteredByColumns/parties.csv";
		
		//print simple hubs csv file without coordinates
		printNeo4JHubs (hubs, neo4jHubs);

		//1. find parties that are also in coordinates by their compressed address and get a list of hashcodes
//		Map<String, String> compressedCoordinates = getCompressedCoordinates (coordinates);
//		Map<String, String> compressedParties = getCompressedParties (parties);
//		Map<String, String> equalParties = getEqualParties (compressedCoordinates, compressedParties);
//		List<Hub> neo4JHubs = getHubsWithCoordinates(equalParties, hubs);
//		
//		System.out.println("neo4JHubs contains " + neo4JHubs.size() + " hubs");
//		
//		System.out.println("Printing neo4JHubs: ");
//		
//		for (Hub h : neo4JHubs) {
//			
//			if (h.getAdditionalPartyIdentification() != null) {
//			System.out.println(h.getAdditionalPartyIdentification());
//			System.out.println(h.getCoordinates());
//			System.out.println(h.getLabel());
//			}
//		}

		//2. find hubs that have the same hashcode as in one


		//3. store list of hub objects (as Hub) retrieved in 2 according to Neo4J csv format

	}
	
	public static void printNeo4JHubs (String inputHubsCSV, String outputHubcsCSV) {
		
		BufferedReader br = null;
		
		try {
			br = new BufferedReader(new FileReader(inputHubsCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//write new csv with street-city combination from map
		FileWriter outputFile = null;
		try {
			outputFile = new FileWriter(outputHubcsCSV);
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		CSVWriter writer = new CSVWriter(outputFile);

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Set<String> hubIds = new HashSet<String>();
		
		for (String[] params : line) {

			hubIds.add(params[1]);

		}
		
		for (String s : hubIds) {
			String[] csvLine = {s, "Hub"};
			writer.writeNext(csvLine);
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	//
	public static List<Hub> getHubsWithCoordinates (Map<String, String> equalParties, String hubsCSV) {
		
		List<Hub> hubsWithCoordinates = new ArrayList<Hub>();
		
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(hubsCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
		Hub hub = null;
		String coordinates = null;
		String lat = null;
		String lon = null;

		//iterate hubsCSV, if hashcode is in equalParties, get the coordinates...
		//and add AdditionalPartyIdentitication (params[1], "{latitude:55.6121514, longitude:12.9950357}", Hub
		for (String[] params : line) {

			if (equalParties.containsKey(params[3])) {
				
				System.out.println("equalParties contains " + params[3]);
				coordinates = equalParties.get(params[3]);
				lat = coordinates.substring(0, coordinates.lastIndexOf(","));
				lon = coordinates.substring(coordinates.lastIndexOf(","), coordinates.length());
				
				System.out.println("AdditionalPartyIdentification: " + params[1]);
				System.out.println("Coordinates: " + "{latitude:" + lat + ", longitude:" + lon + "}");
				
			hub = new Hub.HubBuilder()
					.setAdditionalPartyIdentification(params[1])
					.setCoordinates("{latitude:" + lat + ", longitude:" + lon + "}")
					.setLabel("Hub")
					.build();
					
			}
			
			hubsWithCoordinates.add(hub);

		}
		
		System.out.println("hubsWithCoordinates list contains " + hubsWithCoordinates.size() + " hubs");
		
		return hubsWithCoordinates;
		
	}

	public static Map<String, String> getEqualParties (Map<String, String> compressedCoordinates, Map<String, String> compressedParties) {

		Map<String, String> equalPartiesAndCoordinates = new HashMap<String, String>();

		for (Entry<String, String> c : compressedCoordinates.entrySet()) {

			for (Entry<String, String> p : compressedParties.entrySet()) {

				if (c.getKey().equals(p.getKey())) {

					equalPartiesAndCoordinates.put(p.getValue(), c.getValue());

				}
			}

		}

		return equalPartiesAndCoordinates;

	}

	public static Map<String, String> getCompressedCoordinates (String coordinates) {

		Map<String, String> coordinatesMap = new HashMap<String, String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(coordinates));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		for (String[] params : line) {

			coordinatesMap.put(params[0].replaceAll("\\s+","") + "_" + params[1].replaceAll("\\s+","") + "_" + params[2].replaceAll("\\s+",""), params[3]);

		}

		System.out.println("\nPrinting coordinatesMap:");
		for (Entry<String, String> e : coordinatesMap.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}



		return coordinatesMap;

	}

	public static Map<String, String> getCompressedParties (String parties) {

		Map<String, String> coordinatesMap = new HashMap<String, String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(parties));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		for (String[] params : line) {

			coordinatesMap.put(params[5].replaceAll("\\s+","") + "_" + params[8].replaceAll("\\s+","") + "_" + params[7].replaceAll("\\s+",""), params[2]);

		}

		System.out.println("\nPrinting partiesMap:");
		for (Entry<String, String> e : coordinatesMap.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}

		return coordinatesMap;

	}

}
