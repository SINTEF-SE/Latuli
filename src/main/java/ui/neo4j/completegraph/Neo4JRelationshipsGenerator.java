package ui.neo4j.completegraph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Neo4JRelationshipsGenerator {
	
	public static void main(String[] args) {
		
		String consignmentCSV = "./files/DATASETS/2020_2022_FilteredByColumns/consignments_validated.csv";
		String shipmentItemCSV = "./files/DATASETS/2020_2022_FilteredByColumns/shipmentitems_validated.csv";
		String xdluCSV = "./files/DATASETS/2020_2022_FilteredByColumns/xdlu_validated.csv";
		
		String CONSIGNMENT_INCLUDED_IN_WAVE = "./files/DATASETS/Relationships/CONSIGNMENT_INCLUDED_IN_WAVE.csv";
		String SHIPMENT_ITEM_IN_SHIPMENT = "./files/DATASETS/Relationships/SHIPMENT_ITEM_IN_SHIPMENT.csv";
		String SHIPMENT_ITEM_INCLUDES_LOADING_UNIT = "./files/DATASETS/Relationships/SHIPMENT_ITEM_INCLUDES_LOADING_UNIT.csv";
		String XDLU_INCLUDED_IN_WAVE = "./files/DATASETS/Relationships/XDLU_INCLUDED_IN_WAVE.csv";
		String XDLU_PROCESSED_BY_HUB = "./files/DATASETS/Relationships/XDLU_PROCESSED_BY_HUB.csv";
		String XDLU_INCLUDES_OUTBOUND_CONSIGNMENT = "./files/DATASETS/Relationships/XDLU_INCLUDES_OUTBOUND_CONSIGNMENT.csv";
		String XDLU_INCLUDES_INBOUND_CONSIGNMENT = "./files/DATASETS/Relationships/XDLU_INCLUDES_INBOUND_CONSIGNMENT.csv";
		String XDLU_LOADING_UNIT = "./files/DATASETS/Relationships/XDLU_LOADING_UNIT.csv";
		
		generateConsignmentIncludedInWaveRel(consignmentCSV, CONSIGNMENT_INCLUDED_IN_WAVE);
		generateShipmentItemInShipmentRel(shipmentItemCSV, SHIPMENT_ITEM_IN_SHIPMENT);
		generateShipmentItemIncludesLoadingUnitRel(shipmentItemCSV, SHIPMENT_ITEM_INCLUDES_LOADING_UNIT);
		generateXdluIncludedInWaveRel(xdluCSV, XDLU_INCLUDED_IN_WAVE);
		generateXdluProcessedByHubRel(xdluCSV, XDLU_PROCESSED_BY_HUB);
		generateXdluIncludesOutboundConsignmentRel(xdluCSV, XDLU_INCLUDES_OUTBOUND_CONSIGNMENT);
		generateXdluIncludesInboundConsignmentRel(xdluCSV, XDLU_INCLUDES_INBOUND_CONSIGNMENT);
		generateXdluLoadingUnitRel(xdluCSV, XDLU_LOADING_UNIT);
		
	}
	
	public static void generateConsignmentIncludedInWaveRel (String consignmentCSV, String CONSIGNMENT_INCLUDED_IN_WAVE) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(consignmentCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(CONSIGNMENT_INCLUDED_IN_WAVE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[1] + ",CONSIGNMENT_INCLUDED_IN_WAVE");
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
	
	public static void generateShipmentItemInShipmentRel (String shipmentItemCSV, String SHIPMENT_ITEM_IN_SHIPMENT) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(shipmentItemCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(SHIPMENT_ITEM_IN_SHIPMENT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[1] + ",SHIPMENT_ITEM_IN_SHIPMENT");
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
	
	public static void generateShipmentItemIncludesLoadingUnitRel (String shipmentItemCSV, String SHIPMENT_ITEM_INCLUDES_LOADING_UNIT) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(shipmentItemCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(SHIPMENT_ITEM_INCLUDES_LOADING_UNIT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[2] + ",SHIPMENT_ITEM_INCLUDES_LOADING_UNIT");
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
	
	public static void generateXdluIncludedInWaveRel (String xdluCSV, String XDLU_INCLUDED_IN_WAVE) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(xdluCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(XDLU_INCLUDED_IN_WAVE));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[10] + ",XDLU_INCLUDED_IN_WAVE");
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
	
	public static void generateXdluProcessedByHubRel (String xdluCSV, String XDLU_PROCESSED_BY_HUB) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(xdluCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(XDLU_PROCESSED_BY_HUB));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[9] + ",XDLU_PROCESSED_BY_HUB");
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
	
	public static void generateXdluIncludesOutboundConsignmentRel (String xdluCSV, String XDLU_INCLUDES_OUTBOUND_CONSIGNMENT) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(xdluCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(XDLU_INCLUDES_OUTBOUND_CONSIGNMENT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[8] + ",XDLU_INCLUDES_OUTBOUND_CONSIGNMENT");
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
	
	public static void generateXdluIncludesInboundConsignmentRel (String xdluCSV, String XDLU_INCLUDES_INBOUND_CONSIGNMENT) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(xdluCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(XDLU_INCLUDES_INBOUND_CONSIGNMENT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[7] + ",XDLU_INCLUDES_INBOUND_CONSIGNMENT");
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
	
	public static void generateXdluLoadingUnitRel (String xdluCSV, String XDLU_LOADING_UNIT) {
		
		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		

		try {
			br = new BufferedReader(new FileReader(xdluCSV));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(XDLU_LOADING_UNIT));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");

					bw.write(params[0] + "," + params[6] + ",XDLU_LOADING_UNIT");
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
