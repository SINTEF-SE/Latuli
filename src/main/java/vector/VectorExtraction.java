package vector;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class VectorExtraction {
	
	public static void main(String[] args) {
		
		String initialVectorFile = "./files/embeddings/vectors.txt";
		String outputVectorFile = "./files/embeddings/outputVectors_hubs.txt";

		BufferedReader br = null;
		BufferedWriter bw = null;
		String[] params = null;
		String line;		
		
		try {
			br = new BufferedReader(new FileReader(initialVectorFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			bw = new BufferedWriter(new FileWriter(outputVectorFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			while ((line = br.readLine()) != null) {

				params = line.split("\t");
				
				if (params[0].contains("_hubReconstructionLocation")) {

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
