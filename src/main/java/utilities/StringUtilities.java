package utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLLiteral;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.vocab.OWL2Datatype;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import datavalidation.Party;

public class StringUtilities {
	static OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
	static OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();


	public static void main(String[] args){

		String timeStampOrg = "2019-05-31 05:02:43.3800000";
		String timeStamp = convertToDateTime(timeStampOrg);
		int epoch = convertToEpoch(timeStamp);

		System.out.println(epoch);


	}
	
	public static void addCoordinatesToParties (String partiesWOCoordinates, String partiesWCoordinates, String outputFilePath) {

		//get the hashCode and corresponding coordinates from the partiesWCoordinates file
		Map<String, String> coordinatesMap = new HashMap<String, String>();

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


		for (String[] params : line) {

			coordinatesMap.put(params[2], params[19]);

		}

		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

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
			
			//get coordinates for this party
			coords = coordinatesMap.get(params[2]);

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

	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}

	public static List<String[]> oneByOne(Reader reader) throws Exception {
		List<String[]> list = new ArrayList<>();

		CSVParser parser = new CSVParserBuilder()
				.withSeparator(',')
				.withIgnoreQuotations(false)
				.build();

		CSVReader csvReader = new CSVReaderBuilder(reader)
				.withSkipLines(0)
				.withCSVParser(parser)
				.build();

		String[] line;
		while ((line = csvReader.readNext()) != null) {
			list.add(line);
		}
		reader.close();
		csvReader.close();
		return list;
	}


	public static String convertToDateTime(String input) {	

		if (input.equals("NULL") || input.length() != 27 || !input.startsWith("20")) {
			input = "0000-00-00 00:00:00.0000000";
		}

		String dateTime = input.substring(0, input.lastIndexOf("."));

		dateTime = dateTime.replaceAll(" ", "T");

		return dateTime;

	}

	public static Integer convertToEpoch(String timestamp){

		  if(timestamp == null) return null;
		  try {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		    Date dt = sdf.parse(timestamp);
		    long epoch = dt.getTime();
		    return (int)(epoch/1000);
		  } catch(ParseException e) {
		     return null;
		  }


	}

	/**
	 * Capitalises each word
	 * @param str input string
	 * @return string where each word is capitalised
	   Mar 4, 2020
	 */
	public static String capitaliseWord(String str){  
		String words[]=str.split("\\s");  
		String capitaliseWord="";  
		for(String w:words){  
			String first=w.substring(0,1);  
			String afterfirst=w.substring(1);  
			capitaliseWord+=first.toUpperCase()+afterfirst+" ";  
		}  
		return capitaliseWord.trim();  
	}  

	/**
	 * Returns ONE string by random from a list of strings
	 * @param listOfStrings
	 * @return
	   Jul 2, 2019
	 */
	public static String getRandomString1(List<String> listOfStrings) {
		Random rand = new Random();
		String returnedString = null;

		int numberOfElements = 1;

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(listOfStrings.size());
			returnedString = listOfStrings.get(randomIndex);

		}

