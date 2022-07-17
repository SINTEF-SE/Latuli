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
public class Waves {
	
	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";
	
	public static void processWavesToNTriple (File wavesFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String waveType = "Wave";
		//String hubReconstructionLocationType = "HubReconstructionLocation";
		String tripleClosure = "> .\n";

		String waveEntity;
		
		String hubReconstructionLocationEntity;

		File[] filesInDir = wavesFolder.listFiles();

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


					//isType for Wave					
					waveEntity = params[0] + "_wave>";
					bw.write(KGUtilities.createType(waveEntity, baseURI, rdf_type, waveType, tripleClosure));
					
					//isType for HubReconstructionLocation;
					hubReconstructionLocationEntity = params[4] + "_hubReconstructionLocation";

					//waveid
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "waveId", params[0], DATATYPE_STRING, tripleClosure));
					
					//isPlannedAt			
					bw.write(KGUtilities.createObjectProperty(waveEntity, baseURI, "isPlannedAt", params[4], "_hubReconstructionLocation", tripleClosure));
					//<https://w3id.org/latuli/ontology/m3#55064_wave> <https://w3id.org/latuli/ontology/m3#isPlannedAt>  <https://w3id.org/latuli/ontology/m3#C004_hubReconstructionLocation> .

					
					//hasPlanned
					bw.write(KGUtilities.createObjectProperty(hubReconstructionLocationEntity, baseURI, "hasPlanned", params[0], "_wave", tripleClosure));

					//qttBoxesProcessed
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttBoxesProcessed", params[18], DATATYPE_INT, tripleClosure));


					//qttPalletsBuilt
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttPalletsBuilt", params[19], DATATYPE_INT, tripleClosure));


					//qttShipments
					bw.write(KGUtilities.createDataProperty(waveEntity, baseURI, "qttShipments", params[21], DATATYPE_INT, tripleClosure));



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
	
	public static void processWavesToTSV (File wavesFolder, String tsvFile) {


		String waveEntity;
		String hubReconstructionLocationEntity;

		File[] filesInDir = wavesFolder.listFiles();

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
					waveEntity = params[0] + "_wave";
					bw.write(waveEntity + "\t" + "isType" + "\t" + "Wave" + "\n");
					
					//hubReconstructionLocationEntity
					hubReconstructionLocationEntity = params[4] + "_hubReconstructionLocation";
					
					//hasWaveid
					bw.write(waveEntity + "\t" + "hasWaveid" + "\t" + params[0] + "\n");

					//isPlannedAt			
					bw.write(waveEntity + "\t" + "isPlannedAt" + "\t" + params[4] + "_hubReconstructionLocation" + "\n");
					System.out.println("Waves: " + waveEntity + "\t" + "isPlannedAt" + "\t" + params[4] + "_hubReconstructionLocation" + "\n");
					
					//hasPlanned
					bw.write(hubReconstructionLocationEntity + "\t" + "hasPlanned" + "\t" + params[0] + "_wave" + "\n");
					System.out.println("Waves: " + hubReconstructionLocationEntity + "\t" + "hasPlanned" + "\t" + params[0] + "_wave" + "\n");

					//hasQttBoxesProcessed
					bw.write(waveEntity + "\t" + "hasQttBoxesProcessed" + "\t" + params[18] + "\n");

					//hasQttPalletsBuilt
					bw.write(waveEntity + "\t" + "hasQttPalletsBuilt" + "\t" + params[19] + "\n");

					//hasQttShipments
					bw.write(waveEntity + "\t" + "hasQttShipments" + "\t" + params[21] + "\n");


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
