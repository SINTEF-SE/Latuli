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

import org.apache.commons.io.FileUtils;

import com.google.common.base.Stopwatch;

import csvfiltering.CSVProcessor;

public class Neo4JDatasetGenerator {
	
	//test method
	public static void main(String[] args) throws IOException {
		
		
		Stopwatch stopwatch = Stopwatch.createStarted();

		Scanner input = new Scanner(System.in);

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		System.out.println("\nEnter name of new folder where the generated CSV files will be stored");
		String csvOutput = input.nextLine();

		input.close(); 

		System.out.println("\nThis process may take several minutes to complete...");	  

		String tmpSplitFiles = "tmpSplitFiles/";
		String tmpSplitFilesFiltered = "tmpSplitFilesFiltered/";

		int chunkSize = 50;

		File tmpSplitFilesFolder = new File(tmpSplitFiles);
		File tmpSplitFilesFilteredFolder = new File (tmpSplitFilesFiltered);
		File csvSourceFolder = new File (csvSource);
		File csvOutputFolder = new File (csvOutput);

		if (!csvSourceFolder.exists()) {
			csvSourceFolder.mkdir();
		} if (!tmpSplitFilesFolder.exists()) {
			tmpSplitFilesFolder.mkdir();
		} if (!tmpSplitFilesFilteredFolder.exists()) {
			tmpSplitFilesFilteredFolder.mkdir();
		} if (!csvOutputFolder.exists()) {
			csvOutputFolder.mkdir();
		} 

		List<File> list = new ArrayList<>();

		try {
			list = CSVProcessor.createFileList(csvSource);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		for (File file : list) {
			System.out.println("Splitting file " + file.getName() + " (" + file.length() / (1024 * 1024) + " MB) into chunks of max size " + chunkSize + " MB");
			try {
				CSVProcessor.splitCSV(file.getPath(), tmpSplitFiles, chunkSize);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			System.out.println("Starting filtering on period...");
			CSVProcessor.filterOnPeriod(startDateTime, endDateTime, tmpSplitFiles, tmpSplitFilesFiltered);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		createNeo4JDataset(tmpSplitFilesFiltered, csvOutput);


		//remove tmp folders after CSV file generation
		try {
			deleteFolder(tmpSplitFilesFolder.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			deleteFolder(tmpSplitFilesFilteredFolder.toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
			if (f.listFiles().length <= 1 && !f.getName().endsWith("_ids")) {
				File[] singleFiles = f.listFiles();
				for (File sf : singleFiles) {
					FileUtils.copyFile(sf, new File(joinedCSVOutputFolder + "/" + sf.getName() + ".csv"));
				}
			}
			else if (!f.getName().endsWith("_ids")) {
				System.out.println("Processing folder " + f.getName() + " with number of files: " + f.listFiles().length);
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
