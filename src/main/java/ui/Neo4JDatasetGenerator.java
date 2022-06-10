package ui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Stopwatch;
import com.opencsv.CSVWriter;

import csvfiltering.CSVProcessor;
import utilities.StringUtilities;

public class Neo4JDatasetGenerator {

	//test method
	public static void main(String[] args) throws IOException {


		Stopwatch stopwatch = Stopwatch.createStarted();
//		Scanner input = new Scanner(System.in);
//
//		System.out.println("Enter start datetime (yyyy-MM-dd'T'HH:mm:ss): "); 
//		String startDateTime = input.nextLine();
//
//		System.out.println("\nEnter end datetime (yyyy-MM-dd'T'HH:mm:ss): "); 
//		String endDateTime = input.nextLine();
//
//		System.out.println("\nEnter existing source folder for CSV files: "); 
//		String csvSource = input.nextLine();
//
//		System.out.println("\nDo your CSV files contain a header (Y or N)?: "); 
//		String header = input.nextLine();
//
//		if (header.equalsIgnoreCase("Y")) {
//			try {
//				System.out.println("Removing the header from all CSV files...");
//				CSVProcessor.removeFirstLineFromFilesInFolder(csvSource);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//
//		System.out.println("\nEnter name of new folder where the generated CSV files will be stored");
//		String csvOutput = input.nextLine();
//
//		input.close(); 
		
		List<Integer> xDocLoadingUnitColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,3,5,6,7,11,12,18,36,37,40,41,42));
		List<Integer> consignmentColumns = new LinkedList<Integer>(Arrays.asList(0,18,23,32,33,34,35,36,40,41));
		List<Integer> shipmentColumns = new LinkedList<Integer>(Arrays.asList(0,3,4,18,19));
		List<Integer> shipmentItemsColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,4));
		List<Integer> loadingUnitColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,3));
		List<Integer> hubReconstructionLocationColumns = new LinkedList<Integer>(Arrays.asList(0,1,2));
		List<Integer> waveColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,3,4,8,9,10,11,12,13,14,15,16,17,18,19,20,21));
		
		String startDateTime = "2019-12-01T10:00:00";
		String endDateTime = "2020-05-01T10:00:00";
		String csvSource = "./files/DATASETS/2019_2022";
		//if there are headers in the csv files
		//System.out.println("Removing headers...");
		//CSVProcessor.removeFirstLineFromFilesInFolder(csvSource);
		String csvOutput = "./files/DATASETS/FilteredByColumns";

		System.out.println("\nThis process may take several minutes to complete...");	  

		String tmpSplitFiles = "tmpSplitFiles/";
		String tmpSplitFilesFiltered = "tmpSplitFilesFiltered/";
		String tmpSplitFilesByColumn = "tmpSplitFilesByColumn/";

		int chunkSize = 50;

		File tmpSplitFilesFolder = new File(tmpSplitFiles);
		File tmpSplitFilesFilteredFolder = new File (tmpSplitFilesFiltered);
		File tmpSplitFilesColumn = new File (tmpSplitFilesByColumn);
		File csvSourceFolder = new File (csvSource);
		File csvOutputFolder = new File (csvOutput);

		if (!csvSourceFolder.exists()) {
			csvSourceFolder.mkdir();
		} if (!tmpSplitFilesFolder.exists()) {
			tmpSplitFilesFolder.mkdir();
		} if (!tmpSplitFilesFilteredFolder.exists()) {
			tmpSplitFilesFilteredFolder.mkdir();
		} if (!tmpSplitFilesColumn.exists()) {
			tmpSplitFilesColumn.mkdir();
		}if (!csvOutputFolder.exists()) {
			csvOutputFolder.mkdir();
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
		
		//filter on columns
		System.out.println("Filtering on columns");
		try {
			CSVProcessor.filterOnColumns(tmpSplitFilesFiltered, tmpSplitFilesByColumn, xDocLoadingUnitColumns, consignmentColumns, 
					shipmentColumns, shipmentItemsColumns, loadingUnitColumns, waveColumns);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		System.out.println("Creating Neo4J dataset");
		createNeo4JDataset(tmpSplitFilesByColumn, csvOutput);


		//remove tmp folders after CSV file generation
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
		
		try {
			deleteFolder(tmpSplitFilesColumn.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		


		stopwatch.stop();

		System.out.println("The generation of CSV files for Neo4J import took: " + stopwatch.elapsed(TimeUnit.MINUTES) + " minutes.");

	}


	public static void createNeo4JDataset (String tmpSplitFilesFiltered, String joinedCSVOutputFolder) throws IOException {

		//process each sub-folder (e.g. consignments_split_filtered) in tmpSplitFilesFiltered
		File parentFolder = new File(tmpSplitFilesFiltered);
		File[]folders = parentFolder.listFiles();
		File joinedCSV = null;
		for (File f : folders) {
			
			System.out.println("Checking file: " + f.getPath());
			if (f.listFiles().length <= 1 && !f.getName().endsWith("_ids")) {
				File[] singleFiles = f.listFiles();
				for (File sf : singleFiles) {
					FileUtils.copyFile(sf, new File(joinedCSVOutputFolder + "/" + sf.getName() + ".csv"));
				}
			} else if (!f.getName().endsWith("_ids")) {
				System.out.println("Processing folder " + f.getName() + " with number of files: " + f.listFiles().length);
				System.out.println("Starting filtering on columns...");

				joinedCSV = CSVProcessor.joinFiles(f, new File(joinedCSVOutputFolder + "/" + f.getName().substring(0,f.getName().indexOf("_")) + ".csv"));

			}
		}

	}

	/**
	 * Delete tmp folders after CSV has been generated.
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

}
