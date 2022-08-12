package ui.kge.neo4j.simple;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SimpleNeo4JGraphGenerator {

	final static double CP_THRESHOLD = 0.75;

	public static void main(String[] args) throws IOException, ParseException {

		String maxFile = "./files/DATASETS/HubCentric/max.csv";
		String wavesFile = "./files/DATASETS/HubCentric/waves.csv";
		String xdluFile = "./files/DATASETS/HubCentric/xdlu.csv";
		
		String trainingFromDate = "2020-01-01";
		String trainingToDate = "2021-12-31";
		String testFromDate = "2022-01-01";
		String testToDate = "2022-05-01";
		
		createKnowledgeGraph(maxFile, wavesFile, xdluFile, trainingFromDate, trainingToDate, testFromDate, testToDate);

	}

	public static void createKnowledgeGraph (String maxFile, String wavesFile, String xdluFile, String trainingFrom, String trainingTo, String testFrom, String testTo) throws ParseException, IOException {

		//get data from waves
		List<HubData> wavesHubDataList = aggregateWaveData(wavesFile);

		//get data from xdlus
		List<HubData> xdluHubDataList = aggregateXDLUData(xdluFile);

		//get max data
		List<HubData> maxList = aggregateMaxData(maxFile);

		//iterate all three lists and create a common list
		List<HubData> commonList = createCommonList(wavesHubDataList, xdluHubDataList, maxList);

		//compute relativeMaxCap
		List<HubData> completeList = computeRelativeToMaxCapacity(commonList, CP_THRESHOLD);

		//print to different csv files
		String hub_csv = "./files/DATASETS/HubCentric/Nodes/Hub.csv";

		String hasThroughput_csv = "./files/DATASETS/HubCentric/Relationships/hasThroughput.csv";
		String hasCapacityPrediction_csv = "./files/DATASETS/HubCentric/Relationships/hasCapacityPrediction.csv";


		BufferedWriter hub_br = null;		
		BufferedWriter hasThroughput_br = null;
		BufferedWriter hasCapacityPrediction_br = null;

		try {
			hub_br = new BufferedWriter(new FileWriter(hub_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasThroughput_br = new BufferedWriter(new FileWriter(hasThroughput_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasCapacityPrediction_br = new BufferedWriter(new FileWriter(hasCapacityPrediction_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}


		Date currentDate = null;
		Date trainingDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(trainingFrom);
		Date trainingDateTo = new SimpleDateFormat("yyyy-MM-dd").parse(trainingTo);
		Date testDateFrom = new SimpleDateFormat("yyyy-MM-dd").parse(testFrom);
		Date testDateTo = new SimpleDateFormat("yyyy-MM-dd").parse(testTo);

		for (HubData hd : completeList) {

			currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(hd.getDate());

			if (currentDate.after(testDateFrom) && currentDate.before(testDateTo)) {

				hub_br.write(hd.getHubId() + "," + hd.getHub() + "," + hd.getDate() + "," + hd.getRelativeToMaxCapacity() + "," + "Hub");
				hub_br.newLine();

				//shipments throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getShipmentsThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//volume throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getVolumeThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//weight throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getWeightThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//pallets throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getPalletsThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//boxes throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getBoxesThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

			} else if (currentDate.after(trainingDateFrom) && currentDate.before(trainingDateTo)) {

				hub_br.write(hd.getHubId() + "," + hd.getHub() + "," + hd.getDate() + "," + hd.getRelativeToMaxCapacity() + "," + "Hub");
				hub_br.newLine();

				//shipments throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getShipmentsThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//volume throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getVolumeThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//weight throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getWeightThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//pallets throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getPalletsThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//boxes throughput
				hasThroughput_br.write(hd.getHubId() + "," + hd.getBoxesThroughputMeasurement() + "," + "hasThroughput");
				hasThroughput_br.newLine();

				//write capacity prediction
				hasCapacityPrediction_br.write(hd.getHubId() + "," + hd.getCapacityPrediction() + "," + "hasCapacityPrediction");
				hasCapacityPrediction_br.newLine();				

			}

		}


		try {
			hub_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasThroughput_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasCapacityPrediction_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


	private static List<HubData> aggregateWaveData (String waves) {

		//get data from waves
		List<HubData> wavesHubDataList = new ArrayList<HubData>();

		//iterate all rows in waves
		BufferedReader br = null;
		String[] params = null;
		String line;	
		HubData wavesHubData = null;

		try {
			br = new BufferedReader(new FileReader(waves));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		String hubAndDate = null;
		String hub;
		String date = null;
		int qttShipments = 0;
		int qttBoxes = 0;
		int qttPallets = 0;

		//TODO: Is this the place to add static hub data (terminal equipment and terminal size)?

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");
				hubAndDate = params[0] + "_" + params[1];
				hub = params[0];
				date = params[1];
				qttShipments = Integer.parseInt(params[2]);
				qttBoxes = Integer.parseInt(params[3]);
				qttPallets = Integer.parseInt(params[4]);

				wavesHubData = new HubData.HubDataBuilder()
						.setHubId(hubAndDate)
						.setHub(hub)
						.setDate(date)
						.setQttShipments(qttShipments)
						.setQttBoxesProcessed(qttBoxes)
						.setQttPalletsBuilt(qttPallets)
						.build();

				wavesHubDataList.add(wavesHubData);


			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return wavesHubDataList;

	}

	private static List<HubData> aggregateXDLUData (String xdlu) {

		//get data from xdlu
		List<HubData> xdluHubDataList = new ArrayList<HubData>();

		//iterate all rows in xdlu
		HubData xdluHubData = null;

		BufferedReader br = null;
		String[] params = null;
		String line;	

		String hubAndDate = null;
		String hub;
		String date = null;

		try {
			br = new BufferedReader(new FileReader(xdlu));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		double volume = 0;
		double weight = 0;

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");
				hubAndDate = params[0] + "_" + params[1];
				hub = params[0];
				date = params[1];
				volume = Double.parseDouble(params[2]);
				weight = Double.parseDouble(params[3]);

				xdluHubData = new HubData.HubDataBuilder()
						.setHubId(hubAndDate)
						.setHub(hub)
						.setDate(date)
						.setTotalVolume(volume)
						.setTotalWeight(weight)
						.build();

				xdluHubDataList.add(xdluHubData);


			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return xdluHubDataList;
	}

	private static List<HubData> aggregateMaxData (String max) {

		//get max data
		List<HubData> maxList = new ArrayList<HubData>();

		BufferedReader br = null;
		String[] params = null;
		String line;	
		String hub;

		//iterate all rows in xdlu
		HubData maxData = null;

		try {
			br = new BufferedReader(new FileReader(max));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		int maxShipments = 0;
		double maxVolume = 0;
		double maxWeight = 0;
		int maxPallets = 0;
		int maxBoxes = 0;

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");
				hub = params[0];
				maxShipments = Integer.parseInt(params[1]);
				maxVolume = Double.parseDouble(params[2]);
				maxWeight = Double.parseDouble(params[3]);
				maxPallets = Integer.parseInt(params[4]);
				maxBoxes = Integer.parseInt(params[5]);

				maxData = new HubData.HubDataBuilder()
						.setHub(hub)
						.setMaxShipments(maxShipments)
						.setMaxVolume(maxVolume)
						.setMaxWeight(maxWeight)
						.setMaxPallets(maxPallets)
						.setMaxBoxes(maxBoxes)
						.build();

				maxList.add(maxData);


			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return maxList;

	}

	private static List<HubData> createCommonList (List<HubData> wavesHubDataList, List<HubData> xdluHubDataList, List<HubData> maxList) {

		//iterate all three lists and create a common list
		List<HubData> commonList = new ArrayList<HubData>();
		HubData commonHubData = null;

		//double relativeCapacity = 0;

		for (HubData wavesData : wavesHubDataList) {
			for (HubData xdluData : xdluHubDataList) {
				for (HubData maxXdluAndWavesData : maxList) {

					if ((wavesData.getHubId().equals(xdluData.getHubId()) 
							&& (wavesData.getHub().equals(maxXdluAndWavesData.getHub())))) {
						commonHubData = new HubData.HubDataBuilder()
								.setHubId(wavesData.getHubId())
								.setHub(wavesData.getHub())
								.setDate(wavesData.getDate())
								.setQttShipments(wavesData.getQttShipments())
								.setQttBoxesProcessed(wavesData.getQttBoxesProcessed())
								.setQttPalletsBuilt(wavesData.getQttPalletsBuilt())
								.setTotalVolume(xdluData.getTotalVolume())
								.setTotalWeight(xdluData.getTotalWeight())
								.setMaxShipments(maxXdluAndWavesData.getMaxShipments())
								.setMaxVolume(maxXdluAndWavesData.getMaxVolume())
								.setMaxWeight(maxXdluAndWavesData.getMaxWeight())
								.setMaxPallets(maxXdluAndWavesData.getMaxPallets())
								.setMaxBoxes(maxXdluAndWavesData.getMaxBoxes())
								.build();

						commonList.add(commonHubData);
					}
				}
			}
		}

		return commonList;

	}

	private static List<HubData> computeRelativeToMaxCapacity (List<HubData> hubDataList, double CP_THRESHOLD) {

		List<HubData> revisedHubDataList = new ArrayList<HubData>();

		HubData revisedHubData = null;
		double relativeMax = 0;
		double relativeMaxShipment = 0;
		double relativeMaxVolume = 0;
		double relativeMaxWeight = 0;

		String shipmentsThroughput = null;
		String weightThroughput = null;
		String volumeThroughput = null;
		String palletsThroughput = null;
		String boxesThroughput = null;
		String capacityPrediction = null;

		//attribute to hold whether the relativeToMaxCapacity is > 0.5
		boolean highCP = false;

		//relativeMaxCapacity is computed as the average of shipments, volume and weight
		//TODO: implement these computations in separate method
		for (HubData hd : hubDataList) {

			//tweaking these since some discrepancy in modifiedOn (waves resp xdlu) results in daily max being larger than periodic max
			if ((double)hd.getQttShipments() > (double)hd.getMaxShipments()) {
				relativeMaxShipment = 1;
			} else {
				relativeMaxShipment = (double)hd.getQttShipments() / (double)hd.getMaxShipments();
			}

			if (hd.getTotalVolume() > hd.getMaxVolume()) {
				relativeMaxVolume = 1;
			} else {
				relativeMaxVolume = hd.getTotalVolume() / hd.getMaxVolume();
			}


			if (hd.getTotalWeight() > hd.getMaxWeight()) {
				relativeMaxWeight = 1;
			} else {
				relativeMaxWeight = hd.getTotalWeight() / hd.getMaxWeight();
			}

			relativeMax = (relativeMaxShipment + relativeMaxVolume + relativeMaxWeight) / 3;

			//if the relativeToMaxCapacity is below the set threshold, then highCP is true
			if (relativeMax < CP_THRESHOLD) {
				highCP = true;
			} else {
				highCP = false;
			}

			shipmentsThroughput = computeShipmentsThroughputMeasurement(hd.getMaxShipments(), hd.getQttShipments(), CP_THRESHOLD);
			weightThroughput = computeWeightThroughputMeasurement(hd.getMaxWeight(), hd.getTotalWeight(), CP_THRESHOLD);
			volumeThroughput = computeVolumeThroughputMeasurement(hd.getMaxVolume(), hd.getTotalVolume(), CP_THRESHOLD);
			palletsThroughput = computePalletsThroughputMeasurement(hd.getMaxPallets(), hd.getQttPalletsBuilt(), CP_THRESHOLD);
			boxesThroughput = computeBoxesThroughputMeasurement(hd.getMaxBoxes(), hd.getQttBoxesProcessed(), CP_THRESHOLD);

			if (highCP) {
				capacityPrediction = "HighCapacityPrediction";
			} else {
				capacityPrediction = "LowCapacityPrediction";
			}



			revisedHubData = new HubData.HubDataBuilder()
					.setHubId(hd.getHubId())
					.setHub(hd.getHub())
					.setDate(hd.getDate())
					.setRelativeToMaxCapacity(relativeMax)
					.setTotalVolume(hd.getTotalVolume())
					.setTotalWeight(hd.getTotalWeight())
					.setQttShipments(hd.getQttShipments())
					.setQttBoxesProcessed(hd.getQttBoxesProcessed())
					.setQttPalletsBuilt(hd.getQttPalletsBuilt())
					.setMaxShipments(hd.getMaxShipments())
					.setMaxVolume(hd.getMaxVolume())
					.setMaxWeight(hd.getMaxWeight())
					.setMaxPallets(hd.getMaxPallets())
					.setMaxBoxes(hd.getMaxBoxes())
					.setShipmentsThroughputMeasurement(shipmentsThroughput)
					.setWeightThroughputMeasurement(weightThroughput)
					.setVolumeThroughputMeasurement(volumeThroughput)
					.setPalletsThroughputMeasurement(palletsThroughput)
					.setBoxesThroughputMeasurement(boxesThroughput)
					.setHighCP(highCP)
					.setCapacityPrediction(capacityPrediction)
					.build();

			revisedHubDataList.add(revisedHubData);


		}

		return revisedHubDataList;

	}

	private static String computeWeightThroughputMeasurement (double max, double daily, double threshold) {

		//round to 2 decimals
		double ratio = round((daily / max), 2);
		String weightThroughputMeasurement = null;

		if (ratio < threshold) {
			weightThroughputMeasurement = "LowWeightThroughput";
		} else {
			weightThroughputMeasurement = "HighWeightThroughput";
		}

		return weightThroughputMeasurement;
	}

	private static String computeVolumeThroughputMeasurement (double max, double daily, double threshold) {

		//round to 2 decimals
		double ratio = round((daily / max), 2);
		String volumeTroughputMeasurement = null;

		if (ratio < threshold) {
			volumeTroughputMeasurement = "LowVolumeThroughput";	
		} else {
			volumeTroughputMeasurement = "HighVolumeThroughput";
		}

		return volumeTroughputMeasurement;
	}

	private static String computeShipmentsThroughputMeasurement (int max, int daily, double threshold) {

		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String shipmentsThroughputMeasurement = null;

		if (ratio < threshold) {
			shipmentsThroughputMeasurement = "LowShipmentsThroughput";		
		} else {
			shipmentsThroughputMeasurement = "HighShipmentsThroughput";
		}

		return shipmentsThroughputMeasurement;
	}

	private static String computePalletsThroughputMeasurement (int max, int daily, double threshold) {

		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String palletsThroughputMeasurement = null;

		if (ratio < threshold) {
			palletsThroughputMeasurement = "LowPalletsThroughput";
		} else {
			palletsThroughputMeasurement = "HighPalletsThroughput";
		}

		return palletsThroughputMeasurement;
	}

	private static String computeBoxesThroughputMeasurement (int max, int daily, double threshold) {

		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String boxesThroughputMeasurement = null;

		if (ratio < threshold) {
			boxesThroughputMeasurement = "LowBoxesThroughput";
		} else {
			boxesThroughputMeasurement = "HighBoxesThroughput";
		}

		return boxesThroughputMeasurement;
	}

	private static double round (double value, int precision) { 
		int scale = (int) Math.pow(10, precision); 
		return (double) Math.round(value * scale) / scale; 
	} 

	public static class HubData {
		private String hubId;
		private String hub;
		private String date;
		private double relativeToMaxCapacity;
		private double totalVolume;
		private double totalWeight;
		private int qttShipments;
		private int qttBoxesProcessed;
		private int qttPalletsBuilt;
		private int maxShipments;
		private double maxVolume;
		private double maxWeight;
		private int maxPallets;
		private int maxBoxes;
		private boolean highCP;
		private String weightThroughputMeasurement;
		private String volumeThroughputMeasurement;
		private String shipmentsThroughputMeasurement;
		private String palletsThroughputMeasurement;
		private String boxesThroughputMeasurement;
		private String capacityPrediction;


		private HubData(HubDataBuilder builder) {
			this.hubId = builder.hubId;
			this.hub = builder.hub;
			this.date = builder.date;
			this.relativeToMaxCapacity = builder.relativeToMaxCapacity;
			this.totalVolume = builder.totalVolume;
			this.totalWeight = builder.totalWeight;
			this.qttShipments = builder.qttShipments;
			this.qttBoxesProcessed = builder.qttBoxesProcessed;
			this.qttPalletsBuilt = builder.qttPalletsBuilt;
			this.maxShipments = builder.maxShipments;
			this.maxVolume = builder.maxVolume;
			this.maxWeight = builder.maxWeight;
			this.maxPallets = builder.maxPallets;
			this.maxBoxes = builder.maxBoxes;
			this.highCP = builder.highCP;
			this.weightThroughputMeasurement = builder.weightThroughputMeasurement;
			this.volumeThroughputMeasurement = builder.volumeThroughputMeasurement;
			this.shipmentsThroughputMeasurement = builder.shipmentsThroughputMeasurement;
			this.palletsThroughputMeasurement = builder.palletsThroughputMeasurement;
			this.boxesThroughputMeasurement = builder.boxesThroughputMeasurement;
			this.capacityPrediction = builder.capacityPrediction;
		}

		public static class HubDataBuilder {

			private String hubId;
			private String hub;
			private String date;
			private double relativeToMaxCapacity;
			private double totalVolume;
			private double totalWeight;
			private int qttShipments;
			private int qttBoxesProcessed;
			private int qttPalletsBuilt;
			private int maxShipments;
			private double maxVolume;
			private double maxWeight;
			private int maxPallets;
			private int maxBoxes;
			private boolean highCP;
			private String weightThroughputMeasurement;
			private String volumeThroughputMeasurement;
			private String shipmentsThroughputMeasurement;
			private String palletsThroughputMeasurement;
			private String boxesThroughputMeasurement;
			private String capacityPrediction;

			public HubDataBuilder() {}

			public HubDataBuilder setHubId(String hubId) {
				this.hubId = hubId;
				return this;
			}

			public HubDataBuilder setHub(String hub) {
				this.hub = hub;
				return this;
			}
			public HubDataBuilder setDate(String date) {
				this.date = date;
				return this;
			}

			public HubDataBuilder setRelativeToMaxCapacity(double relativeToMaxCapacity) {
				this.relativeToMaxCapacity = relativeToMaxCapacity;
				return this;
			}

			public HubDataBuilder setTotalVolume(double totalVolume) {
				this.totalVolume = totalVolume;
				return this;
			}

			public HubDataBuilder setTotalWeight(double totalWeight) {
				this.totalWeight = totalWeight;
				return this;
			}

			public HubDataBuilder setQttShipments(int qttShipments) {
				this.qttShipments = qttShipments;
				return this;
			}

			public HubDataBuilder setQttBoxesProcessed(int qttBoxesProcessed) {
				this.qttBoxesProcessed = qttBoxesProcessed;
				return this;
			}

			public HubDataBuilder setQttPalletsBuilt(int qttPalletsBuilt) {
				this.qttPalletsBuilt = qttPalletsBuilt;
				return this;
			}

			public HubDataBuilder setMaxShipments(int maxShipments) {
				this.maxShipments = maxShipments;
				return this;
			}

			public HubDataBuilder setMaxVolume(double maxVolume) {
				this.maxVolume = maxVolume;
				return this;
			}

			public HubDataBuilder setMaxWeight(double maxWeight) {
				this.maxWeight = maxWeight;
				return this;
			}

			public HubDataBuilder setMaxPallets(int maxPallets) {
				this.maxPallets = maxPallets;
				return this;
			}

			public HubDataBuilder setMaxBoxes(int maxBoxes) {
				this.maxBoxes = maxBoxes;
				return this;
			}

			public HubDataBuilder setHighCP(boolean highCP) {
				this.highCP = highCP;
				return this;
			}

			public HubDataBuilder setWeightThroughputMeasurement(String weightThroughputMeasurement) {
				this.weightThroughputMeasurement = weightThroughputMeasurement;
				return this;
			}

			public HubDataBuilder setVolumeThroughputMeasurement(String volumeThroughputMeasurement) {
				this.volumeThroughputMeasurement = volumeThroughputMeasurement;
				return this;
			}

			public HubDataBuilder setShipmentsThroughputMeasurement(String shipmentsThroughputMeasurement) {
				this.shipmentsThroughputMeasurement = shipmentsThroughputMeasurement;
				return this;
			}

			public HubDataBuilder setPalletsThroughputMeasurement(String palletsThroughputMeasurement) {
				this.palletsThroughputMeasurement = palletsThroughputMeasurement;
				return this;
			}

			public HubDataBuilder setBoxesThroughputMeasurement(String boxesThroughputMeasurement) {
				this.boxesThroughputMeasurement = boxesThroughputMeasurement;
				return this;
			}

			public HubDataBuilder setCapacityPrediction(String capacityPrediction) {
				this.capacityPrediction = capacityPrediction;
				return this;
			}

			public HubData build() {
				return new HubData(this);
			}
		}

		public String getHubId() {
			return hubId;
		}

		public String getHub() {
			return hub;
		}

		public String getDate() {
			return date;
		}

		public double getRelativeToMaxCapacity() {
			return relativeToMaxCapacity;
		}

		public double getTotalVolume() {
			return totalVolume;
		}

		public double getTotalWeight() {
			return totalWeight;
		}

		public int getQttShipments() {
			return qttShipments;
		}

		public int getQttBoxesProcessed() {
			return qttBoxesProcessed;
		}

		public int getQttPalletsBuilt() {
			return qttPalletsBuilt;
		}

		public int getMaxShipments() {
			return maxShipments;
		}

		public double getMaxVolume() {
			return maxVolume;
		}

		public double getMaxWeight() {
			return maxWeight;
		}

		public int getMaxPallets() {
			return maxPallets;
		}

		public int getMaxBoxes() {
			return maxBoxes;
		}

		public boolean isHighCP() {
			return highCP;
		}

		public String getWeightThroughputMeasurement() {
			return weightThroughputMeasurement;
		}

		public String getVolumeThroughputMeasurement() {
			return volumeThroughputMeasurement;
		}

		public String getShipmentsThroughputMeasurement() {
			return shipmentsThroughputMeasurement;
		}

		public String getPalletsThroughputMeasurement() {
			return palletsThroughputMeasurement;
		}

		public String getBoxesThroughputMeasurement() {
			return boxesThroughputMeasurement;
		}

		public String getCapacityPrediction() {
			return capacityPrediction;
		}

	}

}
