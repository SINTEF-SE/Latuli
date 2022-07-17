package analysis;

import java.util.ArrayList;

/**
 * @author audunvennesland
 * 29. sep. 2017 
 */
public class Cosine {
	
	public static double cosineSimilarity(double[] vectorA, double[] vectorB) {
		
	    double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    double tempSim = 0.0;
	    double finalSim = 0.0;
	    
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    tempSim = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	    
	    if (tempSim < 0) {
	    	finalSim = 0;
	    } else if (tempSim > 1.0) {
	    	finalSim = 1.0;
	    } else {
	    	finalSim = tempSim;
	    }

		return Double.isNaN(finalSim) ? 0.0 : finalSim;
	}
	
	public static double cosineSimilarity(ArrayList<Double> vectorAList, ArrayList<Double> vectorBList) {
		
		double[] vectorA = new double[vectorAList.size()];
		double[] vectorB = new double[vectorBList.size()];
		
		for (int i = 0; i < vectorA.length; i++) {
			vectorA[i] = vectorAList.get(i);
		}
		
		for (int i = 0; i < vectorB.length; i++) {
			vectorB[i] = vectorBList.get(i);
		}
		
		double dotProduct = 0.0;
	    double normA = 0.0;
	    double normB = 0.0;
	    double tempSim = 0.0;
	    double finalSim = 0.0;
	    
	    for (int i = 0; i < vectorA.length; i++) {
	        dotProduct += vectorA[i] * vectorB[i];
	        normA += Math.pow(vectorA[i], 2);
	        normB += Math.pow(vectorB[i], 2);
	    }   
	    tempSim = dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
	    
	    if (tempSim < 0) {
	    	finalSim = 0;
	    } else if (tempSim > 1.0) {
	    	finalSim = 1.0;
	    } else {
	    	finalSim = tempSim;
	    }
	    
	    return finalSim;
		
	}
	
	
	
	public static void main(String[] args) {
		
		String C025_2022_04_14 = "0.03066301715700049 0.3087526624341489 0.04137730508530727 0.26860504043936106 0.06381973611690198 0.7679103284598431 0.041260676882833164 0.037841367626986915 0.4777003987611713 0.06913398933513526";
		String C034_2021_08_13 = "0.32948046169087875 0.14750620278133855 0.00860921032297285 0.267312818163414 0.2796437980493992 0.6636461920870937 0.35526828500686025 0.02011096599528711 0.3902149135364425 0.025291854047878662";
		
		String C042_2021_08_13 = "0.03846709385223068 0.31286537729563696 0.02010968485548534 0.28837941507607495 0.08587617841543287 0.7182905229096341 0.04876075224039641 0.05221898714819755 0.5325918630914979 0.0706677587399949";

		String[] C025_2022_04_14_array = C025_2022_04_14.split(" ");
		String[] C034_2021_08_13_array = C034_2021_08_13.split(" ");
		
		String[] C042_2021_08_13_array = C042_2021_08_13.split(" ");
		
		
		double[] C025_2022_04_14_vec = new double[C025_2022_04_14_array.length];
		double[] C034_2021_08_13_vec = new double[C034_2021_08_13_array.length];
		double[] C042_2021_08_13_vec = new double[C042_2021_08_13_array.length];
		
		for (int i = 0; i < C025_2022_04_14_array.length; i++) {
			C025_2022_04_14_vec[i] = Double.parseDouble(C025_2022_04_14_array[i]);
		}
		
		for (int i = 0; i < C034_2021_08_13_array.length; i++) {
			C034_2021_08_13_vec[i] = Double.parseDouble(C034_2021_08_13_array[i]);
		}
		
		for (int i = 0; i < C042_2021_08_13_array.length; i++) {
			C042_2021_08_13_vec[i] = Double.parseDouble(C042_2021_08_13_array[i]);
		}
		
		
		double cos_sim_C025_2022_04_14_C034_2021_08_13 = cosineSimilarity(C025_2022_04_14_vec, C034_2021_08_13_vec);	
		double cos_sim_C025_2022_04_14_C042_2021_08_13 = cosineSimilarity(C025_2022_04_14_vec, C042_2021_08_13_vec);
		
		
		System.out.println("The cosine similarity between C025_2022_04_14 and C034_2021_08_13 is " + cos_sim_C025_2022_04_14_C034_2021_08_13);	
		System.out.println("\nThe cosine similarity between C025_2022_04_14 and C042_2021_08_13 is " + cos_sim_C025_2022_04_14_C042_2021_08_13);
	}

}