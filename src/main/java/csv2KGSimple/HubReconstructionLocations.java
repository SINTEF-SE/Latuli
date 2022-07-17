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
public class HubReconstructionLocations {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processHubReconstructionLocationsToNTriple (File hubReconstructionLocationsFolder, String ntFile) {
		
		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "HubReconstructionLocation";
		String tripleClosure = "> .\n";

		String hubReconstructionLocationEntity;

		File[] filesInDir = hubReconstructionLocationsFolder.listFiles();

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

					hubReconstructionLocationEntity = params[0] + "_hubReconstructionLocation>";
					
					//rdf:type
					bw.write(KGUtilities.createType(hubReconstructionLocationEntity, baseURI, rdf_type, type, tripleClosure));
					
					//hasHubParty						
					bw.write(KGUtilities.createObjectProperty(hubReconstructionLocationEntity, baseURI, "hasHubParty", params[0], "_party", tripleClosure));
			
					//additionalPartyIdentification
					bw.write(KGUtilities.createDataProperty(hubReconstructionLocationEntity, baseURI, "additionalPartyIdentification", params[0], DATATYPE_STRING, tripleClosure));

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
	
	public static void processHubReconstructionLocationsToTSV (File hubReconstructionLocationsFolder, String tsvFile) {

		String hubReconstructionLocationEntity;

		File[] filesInDir = hubReconstructionLocationsFolder.listFiles();

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

					hubReconstructionLocationEntity = params[0] + "_hubReconstructionLocation";
					
					bw.write(hubReconstructionLocationEntity + "\t" + "isType" + "\t" + "HubReconstructionLocation" + "\n");
					
					//hasHubParty						
					bw.write(hubReconstructionLocationEntity + "\t" + "hasHubParty" + "\t" + params[3] + "_party" + "\n");

					//hasAdditionalPartyId
					bw.write(hubReconstructionLocationEntity + "\t" + "hasAdditionalPartyId" + "\t" + params[1] + "\n");

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

}//end class
