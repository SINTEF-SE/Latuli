package geocoding;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.Objects;

import com.opencsv.CSVWriter;

import datavalidation.Party;
import utilities.StringUtilities;

public class Coordinates {

	public static void main(String[] args) {

		//		String partyCSV = "./files/DATASETS/ORIGINAL_CSV/parties.csv";
		//
		//		Map<String, String> addressToCoordinateMap = createAddressToCoordinateMap(partyCSV);
		//
		//		System.out.println("There are " + addressToCoordinateMap.size() + " entries.");
		//
		//
		//		String partiesWOCoordinates = "./files/DATASETS/NEW_3M_DATASET/parties.csv";
		//		String output = "./files/DATASETS/NEW_3M_DATASET/partiesWCoordinates.csv";
		//		String partiesWithMissingCoordinates = "./files/PartiesWithMissingCoordinates.csv";
		//		String deduplicatedPartiesWOCoordinatesCSV = "./files/DeduplicatedPartiesWOCoordinates.csv";


		//THE APPROACH IS AS FOLLOWS:
		//1. addCoordinatesToParties(partiesWOCoordinates, addressToCoordinateMap, output);
		//2. printPartiesWithMissingCoordinatesToCSV(output, partiesWithMissingCoordinates);
		//3. deduplicateParties(partiesWithMissingCoordinates, deduplicatedPartiesWOCoordinatesCSV);
		//4. GeoCoder.getCoordinates(using subsets of the csv resulting from deduplicateParties())



		//1. Create a Map<String, String> that holds key: <street, city, country code> without whitespace, value: <coordinates>.

		String hubReconstructionLocationsCSV = "./files/DATASETS/FilteredByColumns/hubreconstructionlocations_allFields.csv";
		String partiesCSV = "./files/DATASETS/FilteredByColumns/parties.csv";
		String coordinatesCSV = "./files/COORDINATES/coordinates.csv";

		Map<String, String> hubsMap = getHubs2HashCodeMap(hubReconstructionLocationsCSV);

		Map<String, String> hub2AddressMap = getHub2AddressMap (hubsMap, partiesCSV);

		Map<String, String> coordinatesMap = getCoordinatesMap (coordinatesCSV);

		int counter = 0;
		for (Entry<String, String> c : coordinatesMap.entrySet()) {

			for (Entry<String, String> h : hub2AddressMap.entrySet()) {

				if (c.getKey().equals(h.getKey())) {

					counter++;

					System.out.println("coordinatesMap key " + c.getKey() + " matches hub2AddressMap key " + h.getKey());
				}

			}


		}

		System.out.println("There are " + counter + " similar keys in coordinatesMap and hub2AddressMap");


		//createHubCSV (hub2AddressMap, coordinatesCSV);




	}

