package csvfiltering;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;

import utilities.StringUtilities;

public class CSVProcessor {
	
	public static void main(String[] args) throws IOException {
		
		//private static void reformatShipmentItems (String inputFile, String outputFile) {
		String inputFile = "./files/DATASETS/Sample_3M/shipmentitems2.csv";
		reformatShipmentItems (inputFile);
		
	}
	
	/**
	 * Checks if a value is a date
	 * @param input
	 * @return
	   Aug 30, 2023
	 */
	private static boolean isDate (String input) {
		
		if (input.startsWith("2019") || input.startsWith("2020") || input.startsWith("2021") || input.startsWith("2022")) {
			
			return true;
			
		} else {
			
			return false;
		}
		
	}
	
	/**
	 * Convert a date format to another date format
	 * @param date
	 * @return
	   Aug 30, 2023
	 */
	private static String convertDate (String date) {
		
		return date = StringUtilities.convertToShortenedDateTime(date);
	 					
	}
	
	/**
	 * Corrects a date value to appropriate format
	 * @param csvFileIn
	 * @param csvFileOut
	   Aug 30, 2023
	 */
	public static void correctDateTime (String csvFileIn, String csvFileOut) {
		
		BufferedReader br = null;
		
        FileWriter outputfile = null;
		try {
			outputfile = new FileWriter(new File(csvFileOut));
		} catch (IOException e2) {
			e2.printStackTrace();
		}
  
        CSVWriter writer = new CSVWriter(outputfile);

		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileIn), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
				
		String line;
		String[] params = null;

