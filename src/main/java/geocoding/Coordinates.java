package geocoding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.opencsv.CSVWriter;

import datavalidation.Party;
import utilities.StringUtilities;

public class Coordinates {
	
	public static void main(String[] args) {
		
		String partyCSV = "./files/DATASETS/ORIGINAL_CSV/parties.csv";

		Map<String, String> addressToCoordinateMap = createAddressToCoordinateMap(partyCSV);

		System.out.println("There are " + addressToCoordinateMap.size() + " entries.");


		String partiesWOCoordinates = "./files/DATASETS/NEW_3M_DATASET/parties.csv";
		String output = "./files/DATASETS/NEW_3M_DATASET/partiesWCoordinates.csv";
		String partiesWithMissingCoordinates = "./files/PartiesWithMissingCoordinates.csv";
		String deduplicatedPartiesWOCoordinatesCSV = "./files/DeduplicatedPartiesWOCoordinates.csv";


		//THE APPROACH IS AS FOLLOWS:
		//1. addCoordinatesToParties(partiesWOCoordinates, addressToCoordinateMap, output);
		//2. printPartiesWithMissingCoordinatesToCSV(output, partiesWithMissingCoordinates);
		//3. deduplicateParties(partiesWithMissingCoordinates, deduplicatedPartiesWOCoordinatesCSV);
		//4. GeoCoder.getCoordinates(using subsets of the csv resulting from deduplicateParties())
	}
	
	
	
