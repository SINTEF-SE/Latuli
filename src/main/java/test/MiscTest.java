package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MiscTest {

	private static double round (double value, int precision) { 
		int scale = (int) Math.pow(10, precision); 
		return (double) Math.round(value * scale) / scale; 
	} 
	
	private static String computeWeightThroughputMeasurement (double max, double daily) {

		//round to 2 decimals
		double ratio = round((daily / max), 2);
		String weightThroughputMeasurement = null;

		if (ratio <= 0.33) {
			weightThroughputMeasurement = "LowWeightThroughput";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			weightThroughputMeasurement = "MediumWeightThroughput";
		} else {
			weightThroughputMeasurement = "HighWeightThroughput";
		}

		return weightThroughputMeasurement;
	}

	private static String computeVolumeThroughputMeasurement (double max, double daily) {

		//round to 2 decimals
		double ratio = round((daily / max), 2);
		String volumeTroughputMeasurement = null;

		if (ratio <= 0.33) {
			volumeTroughputMeasurement = "LowVolumeThroughput";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			volumeTroughputMeasurement = "MediumVolumeThroughput";
		} else {
			volumeTroughputMeasurement = "HighVolumeThroughput";
		}

		return volumeTroughputMeasurement;
	}

	private static String computeShipmentsThroughputMeasurement (int max, int daily) {

		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String shipmentsThroughputMeasurement = null;

		if (ratio <= 0.33) {
			shipmentsThroughputMeasurement = "LowShipmentsThroughput";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			shipmentsThroughputMeasurement = "MediumShipmentsThroughput";
		} else {
			shipmentsThroughputMeasurement = "HighShipmentsThroughput";
		}

		return shipmentsThroughputMeasurement;
	}

	private static String computePalletsThroughputMeasurement (int max, int daily) {

		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String palletsThroughputMeasurement = null;

		if (ratio <= 0.33) {
			palletsThroughputMeasurement = "LowPalletsThroughput";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			palletsThroughputMeasurement = "MediumPalletsThroughput";
		} else {
			palletsThroughputMeasurement = "HighPalletsThroughput";
		}

		return palletsThroughputMeasurement;
	}

	private static String computeBoxesThroughputMeasurement (int max, int daily) {

		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String boxesThroughputMeasurement = null;

		if (ratio <= 0.33) {
			boxesThroughputMeasurement = "LowBoxesThroughput";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			boxesThroughputMeasurement = "MediumBoxesThroughput";
		} else {
			boxesThroughputMeasurement = "HighBoxesThroughput";
		}

		return boxesThroughputMeasurement;
	}


	private static String computeDailyShipmentsRatio (int max, int daily) {

		//round to 2 decimals
		double ratio = round(((double)daily / (double)max), 2);
		String dailyShipmentsRatio = null;

		System.out.println("Ratio is " + ratio);

		if (ratio <= 0.33) {
			dailyShipmentsRatio = "LowDailyShipmentsRatio";
		} else if (ratio >= 0.34 && ratio <= 0.66) {
			dailyShipmentsRatio = "MediumDailyShipmentsRatio";
		} else {
			dailyShipmentsRatio = "HighDailyShipmentsRatio";
		}

		return dailyShipmentsRatio;
	}

	public static String getRandomTerminalEquipment() {

		List<String> terminalEquipmentList = new ArrayList<String>();
		terminalEquipmentList.add("FullyAutomatedTerminalEquipment");
		terminalEquipmentList.add("PartiallyAutomatedTerminalEquipment");
		terminalEquipmentList.add("OnlyManualTerminalEquipment");	

		Random random = new Random();
		return terminalEquipmentList.get(random.nextInt(terminalEquipmentList.size()));
	}
	
	public static String getRandomTerminalSize() {

		List<String> terminalSizeList = new ArrayList<String>();
		terminalSizeList.add("LargeTerminal");
		terminalSizeList.add("MediumSizedTerminal");
		terminalSizeList.add("SmallTerminal");	

		Random random = new Random();
		return terminalSizeList.get(random.nextInt(terminalSizeList.size()));
	}

	public static void main(String[] args) {

		int max = 100;
		int daily = 67;

		System.out.println("Daily shipment ratio is " + computeDailyShipmentsRatio(max, daily));

		List<String> TerminalEquipmentList = new ArrayList<String>();
		TerminalEquipmentList.add("FullyAutomatedTerminalEquipment");
		TerminalEquipmentList.add("PartiallyAutomatedTerminalEquipment");
		TerminalEquipmentList.add("OnlyManualTerminalEquipment");

		System.out.println("Terminal Equipment is " + getRandomTerminalEquipment());


	}

}