	public static void getSimpleHubReconstructionLocations (String hubReconstructionLocations, String outputFile) {

		BufferedReader br = null;
		BufferedWriter bw = null;

		try {
			br = new BufferedReader(new FileReader(hubReconstructionLocations));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		try {
			bw = new BufferedWriter(new FileWriter(outputFile));
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<String> hubIds = new HashSet<String>();

		for (String[] params : line) {

			hubIds.add(params[1]);

		}


		for (String s : hubIds) {



		}



	}

	public static Map<String, String> getCoordinatesMap (String coordinatesCSV) {

		Map<String, String> coordinatesMap = new HashMap<String, String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(coordinatesCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}


		for (String[] params : line) {

			coordinatesMap.put(params[0].replaceAll("\\s+","") + "_" + params[1].replaceAll("\\s+","") + "_" + params[2].replaceAll("\\s+",""), params[3]);

		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return coordinatesMap;

	}

	public static void createHubCSV (Map<String, String> hub2AddressMap, String coordinatesCSV) {

		Map<String, String> coordinatesMap = new HashMap<String, String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(coordinatesCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}


		for (String[] params : line) {

			coordinatesMap.put(params[0].replaceAll("\\s+","") + "_" + params[1].replaceAll("\\s+","") + "_" + params[2].replaceAll("\\s+",""), params[3]);

		}

		//		System.out.println("\nPrinting coordinatesMap");
		//		for (Entry<String, String> e : coordinatesMap.entrySet()) {
		//			System.out.println(e.getKey() + " : " + e.getValue());
		//		}

		List<Hub> hubsList = new ArrayList<Hub>();
		Hub hub = null;
		String coordinates = null;
		for (Entry<String, String> e : hub2AddressMap.entrySet()) {

			if (coordinatesMap.containsKey(e.getKey())) {
				coordinates = coordinatesMap.get(e.getKey());
				System.out.println("Coordinates for " + e.getKey() + " are: " + coordinates);
				hub = new Hub.HubBuilder()
						.setAdditionalPartyIdentification(e.getValue())
						.setAddressCode(e.getKey())
						.setLatitude(coordinatesMap.get(e.getKey().substring(0, coordinatesMap.get(e.getKey()).lastIndexOf(","))))
						.setLongitude(coordinatesMap.get(e.getKey().substring(coordinatesMap.get(e.getKey()).lastIndexOf(","), coordinatesMap.get(e.getKey()).length())))
						.setLabel("Hub")
						.build();
			}

			hubsList.add(hub);

		}

		System.out.println("Printing hub instances:");
		for (Hub h : hubsList) {
			System.out.println("additionalPartyIdentification: " + h.getAdditionalPartyIdentification());
			System.out.println("addressCode: " + h.getAddressCode());
			System.out.println("latitude: " + h.getLatitude());
			System.out.println("longitude: " + h.getLongitude());
			System.out.println("label: " + h.getLabel());
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



	}

	//output should be in format C042,"{latitude:55.6121514, longitude:12.9950357}", Hub
	//column 11 in parties.csv is isHub, if hub then value is 1 (0 if not)
	public static void addCoordinatesToHubs (String coordinates, String initialHubCSV, String parties, String outputHubCSV) {

		File coordinatesFile = new File (coordinates);
		File initialHubCSVFile = new File (initialHubCSV);
		File partiesFile = new File (parties);
		File outputHubCSVFile = new File (outputHubCSV);

		//get all parties that are hub from parties file


	}

	private static Map<String, String> getHubs2HashCodeMap (String inputHubReconstructionLocationsCSV) {


		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputHubReconstructionLocationsCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<String> additionalPartyIds = new HashSet<String>();

		for (String[] params : line) {

			additionalPartyIds.add(params[1]);

		}

		System.out.println("There are " + additionalPartyIds.size() + " hubs with unique additionalPartyIdentification");

		Map<String, String> additionPartyId2HashCodeMap = new HashMap<String, String>();

		for (String s : additionalPartyIds) {
			System.out.println(s);
		}

		//map additionaPartyIdentification and hashcode
		for (String[] params : line) {

			if (additionalPartyIds.contains(params[1])) {

				additionPartyId2HashCodeMap.put(params[3], params[1]);

			}

		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("There are " + additionPartyId2HashCodeMap.size() + " mappings.");

		return additionPartyId2HashCodeMap;

	}



	private static Map<String, String> getHub2AddressMap (Map<String, String> hub2HashCodeMap, String inputPartiesCSV) {


		Map<String, String> hub2AddressMap = new HashMap<String, String>();


		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(inputPartiesCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String address = null;

		for (String[] params : line) {

			if (hub2HashCodeMap.containsKey(params[2])) {
				address = params[5].replaceAll("\\s+","") + "_" + params[8].replaceAll("\\s+","") + "_" + params[7].replaceAll("\\s+","");
				hub2AddressMap.put(address, hub2HashCodeMap.get(params[2]));
			}

		}

		System.out.println("hub2AddressMap contains the following entries:");
		for (Entry<String, String> e : hub2AddressMap.entrySet()) {

			System.out.println(e.getKey() + ": " + e.getValue());
		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return hub2AddressMap;

	}

	public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
		for (Entry<T, E> entry : map.entrySet()) {
			if (Objects.equals(value, entry.getValue())) {
				return entry.getKey();
			}
		}
		return null;
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