		try {
			while ((line = br.readLine()) != null) {
				
				params = line.split(",");
				List<String> list = new LinkedList<String>();
				
				for (int i = 0; i < params.length; i++) {
					
					if (isDate(params[i])) {
						list.add(convertDate(params[i]));
					} else {
						list.add(params[i]);
					}
				}
				
				String[]copy = list.toArray(new String[list.size()]);
				writer.writeNext(copy);
				
			}
		} catch (IOException e1) {
			e1.printStackTrace();
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
	
	/**
	 * Format the dates for all files in an input folder and outputs corrected files in an output folder
	 * @param inputFolder
	 * @param outputFolder
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static void formatDates (String inputFolder, String outputFolder) throws IOException {	
		
		createFolders(outputFolder);
		
		String xdlu_folder_in = inputFolder + "xdlu_split_filtered";
		String consignments_folder_in = inputFolder + "consignments_split_filtered";
		String loadingUnits_folder_in = inputFolder + "loadingunits_split_filtered";
		String waves_folder_in = inputFolder + "waves_split_filtered";
		String shipmentItems_folder_in = inputFolder + "shipmentitems_split_filtered";
		String shipments_folder_in = inputFolder + "shipments_split_filtered";
		String hub_folder_in = inputFolder + "hubreconstructionlocations_split_filtered";

		String xdlu_folder_out = outputFolder + "xdlu_split_filtered";
		String consignments_folder_filtered = outputFolder + "consignments_split_filtered";
		String loadingUnits_folder_filtered = outputFolder + "loadingunits_split_filtered";
		String waves_folder_filtered = outputFolder + "waves_split_filtered";
		String shipmentItems_folder_filtered = outputFolder + "shipmentitems_split_filtered";
		String shipments_folder_filtered = outputFolder + "shipments_split_filtered";
		String hub_folder_filtered = outputFolder + "hubreconstructionlocations_split_filtered";
		
		formatDatesInXDocLoadingUnits (xdlu_folder_in, xdlu_folder_out);		
		formatDatesInConsignments(consignments_folder_in, consignments_folder_filtered);		
		formatDatesInLoadingUnits(loadingUnits_folder_in, loadingUnits_folder_filtered);		
		formatDatesInWaves(waves_folder_in, waves_folder_filtered);			
		formatDatesInShipmentItems(shipmentItems_folder_in, shipmentItems_folder_filtered);			
		formatDatesInShipments(shipments_folder_in, shipments_folder_filtered);
		formatDatesInHubs(hub_folder_in, hub_folder_filtered);	
			
	}
	
	private static void formatDatesInHubs(String inputFolder, String filteredFolder) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			
			correctDateTime(f.getPath(), f.getPath().replace(inputFolder, filteredFolder));
		}
	}
	
	private static void formatDatesInXDocLoadingUnits(String inputFolder, String filteredFolder) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			
			correctDateTime(f.getPath(), f.getPath().replace(inputFolder, filteredFolder));
		}
	}
	
	private static void formatDatesInConsignments(String inputFolder, String filteredFolder) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			correctDateTime(f.getPath(), f.getPath().replace(inputFolder, filteredFolder));
		}
	}
	private static void formatDatesInLoadingUnits(String inputFolder, String filteredFolder) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			correctDateTime(f.getPath(), f.getPath().replace(inputFolder, filteredFolder));
		}
	}
	private static void formatDatesInWaves(String inputFolder, String filteredFolder) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			correctDateTime(f.getPath(), f.getPath().replace(inputFolder, filteredFolder));
		}
	}
	private static void formatDatesInShipmentItems(String inputFolder, String filteredFolder) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			correctDateTime(f.getPath(), f.getPath().replace(inputFolder, filteredFolder));
		}
	}
	private static void formatDatesInShipments(String inputFolder, String filteredFolder) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			correctDateTime(f.getPath(), f.getPath().replace(inputFolder, filteredFolder));
		}
	}
	
	/**
	 * Keeps only the desired columns from the CSV and outputs them to the included folder (outputFolder)
	 * @param inputFolder
	 * @param outputFolder
	 * @param xdlu_columns
	 * @param consignment_columns
	 * @param shipment_columns
	 * @param shipmentItems_columns
	 * @param loadingUnit_columns
	 * @param wave_columns
	 * @throws ParseException
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static void filterOnColumns(String inputFolder, String outputFolder, List<Integer> xdlu_columns, List<Integer> consignment_columns, List<Integer> shipment_columns,
			List<Integer> shipmentItems_columns, List<Integer> loadingUnit_columns, List<Integer> wave_columns, List<Integer> hub_columns) throws ParseException, IOException {

		createFolders(outputFolder);

		String xdlu_folder_in = inputFolder + "xdlu_split_filtered";
		String consignments_folder_in = inputFolder + "consignments_split_filtered";
		String loadingUnits_folder_in = inputFolder + "loadingunits_split_filtered";
		String waves_folder_in = inputFolder + "waves_split_filtered";
		String shipmentItems_folder_in = inputFolder + "shipmentitems_split_filtered";
		String shipments_folder_in = inputFolder + "shipments_split_filtered";
		String hub_folder_in = inputFolder + "hubreconstructionlocations_split_filtered";

		String xdlu_folder_out = outputFolder + "xdlu_split_filtered";
		String consignments_folder_filtered = outputFolder + "consignments_split_filtered";
		String loadingUnits_folder_filtered = outputFolder + "loadingunits_split_filtered";
		String waves_folder_filtered = outputFolder + "waves_split_filtered";
		String shipmentItems_folder_filtered = outputFolder + "shipmentitems_split_filtered";
		String shipments_folder_filtered = outputFolder + "shipments_split_filtered";
		String hub_folder_filtered = outputFolder + "hubreconstructionlocations_split_filtered";

		filterXDocLoadingUnitsByColumns (xdlu_folder_in, xdlu_folder_out, xdlu_columns);		
		filterConsignmentsByColumns(consignments_folder_in, consignments_folder_filtered, consignment_columns);		
		filterLoadingUnitsByColumns(loadingUnits_folder_in, loadingUnits_folder_filtered, loadingUnit_columns);		
		filterWavesByColumns(waves_folder_in, waves_folder_filtered, wave_columns);			
		filterShipmentItemsByColumns(shipmentItems_folder_in, shipmentItems_folder_filtered, shipmentItems_columns);			
		filterShipmentsByColumns(shipments_folder_in, shipments_folder_filtered, shipment_columns);	
		filterHubReconstructionLocationsByColumns(hub_folder_in, hub_folder_filtered, hub_columns);

	}
	
	private static void filterHubReconstructionLocationsByColumns(String inputFolder, String filteredFolder, List<Integer> columns) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			filterCSVByColumns(f.getPath(), f.getPath().replace(inputFolder, filteredFolder), columns);
		}
		

	}

	private static void filterXDocLoadingUnitsByColumns(String inputFolder, String filteredFolder, List<Integer> columns) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			filterCSVByColumns(f.getPath(), f.getPath().replace(inputFolder, filteredFolder), columns);
		}
		

	}

	private static void filterConsignmentsByColumns(String inputFolder, String filteredFolder, List<Integer> columns) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			filterCSVByColumns(f.getPath(), f.getPath().replace(inputFolder, filteredFolder), columns);
		}

	}

	private static void filterLoadingUnitsByColumns(String inputFolder, String filteredFolder, List<Integer> columns) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			filterCSVByColumns(f.getPath(), f.getPath().replace(inputFolder, filteredFolder), columns);
		}

	}

	private static void filterWavesByColumns(String inputFolder, String filteredFolder, List<Integer> columns) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			filterCSVByColumns(f.getPath(), f.getPath().replace(inputFolder, filteredFolder), columns);
		}

	}

	private static void filterShipmentItemsByColumns(String inputFolder, String filteredFolder, List<Integer> columns) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			filterCSVByColumns(f.getPath(), f.getPath().replace(inputFolder, filteredFolder), columns);
		}

	}

	private static void filterShipmentsByColumns(String inputFolder, String filteredFolder, List<Integer> columns) {
		File folder = new File (inputFolder);
		File[] files = folder.listFiles();
		
		for (File f : files) {
			filterCSVByColumns(f.getPath(), f.getPath().replace(inputFolder, filteredFolder), columns);
		}

	}
	


	public static void filterCSVByColumns(String csvFileIn, String csvFileOut, List<Integer> columns) {


		BufferedReader br = null;
		BufferedWriter bw = null;

		List<String[]> line = new ArrayList<String[]>();


		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileIn), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(csvFileOut));
		} catch (IOException e1) {
			e1.printStackTrace();
		}


		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}


		for (String[] params : line) {

			Iterator<Integer> colsIterator = columns.iterator();

			while (colsIterator.hasNext()) {

				int col = colsIterator.next();

				try {
					//write cols and comma unless last col
					if (!colsIterator.hasNext()) {
						bw.write(params[col]);
					} else {
						bw.write(params[col] + ",");
					}

				} catch (IOException e) {
					e.printStackTrace();
				}				

			}
			try {
				bw.write("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

		}//end for


		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}
	
	private static void reformatShipmentItems (String inputFile) throws IOException {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		
		File input = new File (inputFile);
		File output = new File ("tmp");
		
		//create a tmp file to hold the content
		FileWriter fw = null;		
		
		try {
			fw = new FileWriter(output);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		List<String[]> line = new ArrayList<String[]>();
		
//		try {
//			try {
//				br = new BufferedReader(new FileReader(input));
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e1) {
//			e1.printStackTrace();
//		}
		
		
		br = new BufferedReader(new FileReader(input));
		
		bw = new BufferedWriter(fw);
		
		try {
			line = StringUtilities.oneByOne(br);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String shipmentId = null;
		String loadingUnitId = null;
		
		for (String[] params : line) {

			shipmentId = params[0];
			loadingUnitId = params[1];
			try {
				bw.write(shipmentId + "_" + loadingUnitId + "," + params[0] + "," + params[1] + "," + params[2] + "," + params[3] + "," + params[4]);
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}//end for
		
		output.renameTo(input);

	}

	/**
	 * Splits the (large) CSV file into more manageable sizes
	 * @param inputFile
	 * @param outputFolder
	 * @param chunkSizeInMb
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static void splitCSV(String inputFile, String outputFolder, int chunkSizeInMb) throws IOException {

		//create a folder to hold the chunks
		File chunkFolder = new File(outputFolder + "/" + inputFile.substring(inputFile.lastIndexOf("/"), inputFile.lastIndexOf(".")) + "_split");

		if (!chunkFolder.exists()) {
			chunkFolder.mkdir();
		}

		//get the core filename
		String fileName = inputFile.substring(inputFile.lastIndexOf("/"), inputFile.lastIndexOf("."));

		FileReader fileReader = new FileReader(inputFile);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		String line="";
		int fileSize = 0;
		BufferedWriter fos = new BufferedWriter(new FileWriter(chunkFolder + "/" + fileName + "_" +new Date().getTime()+".csv",true));
		while((line = bufferedReader.readLine()) != null) {
			if(fileSize + line.getBytes().length > chunkSizeInMb * 1024 * 1024){
				fos.flush();
				fos.close();
				fos = new BufferedWriter(new FileWriter(chunkFolder + "/" + fileName + "_" +new Date().getTime()+".csv",true));
				fos.write(line+"\n");
				fileSize = line.getBytes().length;
			}else{
				fos.write(line+"\n");
				fileSize += line.getBytes().length;
			}
		}          
		fos.flush();
		fos.close();
		bufferedReader.close();
	}

	/**
	 * Creates a list of files from a folder
	 * @param dir
	 * @return
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static List<File> createFileList(String dir) throws IOException {
		List<File> fileList = new ArrayList<File>();
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dir))) {
			for (Path path : stream) {
				if (!Files.isDirectory(path)) {
					fileList.add(path.toFile());
				}
			}
		}
		return fileList;
	}

	/**
	 * Creates a set of sub-folders in a folder
	 * @param splitCSVFilesFilteredFolder
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static void createFolders(String splitCSVFilesFilteredFolder) throws IOException {

		String[] listOfFolders = {"relevant_loading_unit_ids", "relevant_consignment_ids", "relevant_wave_ids", 
				"relevant_shipment_ids", "relevant_transport_ids", "hubreconstructionlocations_split_filtered", "xdlu_split_filtered", "consignments_split_filtered",
				"loadingunits_split_filtered", "waves_split_filtered", "tradeitems_split_filtered", "dgr_split_filtered",
				"shipmentitems_split_filtered", "shipments_split_filtered", "transports_split_filtered"};

		//create the parent target folder
		File p_folder = new File(splitCSVFilesFilteredFolder);

		if (!p_folder.exists()) {
			p_folder.mkdir();
		}

		File t_folder = null;

		for (String s : listOfFolders) {
			t_folder = new File(splitCSVFilesFilteredFolder + s);
			if (!t_folder.exists()) {
				t_folder.mkdir();
			}
		}

	}

	/**
	 * Ensures that the entries listed in CSV files are within a desired time period.
	 * @param startDateTime
	 * @param endDateTime
	 * @param splitCSVFilesFolder
	 * @param splitCSVFilesFilteredFolder
	 * @throws ParseException
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static void filterOnPeriod(String startDateTime, String endDateTime, String splitCSVFilesFolder, String splitCSVFilesFilteredFolder) throws ParseException, IOException {

		createFolders(splitCSVFilesFilteredFolder);

		String xdlu_folder_in = splitCSVFilesFolder + "xdlu_split";
		String consignments_folder_in = splitCSVFilesFolder + "consignments_split";
		String loadingUnits_folder_in = splitCSVFilesFolder + "loadingunits_split";
		String waves_folder_in = splitCSVFilesFolder + "waves_split";
		String shipmentItems_folder_in = splitCSVFilesFolder + "shipmentitems_split";
		String shipments_folder_in = splitCSVFilesFolder + "shipments_split";
		String hubReconstructionLocations_folder_in = splitCSVFilesFolder + "hubreconstructionlocations_split";


		String loadingUnitIdFile = splitCSVFilesFilteredFolder + "relevant_loading_unit_ids/loadingUnitIds.txt"; 
		String consignmentIdFile = splitCSVFilesFilteredFolder + "relevant_consignment_ids/consignmentIds.txt"; 
		String waveIdFile = splitCSVFilesFilteredFolder + "relevant_wave_ids/waveIds.txt"; 
		String shipmentIdFile = splitCSVFilesFilteredFolder + "relevant_shipment_ids/shipmentIds.txt";

		String xdlu_folder_out = splitCSVFilesFilteredFolder + "xdlu_split_filtered";
		String consignments_folder_filtered = splitCSVFilesFilteredFolder + "consignments_split_filtered";
		String loadingUnits_folder_filtered = splitCSVFilesFilteredFolder + "loadingunits_split_filtered";
		String waves_folder_filtered = splitCSVFilesFilteredFolder + "waves_split_filtered";
		String shipmentItems_folder_filtered = splitCSVFilesFilteredFolder + "shipmentitems_split_filtered";
		String shipments_folder_filtered = splitCSVFilesFilteredFolder + "shipments_split_filtered";
		String hubReconstructionLocations_folder_filtered = splitCSVFilesFilteredFolder + "hubreconstructionlocations_split_filtered";

		//the following calls need to run in the correct order
		
		filterXDocLoadingUnitsOnPeriod (xdlu_folder_in, xdlu_folder_out, startDateTime, endDateTime);		
		printRelevantConLoadWave (xdlu_folder_out, loadingUnitIdFile, consignmentIdFile, waveIdFile);
		filterConsignments(consignments_folder_in, consignments_folder_filtered, consignmentIdFile);		
		filterLoadingUnits(loadingUnits_folder_in, loadingUnits_folder_filtered, loadingUnitIdFile);		
		filterWaves(waves_folder_in, waves_folder_filtered, waveIdFile);			
		printRelevantShipmentIds (shipmentItems_folder_in, loadingUnitIdFile, shipmentIdFile);			
		filterShipmentItems(shipmentItems_folder_in, shipmentItems_folder_filtered, shipmentIdFile);			
		filterShipments(shipments_folder_in, shipments_folder_filtered, shipmentIdFile);			
		filterHubReconstructionLocations(hubReconstructionLocations_folder_in, hubReconstructionLocations_folder_filtered);

	}


	/**
	 * Ensures that only shipment entries corresponding to relevant shipment Ids are included in the output folder
	 * @param inputFolder
	 * @param filteredFolder
	 * @param relevantIds
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void filterShipments (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read shipment item ids from file
		Set<String> shipmentItemIds = new HashSet<String>();
		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(relevantIds), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {


				shipmentItemIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");

					if (shipmentItemIds.contains(params[0])) {
						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Ensures that only shipment item entries corresponding to relevant shipment item Ids are included in the output folder
	 * @param inputFolder
	 * @param filteredFolder
	 * @param relevantIds
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void filterShipmentItems (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read shipment item ids from file
		Set<String> shipmentItemIds = new HashSet<String>();
		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(relevantIds), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				shipmentItemIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");

					if (shipmentItemIds.contains(params[1])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}



	/**
	 * Ensures that only loading unit entries corresponding to relevant loading unit Ids are included in the output folder
	 * @param inputFolder
	 * @param filteredFolder
	 * @param relevantIds
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void filterLoadingUnits (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read loading unit ids from file
		Set<String> loadingUnitIds = new HashSet<String>();
		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(relevantIds), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				loadingUnitIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");

					if (loadingUnitIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	/**
	 * Ensures that only wave entries corresponding to relevant wave Ids are included in the output folder
	 * @param inputFolder
	 * @param filteredFolder
	 * @param relevantIds
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void filterWaves (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read wave ids from file
		Set<String> waveIds = new HashSet<String>();
		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(relevantIds), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				waveIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");

					if (waveIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	/**
	 * Ensures that only hub entries corresponding to relevant hub Ids are included in the output folder
	 * @param inputFolder
	 * @param filteredFolder
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void filterHubReconstructionLocations (String inputFolder, String filteredFolder) throws ParseException {

		BufferedReader br = null;

		String id = null;

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {

				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			//for hubs we do not perform any filtering, all hubs are included
			try {
				while ((line = br.readLine()) != null) {

						bw.write(line);
						bw.newLine();

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}


	/**
	 * Ensures that only consignment entries corresponding to relevant consignment Ids are included in the output folder
	 * @param inputFolder
	 * @param filteredFolder
	 * @param relevantIds
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void filterConsignments (String inputFolder, String filteredFolder, String relevantIds) throws ParseException {

		BufferedReader br = null;

		//read consignment ids from file
		Set<String> consignmentIds = new HashSet<String>();
		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(relevantIds), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				consignmentIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			String line;		

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");

					if (consignmentIds.contains(params[0])) {

						bw.write(line);
						bw.newLine();

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}
	
	/**
	 * Ensures that only cross-dock loading unit entries corresponding to relevant cross-dock loading unit Ids are included in the output folder
	 * @param inputFolder
	 * @param filteredFolder
	 * @param startDateTime
	 * @param endDateTime
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void filterXDocLoadingUnitsOnPeriod (String inputFolder, String filteredFolder, String startDateTime, String endDateTime) throws ParseException {

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedReader br = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {
			
			String line;		

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				bw = new BufferedWriter(new FileWriter(filteredFolder + "/" + filesInDir[i].getName() + "_filtered"));
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				while ((line = br.readLine()) != null) {

					params = line.split(",");
					

					if (!params[8].equals("NULL") && withinPeriod(params[8], startDateTime, endDateTime)) {

						bw.write(line);
						bw.newLine();


					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

	}

	
	/**
	 * Prints the shipment ids relevant for a given filtered period to file as to subset the entire dataset. 
	 * @param inputFolder
	 * @param filterPath
	 * @param shipmentIdsFile
	   29. apr. 2022
	 */
	private static void printRelevantShipmentIds (String inputFolder, String filterPath, String shipmentIdsFile) {
		
		Set<String> shipmentIds = new HashSet<String>();

		File splitFolder = new File(inputFolder);
		File[] filesInDir = splitFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;


		//read loading unit ids from file
		Set<String> loadingUnitIds = new HashSet<String>();
		try {
			try {
				br = new BufferedReader(new InputStreamReader(new FileInputStream(filterPath), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String id = null;
		try {
			while ((id = br.readLine()) != null) {

				loadingUnitIds.add(id);

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<String[]> line = new ArrayList<String[]>();

		for (int i = 0; i < filesInDir.length; i++) {

			try {
				try {
					br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			try {
				line = oneByOne(br);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (String[] s : line) {
				
				if (loadingUnitIds.contains(s[2])) {
					shipmentIds.add(s[1]);
				}
			}

			try {
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			

		}

		//print relevant shipment ids to file
		try {
			bw = new BufferedWriter(new FileWriter(shipmentIdsFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String tid : shipmentIds) {
			try {
				bw.write(tid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}


	}


	/**
	 * Prints consignment, loading units and wave Ids relevant for a given filtered period to file
	 * @param inputFolder
	 * @param loadingUnitIdFile
	 * @param consignmentIdFile
	 * @param waveIdFile
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static void printRelevantConLoadWave (String inputFolder, String loadingUnitIdFile, String consignmentIdFile, String waveIdFile) throws ParseException {
		
		Set<String> consignmentIds = new HashSet<String>();
		Set<String> waveIds = new HashSet<String>();
		Set<String> loadingUnitIds = new HashSet<String>();

		File splitFolder = new File(inputFolder);

		File[] filesInDir = splitFolder.listFiles();

		String[] params = null;
		BufferedReader br = null;
		BufferedWriter bw = null;

		for (int i = 0; i < filesInDir.length; i++) {

			try {

				String line;		

				br = new BufferedReader(new InputStreamReader(new FileInputStream(filesInDir[i]), "UTF-8"));

				while ((line = br.readLine()) != null) {

					params = line.split(",");

					loadingUnitIds.add(params[7]);
					consignmentIds.add(params[11]);
					consignmentIds.add(params[12]);
					waveIds.add(params[36]);
					loadingUnitIds.add(params[41]);
					loadingUnitIds.add(params[42]);

				}

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}

		//print relevant loading unit ids to file
		try {
			bw = new BufferedWriter(new FileWriter(loadingUnitIdFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String luid : loadingUnitIds) {
			try {
				bw.write(luid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//print relevant consignment ids to file
		try {
			bw = new BufferedWriter(new FileWriter(consignmentIdFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String cid : consignmentIds) {
			try {
				bw.write(cid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//print relevant wave ids to file
		try {
			bw = new BufferedWriter(new FileWriter(waveIdFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		for (String wid : waveIds) {
			try {
				bw.write(wid);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				bw.newLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	/**
	 * Reads the CSV file line by line and adds each line to a list
	 * @param reader
	 * @return
	 * @throws Exception
	   Aug 30, 2023
	 */
	private static List<String[]> oneByOne(Reader reader) throws Exception {
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


	/**
	 * Checks if a date falls within a period.
	 * @param input
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException
	   Aug 30, 2023
	 */
	private static boolean withinPeriod (String input, String start, String end) throws ParseException {
		
		boolean withinPeriod = false;

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		Date startDT = sdf.parse(start);
		Date endDT = sdf.parse(end);
		Date inputDT = sdf.parse(formatDateTime(input));

		if (inputDT.after(startDT) && inputDT.before(endDT)) {
			withinPeriod = true;
		}

		return withinPeriod;
	}

	/**
	 * Ensures proper time formatting
	 * @param input
	 * @return
	   Aug 30, 2023
	 */
	private static String formatDateTime (String input) {
		String dateTime = input.substring(0, input.lastIndexOf("."));

		dateTime = dateTime.replaceAll(" ", "T");

		return dateTime;
	}

	
	/**
	 * Joins a set of files in a folder into a single output file
	 * @param inputFolder
	 * @param output
	 * @return
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static File joinFiles (File inputFolder, File output) throws IOException {
		try (FileWriter fw = new FileWriter(output); 
				BufferedWriter bw = new BufferedWriter(fw)) {

			for (File file : inputFolder.listFiles()) {
				try (FileReader fr = new FileReader(file); 
						BufferedReader br = new BufferedReader(fr)) {
					String line;

					// Start to parse the file at the first row containing data
					while ((line = br.readLine()) != null) {

						bw.write(line);
						bw.newLine();
					}
				}
			}

		}

		return output;

	}

	/**
	 * Removes the (header) first line from all files in a folder
	 * @param folder
	 * @throws IOException
	   Aug 30, 2023
	 */
	public static void removeFirstLineFromFilesInFolder(String folder) throws IOException {  


		File[] files = new File(folder).listFiles();

		for (File f : files) {
			RandomAccessFile raf = new RandomAccessFile(f.getPath(), "rw");
			//Initial write position                                             
			long writePosition = raf.getFilePointer();                            
			raf.readLine();                                                       
			// Shift the next lines upwards.                                      
			long readPosition = raf.getFilePointer();                             

			byte[] buff = new byte[1024];                                         
			int n;                                                                
			while (-1 != (n = raf.read(buff))) {                                  
				raf.seek(writePosition);                                          
				raf.write(buff, 0, n);                                            
				readPosition += n;                                                
				writePosition += n;                                               
				raf.seek(readPosition);                                           
			}                                                                     
			raf.setLength(writePosition);                                         
			raf.close();    
		}


	}  

}
