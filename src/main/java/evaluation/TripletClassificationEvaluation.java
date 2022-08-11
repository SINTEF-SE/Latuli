package evaluation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TripletClassificationEvaluation {
	
	public static void main(String[] args) throws IOException {

		/** RotatE **/
//		String neo4jDataFile = "./files/DATASETS/HubCentric/Pykeen/Rotate/neo4j_data.csv";
//		String lowPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Rotate/df_low.csv";
//		String highPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Rotate/df_high.csv";
//		String evaluationFile = "./files/DATASETS/HubCentric/Pykeen/Rotate/evaluationFile.csv";		
	
		
//		/** ComplEx **/
//		String neo4jDataFile = "./files/DATASETS/HubCentric/Pykeen/Complex/neo4j_data.csv";
//		String lowPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Complex/df_low.csv";
//		String highPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Complex/df_high.csv";
//		String evaluationFile = "./files/DATASETS/HubCentric/Pykeen/Complex/evaluationFile.csv";
		
//		/** TransE **/
//		String neo4jDataFile = "./files/DATASETS/HubCentric/Pykeen/Transe/neo4j_data.csv";
//		String lowPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Transe/df_low.csv";
//		String highPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Transe/df_high.csv";
//		String evaluationFile = "./files/DATASETS/HubCentric/Pykeen/Transe/evaluationFile.csv";
		
//		/** TucKER **/
//		String neo4jDataFile = "./files/DATASETS/HubCentric/Pykeen/Tucker/neo4j_data.csv";
//		String lowPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Tucker/df_low.csv";
//		String highPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Tucker/df_high.csv";
//		String evaluationFile = "./files/DATASETS/HubCentric/Pykeen/Tucker/evaluationFile.csv";
		
		/** ConvE **/
		String neo4jDataFile = "./files/DATASETS/HubCentric/Pykeen/Conve/neo4j_data.csv";
		String lowPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Conve/df_low.csv";
		String highPredictionsFile = "./files/DATASETS/HubCentric/Pykeen/Conve/df_high.csv";
		String evaluationFile = "./files/DATASETS/HubCentric/Pykeen/Conve/evaluationFile.csv";
		
		
		constructEvaluationHubs(neo4jDataFile, lowPredictionsFile, highPredictionsFile, evaluationFile);
		evaluate(evaluationFile);

	}
	
	private static void evaluate(String inputEvaluationFile) throws IOException {

		BufferedReader evaluationFile_br = null;
		String[] params = null;
		String line = null;

		try {
			evaluationFile_br = new BufferedReader(new FileReader(inputEvaluationFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		

		int total = 0;
		int numCorrect = 0;
		int numIncorrect = 0;
		
		List<EvaluatedHub> incorrectHubs = new ArrayList<EvaluatedHub>();
		EvaluatedHub incorrectHub = null;
		
		try {
			while ((line = evaluationFile_br.readLine()) != null) {
				total++;
				params = line.split(",");
				
				if (params[6].equals("Y")) {
					numCorrect++;

				} else {
					numIncorrect++;
					
					incorrectHub = new EvaluatedHub.EvaluatedHubBuilder()
							.setHubId(params[0])
							.setPykeenId(params[1])
							.setNeo4JId(params[2])
							.setLowPredictionScore(Double.parseDouble(params[3]))
							.setHighPredictionScore(Double.parseDouble(params[4]))
							.setRelativeToMaxCapacity(Double.parseDouble(params[5]))
							.setCorrectPrediction(params[6])
							.setDistance(Double.parseDouble(params[7]))
							.build();
					
					incorrectHubs.add(incorrectHub);
					
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			evaluationFile_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("There are " + total + " predictions");
		System.out.println("There are " + numCorrect + " correct predictions");
		System.out.println("There are " + numIncorrect + " false predictions");
		System.out.println("Correct ratio: " + correctRatio(numCorrect, total));
		System.out.println("Average distance for correct predictions: ");
		System.out.println("Average distance for incorrect predictions: ");
		System.out.println("Number of hub nodes with incorrect predictions: " + incorrectHubs.size());
		System.out.println("Hub nodes with incorrect predictions: ");
		
		for (EvaluatedHub e : incorrectHubs) {
			System.out.println(e.getHubId());
		}
		
	}
	
	private static double correctRatio (double correct, double total) {
		return correct / total;
	}
	
	private static double computeDistance(double lowCapPredScore, double highCapPredScore) {
		
		double distance = 0;
		
		if (lowCapPredScore > highCapPredScore) {
			distance = lowCapPredScore - highCapPredScore;
		} else {
			distance = highCapPredScore - lowCapPredScore;
		}
		
		return distance;
		
	}
	
	private static void constructEvaluationHubs (String neo4jDataFile, String lowPredictionsFile, String highPredictionsFile, String evaluationFile) throws IOException {
		
		List<EvaluatedHub> evaluatedHubList = new ArrayList<EvaluatedHub>();
		List<CapacityPrediction> lowCapacityPredictionsList = new ArrayList<CapacityPrediction>();
		List<CapacityPrediction> highCapacityPredictionsList = new ArrayList<CapacityPrediction>();

		EvaluatedHub evaluatedHub = null;		
		CapacityPrediction lowCapacityPrediction = null;
		CapacityPrediction highCapacityPrediction = null; 

		//import neo4j data from csv to use as a basis
		BufferedReader neo4j_br = null;
		String[] params = null;
		String line;

		try {
			neo4j_br = new BufferedReader(new FileReader(neo4jDataFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while ((line = neo4j_br.readLine()) != null) {

				params = line.split(",");
				evaluatedHub = new EvaluatedHub.EvaluatedHubBuilder()
						.setHubId(params[2])
						.setNeo4JId(params[1])
						.setRelativeToMaxCapacity(Double.parseDouble(params[3]))
						.build();

				evaluatedHubList.add(evaluatedHub);	

			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			neo4j_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}


		//import low capacity prediction scores from csv and append list of evaluated hubs
		BufferedReader low_br = null;
		params = null;
		line = null;

		try {
			low_br = new BufferedReader(new FileReader(lowPredictionsFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while ((line = low_br.readLine()) != null) {

				params = line.split(",");

				lowCapacityPrediction = new CapacityPrediction.CapacityPredictionBuilder()
						.setPykeenId(params[1])
						.setNeo4JId(params[2])
						.setTail(params[6])
						.setScore(Double.parseDouble(params[7]))
						.setLowOrHigh("low")
						.build();

				lowCapacityPredictionsList.add(lowCapacityPrediction);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			low_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}



		//import high capacity prediction scores and append list of evaluated hubs
		BufferedReader high_br = null;
		params = null;
		line = null;

		try {
			high_br = new BufferedReader(new FileReader(highPredictionsFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		try {
			while ((line = high_br.readLine()) != null) {

				params = line.split(",");

				highCapacityPrediction = new CapacityPrediction.CapacityPredictionBuilder()
						.setPykeenId(params[1])
						.setNeo4JId(params[2])
						.setTail(params[6])
						.setScore(Double.parseDouble(params[7]))
						.setLowOrHigh("high")
						.build();

				highCapacityPredictionsList.add(highCapacityPrediction);

			}

		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			high_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//create a uniform list of evaluated hubs that contain scores for low- and high capacity prediction scores
		List<EvaluatedHub> hub_low_high_list = new ArrayList<EvaluatedHub>();
		EvaluatedHub fh = null;

		for (EvaluatedHub h : evaluatedHubList) {

			for (CapacityPrediction lc : lowCapacityPredictionsList) {

				for (CapacityPrediction hc : highCapacityPredictionsList) {

					if (h.neo4JId.equals(lc.neo4JId) && h.neo4JId.equals(hc.neo4JId)) {

						fh = new EvaluatedHub.EvaluatedHubBuilder()
								.setHubId(h.getHubId())
								.setPykeenId(lc.getPykeenId())
								.setNeo4JId(lc.getNeo4JId())
								.setLowPredictionScore(lc.getScore())
								.setHighPredictionScore(hc.getScore())
								.setRelativeToMaxCapacity(h.relativeToMaxCapacity)
								.build();

						hub_low_high_list.add(fh);

					}
				}
			}

		}
		
		List<EvaluatedHub> complete_ehList = new ArrayList<EvaluatedHub>();
		
		EvaluatedHub eh = null;

		double lowPredictionScore;
		double highPredictionScore;
		double relativeToMaxCapacity;
		String correctPrediction = null;
		double distance;
		
		for (EvaluatedHub e : hub_low_high_list) {
			
			lowPredictionScore = e.getLowPredictionScore();
			highPredictionScore = e.getHighPredictionScore();
			relativeToMaxCapacity = e.getRelativeToMaxCapacity();
			
			if (lowPredictionScore > highPredictionScore && relativeToMaxCapacity >= 0.75) {
				correctPrediction = "Y";
				
			} else if (highPredictionScore > lowPredictionScore && relativeToMaxCapacity < 0.75) {
				correctPrediction = "Y";
				
			} else {
				
				correctPrediction = "N";
			}
			
			distance = computeDistance(lowPredictionScore, highPredictionScore);
			
			eh = new EvaluatedHub.EvaluatedHubBuilder()
					.setHubId(e.getHubId())
					.setPykeenId(e.getPykeenId())
					.setNeo4JId(e.getNeo4JId())
					.setLowPredictionScore(lowPredictionScore)
					.setHighPredictionScore(highPredictionScore)
					.setRelativeToMaxCapacity(relativeToMaxCapacity)
					.setCorrectPrediction(correctPrediction)
					.setDistance(distance)
					.build();

			complete_ehList.add(eh);
			
		}

		BufferedWriter hub_br = null;	

		try {
			hub_br = new BufferedWriter(new FileWriter(evaluationFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

		//hub_br.write("HubId,PykeenId,Neo4JId,LowPredictionScore,HighPredictionScore,RelativeToMaxCapacity,CorrectPrediction");
		//hub_br.newLine();

		for (EvaluatedHub evHub : complete_ehList) {

			hub_br.write(evHub.getHubId() + "," + evHub.getPykeenId() + "," + evHub.getNeo4JId() + "," + evHub.getLowPredictionScore() + ","
					+ evHub.getHighPredictionScore() + "," + evHub.getRelativeToMaxCapacity() + "," + evHub.getCorrectPrediction() + "," + evHub.getDistance());

			hub_br.newLine();
		}

		try {
			hub_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


	public static class CapacityPrediction {

		private String pykeenId;
		private String neo4JId;
		private String tail;
		private double score;
		private String lowOrHigh;

		private CapacityPrediction(CapacityPredictionBuilder builder) {
			this.pykeenId = builder.pykeenId;
			this.neo4JId = builder.neo4JId;
			this.tail = builder.tail;
			this.score = builder.score;
			this.lowOrHigh = builder.lowOrHigh;
		}


		public static class CapacityPredictionBuilder {

			private String pykeenId;
			private String neo4JId;
			private String tail;
			private double score;
			private String lowOrHigh;

			public CapacityPredictionBuilder() {}

			public CapacityPredictionBuilder setPykeenId(String pykeenId) {
				this.pykeenId = pykeenId;
				return this;
			}

			public CapacityPredictionBuilder setNeo4JId(String neo4jId) {
				neo4JId = neo4jId;
				return this;
			}

			public CapacityPredictionBuilder setTail(String tail) {
				this.tail = tail;
				return this;
			}

			public CapacityPredictionBuilder setScore(double score) {
				this.score = score;
				return this;
			}

			public CapacityPredictionBuilder setLowOrHigh(String lowOrHigh) {
				this.lowOrHigh = lowOrHigh;
				return this;
			}

			public CapacityPrediction build() {
				return new CapacityPrediction(this);
			}

		}


		public String getPykeenId() {
			return pykeenId;
		}


		public String getNeo4JId() {
			return neo4JId;
		}


		public String getTail() {
			return tail;
		}


		public double getScore() {
			return score;
		}


		public String getLowOrHigh() {
			return lowOrHigh;
		}

	}

	public static class EvaluatedHub {

		private String pykeenId;
		private String neo4JId;
		private String hubId;
		private double relativeToMaxCapacity;
		private double highPredictionScore;
		private double lowPredictionScore;
		private String correctPrediction;
		private double distance;

		private EvaluatedHub(EvaluatedHubBuilder builder) {
			this.pykeenId = builder.pykeenId;
			this.neo4JId = builder.neo4JId;
			this.hubId = builder.hubId;
			this.relativeToMaxCapacity = builder.relativeToMaxCapacity;
			this.highPredictionScore = builder.highPredictionScore;
			this.lowPredictionScore = builder.lowPredictionScore;
			this.correctPrediction = builder.correctPrediction;
			this.distance = builder.distance;
		}

		public static class EvaluatedHubBuilder {

			private String pykeenId;
			private String neo4JId;
			private String hubId;
			private double relativeToMaxCapacity;
			private double highPredictionScore;
			private double lowPredictionScore;
			private String correctPrediction;
			private double distance;

			public EvaluatedHubBuilder() {}

			public EvaluatedHubBuilder setPykeenId(String pykeenId) {
				this.pykeenId = pykeenId;
				return this;
			}

			public EvaluatedHubBuilder setNeo4JId(String neo4jId) {
				neo4JId = neo4jId;
				return this;
			}

			public EvaluatedHubBuilder setHubId(String hubId) {
				this.hubId = hubId;
				return this;
			}

			public EvaluatedHubBuilder setRelativeToMaxCapacity(double relativeToMaxCapacity) {
				this.relativeToMaxCapacity = relativeToMaxCapacity;
				return this;
			}

			public EvaluatedHubBuilder setHighPredictionScore(double highPredictionScore) {
				this.highPredictionScore = highPredictionScore;
				return this;
			}

			public EvaluatedHubBuilder setLowPredictionScore(double lowPredictionScore) {
				this.lowPredictionScore = lowPredictionScore;
				return this;
			}
			
			public EvaluatedHubBuilder setCorrectPrediction(String correctPrediction) {
				this.correctPrediction = correctPrediction;
				return this;
			}
			
			public EvaluatedHubBuilder setDistance(double distance) {
				this.distance = distance;
				return this;
			}

			public EvaluatedHub build() {
				return new EvaluatedHub(this);
			}

		}

		public String getPykeenId() {
			return pykeenId;
		}

		public String getNeo4JId() {
			return neo4JId;
		}

		public String getHubId() {
			return hubId;
		}

		public double getRelativeToMaxCapacity() {
			return relativeToMaxCapacity;
		}

		public double getHighPredictionScore() {
			return highPredictionScore;
		}

		public double getLowPredictionScore() {
			return lowPredictionScore;
		}

		public String getCorrectPrediction() {
			return correctPrediction;
		}
		
		public double getDistance() {
			return distance;
		}

	}

}
