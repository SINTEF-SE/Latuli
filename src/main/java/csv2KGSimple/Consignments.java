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
public class Consignments {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	
	public static void processConsignmentsToNTriple (File consignmentFolder, String ntFile) {
		
		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "Consignment";
		String tripleClosure = "> .\n";

		String consignmentEntity;
		
		String hubReconstructionLocationEntity;

		File[] filesInDir = consignmentFolder.listFiles();

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

					//rdf:type				
					consignmentEntity = params[0] + "_consignment>";

					bw.write(KGUtilities.createType(consignmentEntity, baseURI, rdf_type, type, tripleClosure));

					//isType for HubReconstructionLocation;
					hubReconstructionLocationEntity = params[19] + "_hubReconstructionLocation";
					
					//processedByWave
					if (!params[18].equals("NULL")) {								

						bw.write(KGUtilities.createObjectProperty(consignmentEntity, baseURI, "processedByWave", params[18], "_wave", tripleClosure));
					}

					//consignmentId						
					bw.write(KGUtilities.createDataProperty(consignmentEntity, baseURI, "consignmentId", params[0], DATATYPE_STRING, tripleClosure));
					
					//hasHubParty				
					//bw.write(KGUtilities.createObjectProperty(consignmentEntity, baseURI, "hasHubParty", params[19], "_party", tripleClosure));
					
					//isConsignedAt		
					bw.write(KGUtilities.createObjectProperty(consignmentEntity, baseURI, "isConsignedAt", params[19], "_hubReconstructionLocation", tripleClosure));
					
					//hasConsigned
					bw.write(KGUtilities.createObjectProperty(hubReconstructionLocationEntity, baseURI, "hasConsigned", params[0], "_consignment", tripleClosure));

					//qttReconstructedPallets						
					bw.write(KGUtilities.createDataProperty(consignmentEntity, baseURI, "qttReconstructedPallets", params[35], DATATYPE_INT, tripleClosure));

					//qttReconstructedParcels						
					bw.write(KGUtilities.createDataProperty(consignmentEntity, baseURI, "qttReconstructedParcels", params[36], DATATYPE_INT, tripleClosure));

					//totalConsignmentVolume						
					bw.write(KGUtilities.createDataProperty(consignmentEntity, baseURI, "totalConsignmentVolume", params[40], DATATYPE_DECIMAL, tripleClosure));

					//totalConsignmentWeight
					bw.write(KGUtilities.createDataProperty(consignmentEntity, baseURI, "totalConsignmentWeight", params[41], DATATYPE_DECIMAL, tripleClosure));

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
	
	public static void processConsignmentsToTSV (File consignmentFolder, String tsvFile) {

		String consignmentEntity;
		String hubReconstructionLocationEntity;

		File[] filesInDir = consignmentFolder.listFiles();

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
					consignmentEntity = params[0] + "_consignment";
					
					bw.write(consignmentEntity + "\t" + "isType" + "\t" + "Consignment" + "\n");

					//isProcessedByWave
					if (!params[18].equals("NULL")) {								

						bw.write(consignmentEntity + "\t" + "isProcessedByWave" + "\t" + params[18] + "_wave" + "\n");
					}

					//hasConsignmentId						
					bw.write(consignmentEntity + "\t" + "hasConsignmentId" + "\t" + params[0] + "\n");
					
					//hubReconstructionLocationEntity
					hubReconstructionLocationEntity = params[4] + "_hubReconstructionLocation";
					
					//hasHubParty				
					//bw.write(consignmentEntity + "\t" + "hasHubParty" + "\t" + params[19] + "_party" + "\n");

					//isConsignedAt
					bw.write(consignmentEntity + "\t" + "isConsignedAt" + "\t" + params[19] + "_hubReconstructionLocation" + "\n");
					
					//hasConsigned
					bw.write(hubReconstructionLocationEntity + "\t" + "hasConsigned" + "\t" + params[0] + "_consignment" + "\n");
					
					//hasQttReconstructedPallets						
					bw.write(consignmentEntity + "\t" + "hasQttReconstructedPallets" + "\t" + params[35] + "\n");


					//hasQttReconstructedParcels						
					bw.write(consignmentEntity + "\t" + "hasQttReconstructedParcels" + "\t" + params[36] + "\n");


					//hasTotalConsignmentVolume						
					bw.write(consignmentEntity + "\t" + "hasTotalConsignmentVolume" + "\t" + params[40] + "\n");


					//hasTotalConsignmentWeight
					bw.write(consignmentEntity + "\t" + "hasTotalConsignmentWeight" + "\t" + params[41] + "\n");


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
