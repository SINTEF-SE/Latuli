package ui;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;
import csvfiltering.CSVProcessor;

public class KGGeneratorSimple {

	public static void main(String[] args) {

		Stopwatch stopwatch = Stopwatch.createStarted();

		Scanner input = new Scanner(System.in);

		System.out.println("Would you like the knowledge graph as TSV (1) or N-Triples (2)?");
		String fileFormat = input.nextLine();

		System.out.println("Enter start datetime (yyyy-MM-dd'T'HH:mm:ss): "); 
		String startDateTime = input.nextLine();

		System.out.println("\nEnter end datetime (yyyy-MM-dd'T'HH:mm:ss): "); 
		String endDateTime = input.nextLine();

		System.out.println("\nEnter existing source folder for CSV files: "); 
		String csvSource = input.nextLine();
		
		System.out.println("\nDo your CSV files contain a header (Y or N)?: "); 
		String header = input.nextLine();
		
		if (header.equalsIgnoreCase("Y")) {
			try {
				System.out.println("Removing the header from all CSV files...");
				CSVProcessor.removeFirstLineFromFilesInFolder(csvSource);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		System.out.println("\nEnter name of new folder where the generated knowledge graph will be stored:");
		String kg = input.nextLine();

		input.close(); 

		System.out.println("\nThis process may take several minutes to complete...");	  

		String tmpSplitFiles = "tmpSplitFiles/";
		String tmpSplitFilesFiltered = "tmpSplitFilesFiltered/";

		int chunkSize = 50;

		File tmpSplitFilesFolder = new File(tmpSplitFiles);
		File tmpSplitFilesFilteredFolder = new File (tmpSplitFilesFiltered);
		File csvSourceFolder = new File (csvSource);
		File kgFolder = new File (kg);

		if (!csvSourceFolder.exists()) {
			csvSourceFolder.mkdir();
		} if (!tmpSplitFilesFolder.exists()) {
			tmpSplitFilesFolder.mkdir();
		} if (!tmpSplitFilesFilteredFolder.exists()) {
			tmpSplitFilesFilteredFolder.mkdir();
		} if (!kgFolder.exists()) {
			kgFolder.mkdir();
		} 

		List<File> list = new ArrayList<>();

		try {
			list = CSVProcessor.createFileList(csvSource);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (File file : list) {
			System.out.println("Splitting file " + file.getName() + " (" + file.length() / (1024 * 1024) + " MB) into chunks of max size " + chunkSize + " MB");
			try {
				CSVProcessor.splitCSV(file.getPath(), tmpSplitFiles, chunkSize);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		try {
			System.out.println("Starting filtering on period...");
			CSVProcessor.filterOnPeriod(startDateTime, endDateTime, tmpSplitFiles, tmpSplitFilesFiltered);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		if (fileFormat.equals("1")) {
			System.out.println("Creating the knowledge graph and storing the KG file as " + kg + "/KG.tsv");
			createFullDatasetToTSV(tmpSplitFilesFiltered, kg + "/KG_simple.tsv");
		} else {
			System.out.println("Creating the knowledge graph and storing the KG file as " + kg + "/KG.nt");
			createFullDatasetToNTriples(tmpSplitFilesFiltered, kg + "/KG_simple.nt");	
		}

		//remove tmp folders after KG file generation
		try {
			deleteFolder(tmpSplitFilesFolder.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			deleteFolder(tmpSplitFilesFilteredFolder.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

		stopwatch.stop();
		System.out.println("The knowledge graph generation process took: " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes.");

	}
	


	/**
	 * Creates a dataset from csv data and represents the KG as TSV
	 * @param tmpSplitFilesFilteredFolder
	 * @param outputFile
	   15. jan. 2022
	 */
	public static void createFullDatasetToTSV (String tmpSplitFilesFiltered, String outputFile) {

		//access all files in folder
		File parentFolder = new File(tmpSplitFilesFiltered);
		File[] files = parentFolder.listFiles();

		for (File folder : files) {
			
			if (folder.getName().startsWith("hubreconstructionlocations")) {

				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				csv2KGSimple.HubReconstructionLocations.processHubReconstructionLocationsToTSV (folder, outputFile);
			} else if (folder.getName().startsWith("xdlu")) {		

				System.out.println("\nProcessing XDocLoadingUnits\n");

				csv2KGSimple.XDocLoadingUnits.processXDocLoadingUnitsToTSV (folder, outputFile);

			} else if (folder.getName().startsWith("consignments")) {

				System.out.println("\nProcessing Consignments\n");

				csv2KGSimple.Consignments.processConsignmentsToTSV (folder, outputFile);
			}

			else if (folder.getName().startsWith("parties")) {

				System.out.println("\nProcessing Parties\n");

				csv2KGSimple.Parties.processPartiesToTSV(folder, outputFile);

			} else if (folder.getName().startsWith("waves")) {

				System.out.println("\nProcessing Waves\n");

				csv2KGSimple.Waves.processWavesToTSV (folder, outputFile);

			} 
		}
	}


	/**
	 * Creates a dataset from csv data and represents the KG as N-Triples
	 * @param folderPath
	 * @param outputFile
	   15. jan. 2022
	 */
	public static void createFullDatasetToNTriples (String folderPath, String outputFile) {

		//access all files in folder
		File parentFolder = new File(folderPath);
		File[] files = parentFolder.listFiles();

		for (File folder : files) {
			
			if (folder.getName().startsWith("hubreconstructionlocations")) {

				System.out.println("\nProcessing Hub Reconstruction Locations\n");

				csv2KGSimple.HubReconstructionLocations.processHubReconstructionLocationsToNTriple (folder, outputFile);
			} else if (folder.getName().startsWith("xdlu")) {		

				System.out.println("\nProcessing XDocLoadingUnits\n");

				csv2KGSimple.XDocLoadingUnits.processXDocLoadingUnitsToNTriple (folder, outputFile);

			} else if (folder.getName().startsWith("consignments")) {

				System.out.println("\nProcessing Consignments\n");

				csv2KGSimple.Consignments.processConsignmentsToNTriple (folder, outputFile);
			}

			else if (folder.getName().startsWith("parties")) {

				System.out.println("\nProcessing Parties\n");

				csv2KGSimple.Parties.processPartiesToNTriple(folder, outputFile);

			} else if (folder.getName().startsWith("waves")) {

				System.out.println("\nProcessing Waves\n");

				csv2KGSimple.Waves.processWavesToNTriple (folder, outputFile);

			} 

		}

	}
	
	/**
	 * Delete tmp folders after KG has been generated.
	 * @param path
	 * @throws IOException
	   2. mai 2022
	 */
	private static void deleteFolder(Path path) throws IOException {
		if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
			try (DirectoryStream<Path> entries = Files.newDirectoryStream(path)) {
				for (Path entry : entries) {
					deleteFolder(entry);
				}
			}
		}
		Files.delete(path);
	}



	public static String formatCoordinates (String coordinates) {
		return "POINT(" + coordinates.substring(0, coordinates.indexOf(",")) + " " + coordinates.substring(coordinates.indexOf(",")+1, coordinates.length()) + ")";
	}

}