	//since there are many duplicate party entries in the Parties DB table, this method first creates a map holding
		//street as key and city_country as value so that only 1 copy of the street remains. Next, the street, city and country
		//are written to csv to be used as input to the geocoding
		public static void deduplicateParties (String partiesWOCoordinatesCSV, String deduplicatedPartiesWOCoordinatesCSV) {	

			//read csv
			File partiesWOCoordinatesFile = new File (partiesWOCoordinatesCSV);

			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(partiesWOCoordinatesFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			List<String[]> line = new ArrayList<String[]>();
			try {
				line = StringUtilities.oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//create street-to-city-and-country map
			Map<String, String> street2CityMap = new HashMap<String, String>();

			for (String[] params : line) {

				street2CityMap.put(params[0].replace(" .", ""), params[1] + "_" + params[2]);

			}

			//write new csv with street-city combination from map
			FileWriter outputFile = null;
			try {
				outputFile = new FileWriter(deduplicatedPartiesWOCoordinatesCSV);
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			CSVWriter writer = new CSVWriter(outputFile);

			for (Entry<String, String> e : street2CityMap.entrySet()) {

				if (!e.getKey().equals("NULL") && !e.getKey().equals("") && !e.getValue().substring(0, e.getValue().indexOf("_")).equals("NULL") && !e.getValue().substring(0, e.getValue().indexOf("_")).equals("")) {

					String[] csvLine = {e.getKey(), e.getValue().substring(0, e.getValue().indexOf("_")), e.getValue().substring(e.getValue().indexOf("_")+1, e.getValue().length())};

					writer.writeNext(csvLine);

				}

			}	


			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}


		//prints (eligible) parties that have no coordinates associated to them to csv
		public static void printPartiesWithMissingCoordinatesToCSV (String partiesWCoordinates, String partiesWithMissingCoordinates) {


			File partiesWCoordinatesFile = new File(partiesWCoordinates);
			List<String[]> line = new ArrayList<String[]>();

			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(partiesWCoordinatesFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			try {
				line = StringUtilities.oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			int counter = 0;
			int totalPartiesCounter = 0;

			FileWriter outputFile = null;
			try {
				outputFile = new FileWriter(partiesWithMissingCoordinates);
			} catch (IOException e2) {
				e2.printStackTrace();
			}


			CSVWriter writer = new CSVWriter(outputFile);


			//System.out.println("StreetAddress,City,Country");

			for (String[] params : line) {

				totalPartiesCounter++;

				if (params[19].equals("nan") && !params[5].equals("NULL") && !params[5].contains("?") && !params[5].equals(" ")) {
					counter++;			

					String[] csvLine = {params[5].replace(" .", ""), params[8], params[7]};

					writer.writeNext(csvLine);

				}	

			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nThere are " + counter + " parties without coordinates out of a total of " + totalPartiesCounter + " parties.");

			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}


		//prints (eligible) parties that have no coordinates associated to them to console
		public static void printPartiesWithMissingCoordinates (String partiesFile) {


			File partiesWCoordinatesFile = new File(partiesFile);
			List<String[]> line = new ArrayList<String[]>();

			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(partiesWCoordinatesFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			try {
				line = StringUtilities.oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			int counter = 0;
			int totalPartiesCounter = 0;

			System.out.println("StreetAddress,City,Country");

			for (String[] params : line) {

				totalPartiesCounter++;

				if (params[19].equals("nan") && !params[5].equals("NULL") && !params[5].equals(" ")) {
					counter++;			

					System.out.println(params[5].replace(" .", "") + "," + params[8] + "," + params[7]);

				}	
			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			System.out.println("\nThere are " + counter + " parties without coordinates out of a total of " + totalPartiesCounter + " parties.");

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		//used to add existing coordinates to new (duplicate) parties added from the M3 Analytics DB
		public static void addCoordinatesToParties (String originalPartyCSVWCoordinates, String partiesWOCoordinates, String outputFilePath) {

			Map<String, String> addressToCoordinateMap = createAddressToCoordinateMap (originalPartyCSVWCoordinates);

			List<String[]> line = new ArrayList<String[]>();
			BufferedReader br = null;

			//read parties wo coordinates into Party from parties.csv 			
			Party p = null;			
			Set<Party> partySet = new HashSet<Party>();
			File partiesWOCoordinatesFile = new File (partiesWOCoordinates);

			try {
				br = new BufferedReader(new FileReader(partiesWOCoordinatesFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			try {
				line = StringUtilities.oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String coords = null;

			for (String[] params : line) {

				//get coordinates for this party from the coordinateMap
				coords = addressToCoordinateMap.get(params[5].replaceAll("\\s+","") + "-" + params[8].replaceAll("\\s+",""));

				//if no coordinates exist for a party, add "nan"
				if (coords == null) {

					coords = "nan";

				}

				p = new Party.PartyBuilder()
						.setAdditionalPartyIdentification(params[0])
						.setGLN(params[1])
						.setHashCode(params[2])
						.setPartyName(params[3])
						.setAddressDetail(params[4])
						.setStreet(params[5])
						.setCode3(params[6])
						.setCode2(params[7])
						.setLocation(params[8])
						.setPostalCode(params[9])
						.setModifiedOn(params[10])
						.setIsHub(params[11])
						.setIsShipper(params[12])
						.setIsCarrier(params[13])
						.setIsConsignor(params[14])
						.setIsReadOnly(params[15])
						.setBarCodeFormat(params[16])
						.setSimplifiedCode(params[17])
						.setOriginalDataSource(params[18])
						.setCoordinates(coords)
						.build();			

				partySet.add(p);

			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			//print parties with coordinates to csv
			File newPartiesCSVWithCoordinatesFile = new File (outputFilePath);

			FileWriter outputFile = null;
			try {
				outputFile = new FileWriter(newPartiesCSVWithCoordinatesFile);
			} catch (IOException e2) {
				e2.printStackTrace();
			}

			CSVWriter writer = new CSVWriter(outputFile);

			for (Party party : partySet) {

				String[] csvLine = {party.getAdditionalPartyIdentification(), party.getGln(), party.getHashCode(),party.getPartyName(),party.getAddressDetail(),
						party.getStreet(),party.getCode3(),party.getCode2(),party.getLocation(),party.getPostalCode(),party.getModifiedOn(),party.getIsHub(), 
						party.getIsShipper(),party.getIsCarrier(),party.getIsConsignor(),party.getIsReadOnly(),
						party.getBarCodeFormat(),party.getSimplifiedCode(),party.getOriginalDataSource(), party.getCoordinates()};

				writer.writeNext(csvLine);


			}

			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		//creates a map that holds street-location as key and coordinates as value (from the original Parties.csv file)
		private static Map<String, String> createAddressToCoordinateMap (String originalPartyCSVWCoordinates) {

			Map<String, String> addressToCoordinateMap = new HashMap<String, String>();		

			File partyFile = new File(originalPartyCSVWCoordinates);
			List<String[]> line = new ArrayList<String[]>();

			BufferedReader br = null;
			try {
				br = new BufferedReader(new FileReader(partyFile));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}


			try {
				line = StringUtilities.oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (String[] params : line) {

				addressToCoordinateMap.put(params[5].replaceAll("\\s+","") + "-" + params[8].replaceAll("\\s+",""), params[19]);

			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			return addressToCoordinateMap;


		}

		public static String formatCoordinates (String coordinates) {
			return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
		}

}
