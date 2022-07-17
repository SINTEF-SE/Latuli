package csv2KGSimple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import utilities.KGUtilities;
import utilities.StringUtilities;

/**
 * @author audunvennesland
 *
 */
public class XDocLoadingUnits {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processXDocLoadingUnitsToNTriple (File xdluFolder, String ntFile) {
		
		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "XDocLoadingUnit";
		String tripleClosure = "> .\n";

		String xDocLoadingUnitEntity;
		
		String hubReconstructionLocationEntity;

		File[] filesInDir = xdluFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;

		List<String[]> line = new ArrayList<String[]>();


		for (int i = 0; i < filesInDir.length; i++) {

			try {


				br = new BufferedReader(new FileReader(filesInDir[i]));
				bw = new BufferedWriter(new FileWriter(ntFile, true));
				
				try {
					line = StringUtilities.oneByOne(br);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (String[] params : line) {


					//isType				
					xDocLoadingUnitEntity = params[0] + "_xDocLoadingUnit>";

					bw.write(KGUtilities.createType(xDocLoadingUnitEntity, baseURI, rdf_type, type, tripleClosure));

					hubReconstructionLocationEntity = params[13] + "_hubReconstructionLocation";
					
					//internalId
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "internalId", params[0], DATATYPE_STRING, tripleClosure));

					//hasOutboundConsignment
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasOutboundConsignment", params[12], "_consignment", tripleClosure));


					//hasHubParty
					//bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "hasHubParty", params[13], "_party", tripleClosure));

					//isReconstructedAt
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "isReconstructedAt", params[13], "_hubReconstructionLocation", tripleClosure));
					System.out.println("XDocLoadingUnits: Adding object property" + xDocLoadingUnitEntity + baseURI + "isReconstructedAt" + params[13] + "_hubReconstructionLocation");
					
					//hasReconstructed
					bw.write(KGUtilities.createObjectProperty(hubReconstructionLocationEntity, baseURI, "hasReconstructed", params[0], "_xDocLoadingUnit", tripleClosure));
					System.out.println("XDocLoadingUnits: Adding object property" + hubReconstructionLocationEntity + baseURI + "hasReconstructed" + params[0] + "_xDocLoadingUnit");
					
					//processedByWave
					bw.write(KGUtilities.createObjectProperty(xDocLoadingUnitEntity, baseURI, "processedByWave", params[36], "_wave", tripleClosure));

					//volume
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "volume", params[5], DATATYPE_DECIMAL, tripleClosure));


					//weight
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "weight", params[6], DATATYPE_DECIMAL, tripleClosure));


					//reconstructionType
					bw.write(KGUtilities.createDataProperty(xDocLoadingUnitEntity, baseURI, "reconstructionType", params[37], DATATYPE_STRING, tripleClosure));




				}//end for

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				try {
					if (bw != null)
						bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}


	}

	
	public static void processXDocLoadingUnitsToTSV (File xdluFolder, String tsvFile) {

		String xDocLoadingUnitEntity;
		String hubReconstructionLocationEntity;

		File[] filesInDir = xdluFolder.listFiles();

		BufferedReader br = null;
		BufferedWriter bw = null;

		List<String[]> line = new ArrayList<String[]>();


		for (int i = 0; i < filesInDir.length; i++) {

			try {


				br = new BufferedReader(new FileReader(filesInDir[i]));
				bw = new BufferedWriter(new FileWriter(tsvFile, true));

				
				try {
					line = StringUtilities.oneByOne(br);
				} catch (Exception e) {
					e.printStackTrace();
				}

				for (String[] params : line) {


					//isType				
					xDocLoadingUnitEntity = params[0] + "_xDocLoadingUnit";

					bw.write(xDocLoadingUnitEntity + "\t" + "isType" + "\t" + "XDocLoadingUnit" + "\n");
					
					//hubReconstructionLocationEntity
					hubReconstructionLocationEntity = params[13] + "_hubReconstructionLocation";

					//hasInternalId
					bw.write(xDocLoadingUnitEntity + "\t" + "hasInternalId" + "\t" + params[0] + "\n");
					
					//hasOutboundConsignment
					bw.write(xDocLoadingUnitEntity + "\t" + "hasOutboundConsignment" + "\t" + params[12] + "_consignment" + "\n");

					//hasHubParty
					//bw.write(xDocLoadingUnitEntity + "\t" + "hasHubParty" + "\t" + params[13] + "_party" + "\n");
					
					//isReconstructedAt
					bw.write(xDocLoadingUnitEntity + "\t" + "isReconstructedAt" + "\t" + params[13] + "_hubReconstructionLocation" + "\n");
					
					//hasReconstructed
					bw.write(hubReconstructionLocationEntity + "\t" + "hasReconstructed" + "\t" + params[0] + "_xDocLoadingUnit" + "\n");

					//isProcessedByWave
					bw.write(xDocLoadingUnitEntity + "\t" + "isProcessedByWave" + "\t" + params[36] + "_wave" + "\n");

					//hasVolume
					bw.write(xDocLoadingUnitEntity + "\t" + "hasVolume" + "\t" + params[5] + "\n");

					//hasWeight
					bw.write(xDocLoadingUnitEntity + "\t" + "hasWeight" + "\t" + params[6] + "\n");

					//hasReconstructionType
					bw.write(xDocLoadingUnitEntity + "\t" + "hasReconstructionType" + "\t" + params[37] + "\n");


				}//end for

			} catch (IOException e) {

				e.printStackTrace();

			} finally {

				try {
					if (br != null)
						br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}

				try {
					if (bw != null)
						bw.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}

		}
		

	}

}
