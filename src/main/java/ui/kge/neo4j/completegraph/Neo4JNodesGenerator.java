package ui.kge.neo4j.completegraph;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

import com.google.common.base.Stopwatch;

import csvfiltering.CSVProcessor;
import datavalidation.Validation;

public class Neo4JNodesGenerator {

	//test method
	public static void main(String[] args) throws IOException {


		Stopwatch stopwatch = Stopwatch.createStarted();
		
		List<Integer> xDocLoadingUnitColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,3,5,6,7,11,12,18,36,37,40,41,42));
		List<Integer> consignmentColumns = new LinkedList<Integer>(Arrays.asList(0,18,23,32,33,34,35,36,40,41));
		List<Integer> shipmentColumns = new LinkedList<Integer>(Arrays.asList(0,3,18,19));
		List<Integer> shipmentItemsColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,3,4));
		List<Integer> loadingUnitColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,3));
		List<Integer> waveColumns = new LinkedList<Integer>(Arrays.asList(0,1,2,4,8,9,10,12,13,14,15,16,17,18,19,20,21));
		
		String startDateTime = "2021-01-01T10:00:00";
		String endDateTime = "2022-05-01T10:00:00";
		String csvSource = "./files/DATASETS/2020_2022";
		//if there are headers in the csv files
		System.out.println("Removing headers...");
		CSVProcessor.removeFirstLineFromFilesInFolder(csvSource);
		String csvOutput = "./files/DATASETS/2020_2022_FilteredByColumns";

		System.out.println("\nThis process may take several minutes to complete...");	  

		String tmpSplitFiles = "tmpSplitFiles/";
		String tmpSplitFilesFiltered = "tmpSplitFilesFiltered/";
		String tmpSplitFilesByColumn = "tmpSplitFilesByColumn/";
		String tmpCorrectedDates = "tmpCorrectedDates/";

		int chunkSize = 50;

		File tmpSplitFilesFolder = new File(tmpSplitFiles);
		File tmpSplitFilesFilteredFolder = new File (tmpSplitFilesFiltered);
		File tmpSplitFilesColumn = new File (tmpSplitFilesByColumn);
		File tmpCorrectedDatesFolder = new File (tmpCorrectedDates);
		
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
		} if (!tmpCorrectedDatesFolder.exists()) {
			tmpCorrectedDatesFolder.mkdir();
		} if (!csvOutputFolder.exists()) {
			csvOutputFolder.mkdir();
		} 

		List<File> list = new ArrayList<>();

		try {
			list = CSVProcessor.createFileList(csvSource);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		for (File file : list) {
			System.out.println("Splitting file " + file.getName() + " (" + file.length() / (1024 * 1024) + " MB) into chunks of max size " + chunkSize + " MB...");
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
		System.out.println("Filtering on columns...");
		try {
			CSVProcessor.filterOnColumns(tmpSplitFilesFiltered, tmpSplitFilesByColumn, xDocLoadingUnitColumns, consignmentColumns, 
					shipmentColumns, shipmentItemsColumns, loadingUnitColumns, waveColumns);
		} catch (ParseException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//correct datetime format
		System.out.println("Correcting datetime format...");
		CSVProcessor.formatDates(tmpSplitFilesByColumn, tmpCorrectedDates);

		System.out.println("Creating Neo4J dataset...");
		createNeo4JDataset(tmpCorrectedDates, csvOutput);


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
		
		try {
			deleteFolder(tmpCorrectedDatesFolder.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Validating complete CSV files...");
		String inputConsignmentCSV = csvOutput + "/consignments.csv";
		String inputShipmentsCSV = csvOutput + "/shipments.csv";
		String inputShipmentItemsCSV = csvOutput + "/shipmentitems.csv";
		String inputLoadingUnitsCSV = csvOutput + "/loadingunits.csv";
		String inputXDocLoadingUnitsCSV = csvOutput + "/xdlu.csv";
		String inputWavesCSV = csvOutput + "/waves.csv";
		
		String outputConsignmentCSV = csvOutput + "/consignments_validated.csv";
		String outputShipmentsCSV = csvOutput + "/shipments_validated.csv";
		String outputShipmentItemsCSV = csvOutput + "/shipmentitems_validated.csv";
		String outputLoadingUnitsCSV = csvOutput + "/loadingunits_validated.csv";
		String outputXDocLoadingUnitsCSV = csvOutput + "/xdlu_validated.csv";
		String outputWavesCSV = csvOutput + "/waves_validated.csv";
		
		Validation.validateConsignments(inputConsignmentCSV, outputConsignmentCSV);
		Validation.validateShipments(inputShipmentsCSV, outputShipmentsCSV);	
		Validation.validateShipmentItems(inputShipmentItemsCSV, outputShipmentItemsCSV);	
		Validation.validateLoadingUnits(inputLoadingUnitsCSV, outputLoadingUnitsCSV);	
		Validation.validateXDocLoadingUnits(inputXDocLoadingUnitsCSV, outputXDocLoadingUnitsCSV);
		Validation.validateWaves(inputWavesCSV, outputWavesCSV);		

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
				//if only one file, have the name of the file be filename until '_'
				for (File sf : singleFiles) {
					FileUtils.copyFile(sf, new File(joinedCSVOutputFolder + "/" + sf.getName().substring(0, sf.getName().indexOf("_")) + ".csv"));
//					FileUtils.copyFile(sf, new File(joinedCSVOutputFolder + "/" + sf.getName() + ".csv"));
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
