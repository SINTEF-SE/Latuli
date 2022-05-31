package geocoding;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;

import utilities.StringUtilities;


public class GeoCoder {

	private static final String GEOCODING_RESOURCE = "https://geocode.search.hereapi.com/v1/geocode";
	private static final String API_KEY = "INSERT_API_KEY";

	public static void main(String[] args) throws IOException, InterruptedException {

		String addresses = "./files/addresses_0_20.csv";
		String output = "./files/coordinates_0_20.csv";
		getCoordinates(addresses, output);

	}
	
	public static void getCoordinates (String addresses, String output) throws IOException, InterruptedException {
		
		ObjectMapper mapper = new ObjectMapper();
		GeoCoder geocoder = new GeoCoder();

		String response = null;

		//read addresses from csv file one by one and get their results
		File addressFile = new File (addresses);

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(addressFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}


		List<String[]> line = new ArrayList<String[]>();
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}

		Set<Location> locationSet = new HashSet<Location>();
		
		
		for (String[] params : line) {

			response = geocoder.GeocodeSync(params[0] + "," + params[1] + "," + params[2]);
			JsonNode responseJsonNode = mapper.readTree(response);
			JsonNode items = responseJsonNode.get("items");
			
			Location loc = null;
		
			for (JsonNode item : items) {
				JsonNode address = item.get("address");
				//String label = address.get("label").asText();
				JsonNode position = item.get("position");

				String lat = position.get("lat").asText();
				String lng = position.get("lng").asText();
				
				loc = new Location.LocationBuilder()
						.setStreet(params[0])
						.setCity(params[1])
						.setCountry(params[2])
						.setCoordinates(lat + "," + lng)
						.build();
				
				locationSet.add(loc);
			}
		
		}
		
		//print locations to csv
		FileWriter outputFile = null;
		try {
			outputFile = new FileWriter(output);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		
		CSVWriter writer = new CSVWriter(outputFile);

		for (Location loc : locationSet) {
			

				String[] csvLine = {loc.getStreet(), loc.getCity(), loc.getCountry(), loc.getCoordinates()};

				writer.writeNext(csvLine);

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

	public String GeocodeSync(String query) throws IOException, InterruptedException {

		HttpClient httpClient = HttpClient.newHttpClient();

		String encodedQuery = URLEncoder.encode(query,"UTF-8");
		String requestUri = GEOCODING_RESOURCE + "?apiKey=" + API_KEY + "&q=" + encodedQuery;

		HttpRequest geocodingRequest = HttpRequest.newBuilder().GET().uri(URI.create(requestUri))
				.timeout(Duration.ofMillis(2000)).build();

		HttpResponse geocodingResponse = httpClient.send(geocodingRequest,
				HttpResponse.BodyHandlers.ofString());

		System.out.println("Response: " + geocodingResponse);

		return (String) geocodingResponse.body();
	}

}