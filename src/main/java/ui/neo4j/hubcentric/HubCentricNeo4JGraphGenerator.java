package ui.neo4j.hubcentric;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HubCentricNeo4JGraphGenerator {

	final static double CP_THRESHOLD = 0.75;
	
	private static double round (double value, int precision) { 
	    int scale = (int) Math.pow(10, precision); 
	    return (double) Math.round(value * scale) / scale; 
	} 
	
	private static String computeDailyWeightRatio (double max, double daily) {
		
		//round to 2 decimals
		double ratio = round((daily / max), 2);
		String dailyWeightRatio = null;
		
		if (ratio <= 0.33) {
			dailyWeightRatio = "LowDailyWeightRatio";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			dailyWeightRatio = "MediumDailyWeightRatio";
		} else {
			dailyWeightRatio = "HighDailyWeightRatio";
		}
		
		return dailyWeightRatio;
	}
	
	private static String computeDailyVolumeRatio (double max, double daily) {
		
		//round to 2 decimals
		double ratio = round((daily / max), 2);
		String dailyVolumeRatio = null;
		
		if (ratio <= 0.33) {
			dailyVolumeRatio = "LowDailyVolumeRatio";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			dailyVolumeRatio = "MediumDailyVolumeRatio";
		} else {
			dailyVolumeRatio = "HighDailyVolumeRatio";
		}
		
		return dailyVolumeRatio;
	}
	
	private static String computeDailyShipmentsRatio (int max, int daily) {
		
		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String dailyShipmentsRatio = null;
		
		if (ratio <= 0.33) {
			dailyShipmentsRatio = "LowDailyShipmentsRatio";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			dailyShipmentsRatio = "MediumDailyShipmentsRatio";
		} else {
			dailyShipmentsRatio = "HighDailyShipmentsRatio";
		}
		
		return dailyShipmentsRatio;
	}
	
	private static String computeDailyPalletsBuiltRatio (int max, int daily) {
		
		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String dailyPalletsBuiltRatio = null;
		
		if (ratio <= 0.33) {
			dailyPalletsBuiltRatio = "LowDailyPalletsBuiltRatio";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			dailyPalletsBuiltRatio = "MediumDailyPalletsBuiltRatio";
		} else {
			dailyPalletsBuiltRatio = "HighDailyPalletsBuiltRatio";
		}
		
		return dailyPalletsBuiltRatio;
	}
	
	private static String computeDailyBoxesProcessedRatio (int max, int daily) {
		
		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String dailyBoxesProcessedRatio = null;
		
		if (ratio <= 0.33) {
			dailyBoxesProcessedRatio = "LowDailyBoxesProcessedRatio";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			dailyBoxesProcessedRatio = "MediumDailyBoxesProcessedRatio";
		} else {
			dailyBoxesProcessedRatio = "HighDailyBoxesProcessedRatio";
		}
		
		return dailyBoxesProcessedRatio;
	}

	
	public static void main(String[] args) throws IOException, ParseException {

		String waves = "./files/DATASETS/HubCentric/waves.csv";
		String xdlu = "./files/DATASETS/HubCentric/xdlu.csv";
		String max = "./files/DATASETS/HubCentric/max.csv";

		//get data from waves
		List<HubData> wavesHubDataList = aggregateWaveData(waves);
		
		//get data from xdlus
		List<HubData> xdluHubDataList = aggregateXDLUData(xdlu);
		
		//get max data
		List<HubData> maxList = aggregateMaxData(max);

		//iterate all three lists and create a common list
		List<HubData> commonList = createCommonList(wavesHubDataList, xdluHubDataList, maxList);

		//compute relativeMaxCap
		List<HubData> completeList = computeRelativeToMaxCapacity(commonList, CP_THRESHOLD);

		//print to different csv files
		String hub_csv = "./files/DATASETS/HubCentric/Nodes/Hub.csv";
		String wave_csv = "./files/DATASETS/HubCentric/Nodes/Wave.csv";
		String crossdockloadingunit_csv = "./files/DATASETS/HubCentric/Nodes/CDLU.csv";
		String lowCP_csv = "./files/DATASETS/HubCentric/Nodes/LowCP.csv";
		String highCP_csv = "./files/DATASETS/HubCentric/Nodes/HighCP.csv";

		String processesCrossDockLoadingUnit_csv = "./files/DATASETS/HubCentric/Relationships/PROCESSES_CROSSDOCKLOADINGUNITS.csv";
		String processesWave_csv = "./files/DATASETS/HubCentric/Relationships/PROCESSES_WAVES.csv";

		//adding relations to low lcp and high lcp
		String hasLowCP_csv = "./files/DATASETS/HubCentric/Relationships/HAS_LOW_CP.csv";
		String hasHighCP_csv = "./files/DATASETS/HubCentric/Relationships/HAS_HIGH_CP.csv";

		BufferedWriter hub_br = null;
		BufferedWriter wave_br = null;
		BufferedWriter cdlu_br = null;
		BufferedWriter proc_cdlu_br = null;
		BufferedWriter proc_wave_br = null;
		BufferedWriter highCP_br = null;
		BufferedWriter lowCP_br = null;
		BufferedWriter hasHighCP_br = null;
		BufferedWriter hasLowCP_br = null;


		try {
			hub_br = new BufferedWriter(new FileWriter(hub_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			wave_br = new BufferedWriter(new FileWriter(wave_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			cdlu_br = new BufferedWriter(new FileWriter(crossdockloadingunit_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			proc_cdlu_br = new BufferedWriter(new FileWriter(processesCrossDockLoadingUnit_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			proc_wave_br = new BufferedWriter(new FileWriter(processesWave_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			highCP_br = new BufferedWriter(new FileWriter(highCP_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			lowCP_br = new BufferedWriter(new FileWriter(lowCP_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasHighCP_br = new BufferedWriter(new FileWriter(hasHighCP_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasLowCP_br = new BufferedWriter(new FileWriter(hasLowCP_csv));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int counter = 0;
		String waveId = null;
		String cdluId = null;
		String lowCPId = null;
		String highCPId = null;

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = null;
		Date dateFrom = new SimpleDateFormat("yyyy-MM-dd").parse("2020-01-01");
		Date dateTo = new SimpleDateFormat("yyyy-MM-dd").parse("2021-12-31");

		for (HubData hd : completeList) {

			counter++;
			waveId = "wave" + counter;
			cdluId = "cdlu" + counter;
			lowCPId = "lowCP" + counter;
			highCPId = "highCP" + counter;


			currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(hd.getDate());


			if (currentDate.after(dateFrom) && currentDate.before(dateTo)) {

				hub_br.write(hd.getHubId() + "," + hd.getHub() + "," + hd.getDate() + "," + hd.getRelativeToMaxCapacity() + "," + "Hub");
				hub_br.newLine();
				cdlu_br.write(cdluId + "_" + hd.getHubId() + "," + hd.getTotalVolume() + "," + hd.getTotalWeight() + "," + "CrossDockLoadingUnit");
				cdlu_br.newLine();
				wave_br.write(waveId + "_" + hd.getHubId() + "," + hd.getQttShipments() + "," + hd.getQttBoxesProcessed() + "," + hd.getQttPalletsBuilt() + "," + "Wave");
				wave_br.newLine();
				proc_cdlu_br.write(hd.getHubId() + "," + cdluId + "_" + hd.getHubId() + "," + "PROCESSES_CROSSDOCKLOADINGUNIT");
				proc_cdlu_br.newLine();
				proc_wave_br.write(hd.getHubId() + "," + waveId + "_" + hd.getHubId() + "," + "PROCESSES_WAVE");
				proc_wave_br.newLine();

				if (hd.highCP == true) {

					highCP_br.write(highCPId + "_" + hd.getHubId() + "," + hd.getHub() + "," + hd.getDate() + "," + "HighCapacityPrediction");
					highCP_br.newLine();

					hasHighCP_br.write(hd.getHubId() + "," + highCPId + "_" + hd.getHubId() + "," + "HAS_HIGH_CP");
					hasHighCP_br.newLine();

				} else {

					lowCP_br.write(lowCPId + "_" + hd.getHubId() + "," + hd.getHub() + "," + hd.getDate() + "," + "LowCapacityPrediction");
					lowCP_br.newLine();

					hasLowCP_br.write(hd.getHubId() + "," + lowCPId + "_" + hd.getHubId() + "," + "HAS_LOW_CP");
					hasLowCP_br.newLine();
				}

			}

		}

		try {
			hub_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			wave_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			cdlu_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			proc_cdlu_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			proc_wave_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			highCP_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			lowCP_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasHighCP_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			hasLowCP_br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static List<HubData> aggregateWaveData (String waves) {

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

	public static List<HubData> aggregateXDLUData (String xdlu) {

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

	public static List<HubData> aggregateMaxData (String max) {

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

		try {
			while ((line = br.readLine()) != null) {

				params = line.split(",");
				hub = params[0];
				maxShipments = Integer.parseInt(params[1]);
				maxVolume = Double.parseDouble(params[2]);
				maxWeight = Double.parseDouble(params[3]);

				maxData = new HubData.HubDataBuilder()
						.setHub(hub)
						.setMaxShipments(maxShipments)
						.setMaxVolume(maxVolume)
						.setMaxWeight(maxWeight)
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
	
	public static List<HubData> createCommonList (List<HubData> wavesHubDataList, List<HubData> xdluHubDataList, List<HubData> maxList) {

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
								.build();

						commonList.add(commonHubData);
					}
				}
			}
		}

		return commonList;

	}

	public static List<HubData> computeRelativeToMaxCapacity (List<HubData> hubDataList, double CP_THRESHOLD) {

		List<HubData> revisedHubDataList = new ArrayList<HubData>();

		HubData revisedHubData = null;
		double relativeMax = 0;
		double relativeMaxShipment = 0;
		double relativeMaxVolume = 0;
		double relativeMaxWeight = 0;

		//attribute to hold whether the relativeToMaxCapacity is > 0.5
		boolean highCP = false;

		//relativeMaxCapacity is computed as the average of shipments, volume and weight
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

			System.out.println("RelativeToMaxCapacity score for " + hd.getHubId() + " is: " + relativeMax);

			//if the relativeToMaxCapacity is below the set threshold, then highCP is true
			if (relativeMax < CP_THRESHOLD) {
				highCP = true;
			} else {
				highCP = false;
			}

			System.out.println("HighCP is " + highCP);

			//TODO: Add attributes for dailyWeightRatio, dailyVolumeRatio, dailyShipmentsRatio, dailyPalletsBuiltRatio, dailyBoxesProcessedRatio, 
			//terminalEquipment (fixed per hub), and terminalSize (fixed per hub), 
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
					.setHighCP(highCP)
					.build();

			revisedHubDataList.add(revisedHubData);


		}

		return revisedHubDataList;

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
		private boolean highCP;

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
			this.highCP = builder.highCP;
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
			private boolean highCP;

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

			public HubDataBuilder setHighCP(boolean highCP) {
				this.highCP = highCP;
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

		public boolean isHighCP() {
			return highCP;
		}

	}

}
