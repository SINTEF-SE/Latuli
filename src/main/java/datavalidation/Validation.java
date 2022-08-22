package datavalidation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class Validation {

	//test method
	public static void main(String[] args) {


		String inputConsignmentCSV = "./files/DATASETS/FilteredByColumns/consignments.csv";
		String inputShipmentsCSV = "./files/DATASETS/FilteredByColumns/shipments.csv";
		String inputShipmentItemsCSV = "./files/DATASETS/FilteredByColumns/shipmentitems.csv";
		String inputLoadingUnitsCSV = "./files/DATASETS/FilteredByColumns/loadingunits.csv";
		String inputXDocLoadingUnitsCSV = "./files/DATASETS/FilteredByColumns/xdlu.csv";
		String inputWavesCSV = "./files/DATASETS/FilteredByColumns/waves.csv";

		String outputConsignmentCSV = "./files/DATASETS/FilteredByColumns/consignments_validated.csv";
		String outputShipmentsCSV = "./files/DATASETS/FilteredByColumns/shipments_validated.csv";
		String outputShipmentItemsCSV = "./files/DATASETS/FilteredByColumns/shipmentitems_validated.csv";
		String outputLoadingUnitsCSV = "./files/DATASETS/FilteredByColumns/loadingunits_validated.csv";
		String outputXDocLoadingUnitsCSV = "./files/DATASETS/FilteredByColumns/xdlu_validated.csv";
		String outputWavesCSV = "./files/DATASETS/FilteredByColumns/waves_validated.csv";


		validateConsignments(inputConsignmentCSV, outputConsignmentCSV);

		validateShipments(inputShipmentsCSV, outputShipmentsCSV);

		validateShipmentItems(inputShipmentItemsCSV, outputShipmentItemsCSV);

		validateLoadingUnits(inputLoadingUnitsCSV, outputLoadingUnitsCSV);

		validateXDocLoadingUnits(inputXDocLoadingUnitsCSV, outputXDocLoadingUnitsCSV);

		validateWaves(inputWavesCSV, outputWavesCSV);

	}

	public static void validateHubs(String inputCSV, String outputCSV) {

		System.out.println("Validating hubreconstructionlocations...");

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		
		Set<String> uniqueHubs = new HashSet<String>();

		try {
			br = new BufferedReader(new FileReader(inputCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputCSV));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				//if (isValidId(params[0])) {
					uniqueHubs.add(line);
				//}
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
			for (String hub : uniqueHubs) {
				bw.write(hub + ",Hub");
				bw.newLine();
			} 	
		}
		catch (IOException e) {
			e.printStackTrace();
		}


		try {
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void validateConsignments(String inputCSV, String outputCSV) {

		System.out.println("Validating consignments...");

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(inputCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputCSV));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (isValidId(params[0]) && isValidDate(params[2]) && isValidInt(params[3]) && isValidInt(params[4]) && isValidInt(params[5]) && isValidInt(params[6]) && isValidInt(params[7]) 
						&& isValidDouble(params[8]) && isValidDouble(params[9])) {

					bw.write(line + ",Consignment");
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

	public static void validateShipments(String inputCSV, String outputCSV) {

		System.out.println("Validating shipments...");

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(inputCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputCSV));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (isValidId(params[0]) && isValidDate(params[1]) && isValidInt(params[2]) && isValidInt(params[3])) {

					bw.write(line + ",Shipment");
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

	public static void validateShipmentItems(String inputCSV, String outputCSV) {

		System.out.println("Validating shipmentitems...");

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(inputCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputCSV));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (isValidId(params[0]) && isValidDate(params[3]) && isValidInt(params[4])) {

					bw.write(line + ",ShipmentItem");
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

	public static void validateLoadingUnits(String inputCSV, String outputCSV) {

		System.out.println("Validating loadingunits...");

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(inputCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputCSV));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (isValidId(params[0]) && isValidDate(params[3])) {

					bw.write(line + ",LoadingUnit");
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

	public static void validateXDocLoadingUnits(String inputCSV, String outputCSV) {

		System.out.println("Validating xdlu...");

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(inputCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputCSV));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (isValidId(params[0]) && isValidDate(params[1]) && isValidDate(params[2]) && isValidDate(params[3]) && isValidDouble(params[4]) && isValidDouble(params[5])) {

					bw.write(line + ",XDocLoadingUnit");
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

	public static void validateWaves(String inputCSV, String outputCSV) {

		System.out.println("Validating waves...");

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(inputCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputCSV));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

				if (isValidId(params[0]) && isValidDate(params[1]) && isValidDate(params[2]) && isValidDate(params[4]) && isValidDate(params[6]) && isValidDate(params[8]) 
						&& isValidDate(params[9]) && isValidInt(params[11]) && isValidInt(params[12])
						&& isValidInt(params[13]) && isValidInt(params[14]) && isValidInt(params[15]) && isValidInt(params[16])) {

					bw.write(line + ",Wave");
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

	public static boolean isValidDate(String date) 
	{

		String DATE_FORMAT = "\"yyyy-MM-dd'T'HH:mm\"";

		try {
			DateFormat df = new SimpleDateFormat(DATE_FORMAT);
			df.setLenient(false);
			df.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}


	public static boolean isValidId (String idValue) {

		if (idValue.equals("NULL") || idValue.equals("\"NULL\"")) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean isValidInt(String intValue) {

		try {
			int x = Integer.parseInt(intValue.replaceAll("^\"|\"$", "")); 
			return true;
		} catch(NumberFormatException e) {
			return false;
		} 

	}

	public static boolean isValidDouble(String doubleValue) {

		try {
			double x = Double.parseDouble(doubleValue.replaceAll("^\"|\"$", "")); 
			return true;
		} catch(NumberFormatException e) {
			return false;
		} 

	}

}
