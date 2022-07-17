package dataaggregation;

import java.time.LocalDate;

public class HubPeriodicPerformance {
	
	private LocalDate date;	
	private int numProcessedShipments;
	private int numReconstructionOperations;
	private int qttBoxesProcessed;
	private int qttPalletsProcessed;
	private int qttReconstructedParcelsProcessed;
	private int qttReconstructedPalletsProcessed;
	private int numWavesProcessed;
	private double outboundConsignmentTotalVolume;
	private double relativeCapacity;
	
private HubPeriodicPerformance(HubPeriodicPerformanceBuilder builder) {
		
		this.date = builder.date;
		this.numProcessedShipments = builder.numProcessedShipments;
		this.numReconstructionOperations = builder.numReconstructionOperations;
		this.qttBoxesProcessed = builder.qttBoxesProcessed;
		this.qttPalletsProcessed = builder.qttPalletsProcessed;
		this.qttReconstructedParcelsProcessed = builder.qttReconstructedParcelsProcessed;
		this.qttReconstructedPalletsProcessed = builder.qttReconstructedPalletsProcessed;
		this.numWavesProcessed = builder.numWavesProcessed;
		this.outboundConsignmentTotalVolume = builder.outboundConsignmentTotalVolume;
		this.relativeCapacity = builder.relativeCapacity;
	}

public static class HubPeriodicPerformanceBuilder {
	
	private LocalDate date;
	private int numProcessedShipments;
	private int numReconstructionOperations;
	private int qttBoxesProcessed;
	private int qttPalletsProcessed;
	private int qttReconstructedParcelsProcessed;
	private int qttReconstructedPalletsProcessed;
	private int numWavesProcessed;
	private double outboundConsignmentTotalVolume;
	private double relativeCapacity;
	
	public HubPeriodicPerformanceBuilder() {}

	public HubPeriodicPerformanceBuilder setDate(LocalDate date) {
		this.date = date;
		return this;
	}

	public HubPeriodicPerformanceBuilder setNumProcessedShipments(int numProcessedShipments) {
		this.numProcessedShipments = numProcessedShipments;
		return this;
	}


	public HubPeriodicPerformanceBuilder setNumReconstructionOperations(int numReconstructionOperations) {
		this.numReconstructionOperations = numReconstructionOperations;
		return this;
	}

	public HubPeriodicPerformanceBuilder setQttBoxesProcessed(int qttBoxesProcessed) {
		this.qttBoxesProcessed = qttBoxesProcessed;
		return this;
	}

	public HubPeriodicPerformanceBuilder setQttPalletsProcessed(int qttPalletsProcessed) {
		this.qttPalletsProcessed = qttPalletsProcessed;
		return this;
	}

	public HubPeriodicPerformanceBuilder setQttReconstructedParcelsProcessed(int qttReconstructedParcelsProcessed) {
		this.qttReconstructedParcelsProcessed = qttReconstructedParcelsProcessed;
		return this;
	}

	public HubPeriodicPerformanceBuilder setQttReconstructedPalletsProcessed(int qttReconstructedPalletsProcessed) {
		this.qttReconstructedPalletsProcessed = qttReconstructedPalletsProcessed;
		return this;
	}

	public HubPeriodicPerformanceBuilder setNumWavesProcessed(int numWavesProcessed) {
		this.numWavesProcessed = numWavesProcessed;
		return this;
	}

	public HubPeriodicPerformanceBuilder setOutboundConsignmentTotalVolume(double outboundConsignmentTotalVolume) {
		this.outboundConsignmentTotalVolume = outboundConsignmentTotalVolume;
		return this;
	}
	
	public HubPeriodicPerformanceBuilder setRelativeCapacity(double relativeCapacity) {
		this.relativeCapacity = relativeCapacity;
		return this;
	}
	
	public HubPeriodicPerformance build() {
		return new HubPeriodicPerformance(this);
	}

}

public LocalDate getDate() {
	return date;
}

public int getNumProcessedShipments() {
	return numProcessedShipments;
}

public int getNumReconstructionOperations() {
	return numReconstructionOperations;
}

public int getQttBoxesProcessed() {
	return qttBoxesProcessed;
}

public int getQttPalletsProcessed() {
	return qttPalletsProcessed;
}

public int getQttReconstructedParcelsProcessed() {
	return qttReconstructedParcelsProcessed;
}

public int getQttReconstructedPalletsProcessed() {
	return qttReconstructedPalletsProcessed;
}

public int getNumWavesProcessed() {
	return numWavesProcessed;
}

public double getOutboundConsignmentTotalVolume() {
	return outboundConsignmentTotalVolume;
}

public double getRelativeCapacity() {
	return relativeCapacity;
}



}
