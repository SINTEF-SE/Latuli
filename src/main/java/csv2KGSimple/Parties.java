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
public class Parties {

	final static String DATATYPE_INT = "^^<http://www.w3.org/2001/XMLSchema#int";
	final static String DATATYPE_DATETIME = "^^<http://www.w3.org/2001/XMLSchema#dateTime";
	final static String DATATYPE_STRING = "^^<http://www.w3.org/2001/XMLSchema#string";
	final static String DATATYPE_DECIMAL = "^^<http://www.w3.org/2001/XMLSchema#decimal";

	public static void processPartiesToNTriple (File partiesFolder, String ntFile) {

		String rdf_type = " <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> ";
		String baseURI = "<https://w3id.org/latuli/ontology/m3#";
		String type = "Party";
		String tripleClosure = "> .\n";
		
		String geoSparqlDataProperty = "<http://www.opengis.net/ont/geosparql#asWKT>";
		String geoSparqlTripleClosure = "^^<http://www.opengis.net/ont/geosparql#wktLiteral> .\n";

		String partyEntity;

		File[] filesInDir = partiesFolder.listFiles();

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

					//adding types

					partyEntity = params[0] + "_party>";

					bw.write(KGUtilities.createType(partyEntity, baseURI, rdf_type, type, tripleClosure));

					//additionalPartyIdentification
					bw.write(KGUtilities.createDataProperty(partyEntity, baseURI, "additionalPartyIdentification", params[0], DATATYPE_STRING, tripleClosure));



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

	public static void processPartiesToTSV(File partiesFolder, String tsvFile) {


		String partyEntity;

		File[] filesInDir = partiesFolder.listFiles();

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

					//adding types

					partyEntity = params[0] + "_party";

					bw.write(partyEntity + "\t" + "isType" + "\t" + "Party" + "\n");

					//hasAdditionalPartyId
					bw.write(partyEntity + "\t" + "hasAdditionalPartyId" + "\t" + params[0] + "\n");



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