		return returnedString;
	}

	/**
	 * Returns THREE string by random from a list of strings
	 * @param listOfStrings
	 * @return
	   Jul 2, 2019
	 */
	public static Set<String> getRandomString3(List<String> listOfStrings) {
		Random rand = new Random();
		Set<String> returnedStrings = new HashSet<String>();

		int numberOfElements = 3;

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(listOfStrings.size());
			returnedStrings.add(listOfStrings.get(randomIndex));
		}

		return returnedStrings;
	}

	/**
	 * Returns ONE integer by random from a list of integers
	 * @param list
	 * @return
	   Jul 2, 2019
	 */
	public static int getRandomInt1(List<Integer> list) {
		Random rand = new Random();
		int returnedString = 0;

		int numberOfElements = 1;

		for (int i = 0; i < numberOfElements; i++) {
			int randomIndex = rand.nextInt(list.size());
			returnedString = list.get(randomIndex);

		}

		return returnedString;
	}

	public static String splitCompounds(String input) {
		String[] compounds = input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
		StringBuilder sb = new StringBuilder();

		for (String compound : compounds) {
			sb.append(compound).append(" ");
		}
		return sb.toString();
	}

	public static void fixAlignmentName(String folder) throws IOException {
		File allFiles = new File(folder);
		File[] files = allFiles.listFiles();
		System.err.println("Number of files: " + Objects.requireNonNull(files).length);

		String fileName = null;
		File newFile = null;

		for (File file : files) {
			fileName = file.getName();
			file.renameTo(new File(fileName = "./files/DBLP-Scholar/alignments/new/" + fileName.replaceAll("[^a-zA-Z0-9.-]", "_")));
		}
	}


	/**
	 * Takes a string as input and returns an arraylist of tokens from this string
	 *
	 * @param s:         the input string to tokenize
	 * @param lowercase: if the output tokens should be lowercased
	 * @return an ArrayList of tokens
	 */
	public static ArrayList<String> tokenize(String s, boolean lowercase) {
		if (s == null) {
			return null;
		}

		ArrayList<String> strings = new ArrayList<>();
		performTokinization(s, lowercase, strings);
		return strings;
	}

	private static void performTokinization(String s, boolean lowercase, Collection<String> strings) {
		String current = "";
		Character prevC = 'x';
		for (Character c : s.toCharArray()) {
			if ((Character.isLowerCase(prevC) && Character.isUpperCase(c)) ||
					c == '_' || c == '-' || c == ' ' || c == '/' || c == '\\' || c == '>') {
				current = current.trim();
				if (current.length() > 0) {
					if (lowercase)
						strings.add(current.toLowerCase());
					else
						strings.add(current);
				}
				current = "";
			}

			if (c != '_' && c != '-' && c != '/' && c != '\\' && c != '>') {
				current += c;
				prevC = c;
			}
		}

		current = current.trim();
		if (current.length() > 0) {
			// this check is to handle the id numbers in YAGO
			if (!(current.length() > 4 && Character.isDigit(current.charAt(0)) &&
					Character.isDigit(current.charAt(current.length() - 1)))) {
				strings.add(current.toLowerCase());
			}
		}
	}

	/**
	 * Takes a string as input and returns set of tokens from this string
	 *
	 * @param s:         the input string to tokenize
	 * @param lowercase: if the output tokens should be lowercased
	 * @return a set of tokens
	 */
	public static Set<String> tokenizeToSet(String s, boolean lowercase){
		if (s == null) {
			return null;
		}

		String stringWOStopWords = removeStopWords(s);
		Set<String> strings = new HashSet<>();
		performTokinization(stringWOStopWords, lowercase, strings);
		return strings;
	}


	/**
	 * Returns a string of tokens
	 *
	 * @param s:         the input string to be tokenized
	 * @param lowercase: whether the output tokens should be in lowercase
	 * @return a string of tokens from the input string
	 */
	public static String stringTokenize(String s, boolean lowercase) {
		StringBuilder result = new StringBuilder();
		ArrayList<String> tokens = tokenize(s, lowercase);
		for (String token : tokens) {
			result.append(token).append(" ");
		}
		return result.toString().trim();
	}


	/**
	 * Removes prefix from property names (e.g. hasCar is transformed to car)
	 *
	 * @param s: the input property name to be
	 * @return a string without any prefix
	 */
	public static String stripPrefix(String s) {
		if (s.startsWith("has")) {
			s = s.replaceAll("^has", "");
		} else if (s.startsWith("is")) {
			s = s.replaceAll("^is", "");
		} else if (s.startsWith("is_a_")) {
			s = s.replaceAll("^is_a_", "");
		} else if (s.startsWith("has_a_")) {
			s = s.replaceAll("^has_a_", "");
		} else if (s.startsWith("was_a_")) {
			s = s.replaceAll("^was_a_", "");
		} else if (s.endsWith("By")) {
			s = s.replaceAll("By", "");
		} else if (s.endsWith("_by")) {
			s = s.replaceAll("_by^", "");
		} else if (s.endsWith("_in")) {
			s = s.replaceAll("_in^", "");
		} else if (s.endsWith("_at")) {
			s = s.replaceAll("_at^", "");
		}
		s = s.replaceAll("_", " ");
		s = stringTokenize(s, true);

		return s;
	}

	public static String removeSymbols(String s) {
		s = s.replace("\"", "");
		s = s.replace(".", "");
		s = s.replace("@en", "");

		return s;
	}

	/**
	 * Takes a filename as input and removes the IRI prefix from this file
	 *
	 * @param fileName
	 * @return filename - without IRI
	 */
	public static String stripPath(String fileName) {
		return fileName.substring(fileName.lastIndexOf("/") + 1);

	}


	/**
	 * Returns the label from on ontology concept without any prefix
	 *
	 * @param label: an input label with a prefix (e.g. an IRI prefix)
	 * @return a label without any prefix
	 */
	public static String getString(String label) {
		if (label.contains("#")) {
			label = label.substring(label.indexOf('#') + 1);
			return label;
		}

		if (label.contains("/")) {
			label = label.substring(label.lastIndexOf('/') + 1);
			return label;
		}
		return label;
	}


	/**
	 * Removes underscores from a string (replaces underscores with "no space")
	 *
	 * @param input: string with an underscore
	 * @return string without any underscores
	 */
	public static String replaceUnderscore(String input) {
		String newString = null;
		Pattern p = Pattern.compile("_([a-zA-Z])");
		Matcher m = p.matcher(input);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			m.appendReplacement(sb, m.group(1).toUpperCase());
		}

		m.appendTail(sb);
		newString = sb.toString();

		return newString;
	}

	/**
	 * Checks if an input string is an abbreviation (by checking if there are two consecutive uppercased letters in the string)
	 *
	 * @param s input string
	 * @return boolean stating whether the input string represents an abbreviation
	 */
	public static boolean isAbbreviation(String s) {

		boolean isAbbreviation = false;
		int counter = 0;
		for (int i = 0; i < s.length(); i++) {
			if (Character.isUpperCase(s.charAt(i))) {
				counter++;
			}
			isAbbreviation = counter > 2;
		}
		return isAbbreviation;
	}

	/**
	 * Returns the names of the ontology from the full file path (including owl or rdf suffixes)
	 *
	 * @param ontology name without suffix
	 * @return
	 */
	public static String stripOntologyName(String fileName) {
		String trimmedPath = fileName.substring(fileName.lastIndexOf("/") + 1);
		String owl = ".owl";
		String rdf = ".rdf";
		String stripped = null;

		if (fileName.endsWith(".owl")) {
			stripped = trimmedPath.substring(0, trimmedPath.indexOf(owl));
		} else {
			stripped = trimmedPath.substring(0, trimmedPath.indexOf(rdf));
		}

		return stripped;
	}

	/**
	 * Returns the full IRI of an input ontology
	 *
	 * @param o the input OWLOntology
	 * @return the IRI of an OWLOntology
	 */
	public static String getOntologyIRI(OWLOntology o) {
		return o.getOntologyID().getOntologyIRI().toString();
	}

	/**
	 * Convert from a filename to a file URL.
	 */
	public static String convertToFileURL(String filename) {
		String path = new File(filename).getAbsolutePath();
		if (File.separatorChar != '/') {
			path = path.replace(File.separatorChar, '/');
		}

		if (!path.startsWith("/")) {
			path = "/" + path;
		}
		return "file:" + path;
	}

	public static String validateRelationType(String relType) {
		if (relType.equals("<")) {
			relType = "&lt;";
		}
		return relType;
	}

	public static String removeStopWords (String inputString) {

		List<String> stopWordsList = Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", 
				"he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", 
				"which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having",
				"do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", 
				"about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", 
				"off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", 
				"more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just",
				"don", "should", "now");

		String[] words = inputString.split(" ");
		ArrayList<String> wordsList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();

		for(String word : words)
		{
			String wordCompare = word.toLowerCase();
			if(!stopWordsList.contains(wordCompare))
			{
				wordsList.add(word);
			}
		}

		for (String str : wordsList){
			sb.append(str + " ");
		}

		return sb.toString();
	}

	/**
	 * Takes as input a String and produces an array of Strings from this String
	 *
	 * @param s
	 * @return result
	 */
	public static String[] split(String s) {
		return s.split(" ");
	}

	public static String[] getCompoundParts(String input) {

		return input.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])");
	}


	public static boolean isCompoundWord(String input) {
		String[] compounds = getCompoundParts(input);
		return compounds.length > 1 && !StringUtils.isAllUpperCase(input);

	}

	public static String splitCompoundString(String input) {
		StringBuilder splitCompound = new StringBuilder();
		String[] compounds = getCompoundParts(input);
		for (String compound : compounds) {
			splitCompound.append(" ").append(compound);
		}

		return splitCompound.toString();

	}

	public static String getCompoundWordWithSpaces(String s) {
		StringBuilder sb = new StringBuilder();
		ArrayList<String> compoundWordsList = getWordsFromCompound(s);
		for (String word : compoundWordsList) {
			sb.append(word).append(" ");
		}
		return sb.toString();
	}

	public static String getCompoundHead(String input) {
		if (isCompoundWord(input)) {
			String[] compounds = getCompoundParts(input);
			return compounds[compounds.length - 1];
		} else {
			return null;
		}
	}

	public static String getCompoundQualifier(String input) {
		String[] compounds = getCompoundParts(input);
		return compounds[0];
	}

	public static String getCompoundModifier(String s) {
		return s.replace(Objects.requireNonNull(getCompoundHead(s)), "");
	}

	public static ArrayList<String> getWordsFromCompound(String input) {
		String[] compounds = getCompoundParts(input);
		return new ArrayList<>(Arrays.asList(compounds));

	}

	public static Set<String> getWordsAsSetFromCompound(String input) {
		String[] compounds = getCompoundParts(input);
		return new HashSet<>(Arrays.asList(compounds));

	}

	public static int countCharsInString(String s) {
		int counter = 0;
		for (int i = 0; i < s.length(); i++) {
			if (Character.isLetter(s.charAt(i)))
				counter++;
		}
		return counter;
	}

	/**
	 * prints each (string) item in a set of items
	 * @param certifications
	 * @return sequenced string of certifications separated by commas
   	   Oct 12, 2019
	 */
	public static String printSetItems(Set<String> set) {
		StringBuffer sb = new StringBuffer();

		if (!set.isEmpty()) {

			for (String s : set) {
				sb.append(s + ", ");
			}

			String setItem = sb.deleteCharAt(sb.lastIndexOf(",")).toString();

			return setItem;
		} else {
			return null;
		}

	}

	public static String removeWhiteSpace (String input) {

		String output = input.replaceAll("\\s+", "_");

		return output;
	}


}
