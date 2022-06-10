package dataaggregation;

public class Hub {
	
	private String date;
	private String hubId;
	private int numProcessedShipments;
	private int numProcessedXDocLoadingUnits;
	private int numReconstructionOperations;
	private int qttBoxesProcessed;
	private int qttPalletsProcessed;
	private int qttReconstructedParcelsProcessed;
	private int qttReconstructedPalletsProcessed;
	private int numWavesProcessed;
	private double outboundConsignmentTotalWeight;
	private double outboundConsignmentTotalVolume;
	

	private Hub(HubBuilder builder) {
		
		this.date = builder.date;
		this.hubId = builder.hubId;
		this.numProcessedShipments = builder.numProcessedShipments;
		this.numProcessedXDocLoadingUnits = builder.numProcessedXDocLoadingUnits;
		this.numReconstructionOperations = builder.numReconstructionOperations;
		this.qttBoxesProcessed = builder.qttBoxesProcessed;
		this.qttPalletsProcessed = builder.qttPalletsProcessed;
		this.qttReconstructedParcelsProcessed = builder.qttReconstructedParcelsProcessed;
		this.qttReconstructedPalletsProcessed = builder.qttReconstructedPalletsProcessed;
		this.numWavesProcessed = builder.numWavesProcessed;
		this.outboundConsignmentTotalWeight = builder.outboundConsignmentTotalWeight;
		this.outboundConsignmentTotalVolume = builder.outboundConsignmentTotalVolume;
	}
	
	public static class HubBuilder {
		
		private String date;
		private String hubId;
		private int numProcessedShipments;
		private int numProcessedXDocLoadingUnits;
		private int numReconstructionOperations;
		private int qttBoxesProcessed;
		private int qttPalletsProcessed;
		private int qttReconstructedParcelsProcessed;
		private int qttReconstructedPalletsProcessed;
		private int numWavesProcessed;
		private double outboundConsignmentTotalWeight;
		private double outboundConsignmentTotalVolume;
		
		public HubBuilder() {}

		public HubBuilder setDate(String date) {
			this.date = date;
			return this;
		}

		public HubBuilder setHubId(String hubId) {
			this.hubId = hubId;
			return this;
		}

		public HubBuilder setNumProcessedShipments(int numProcessedShipments) {
			this.numProcessedShipments = numProcessedShipments;
			return this;
		}

		public HubBuilder setNumProcessedXDocLoadingUnits(int numProcessedXDocLoadingUnits) {
			this.numProcessedXDocLoadingUnits = numProcessedXDocLoadingUnits;
			return this;
		}

		public HubBuilder setNumReconstructionOperations(int numReconstructionOperations) {
			this.numReconstructionOperations = numReconstructionOperations;
			return this;
		}

		public HubBuilder setQttBoxesProcessed(int qttBoxesProcessed) {
			this.qttBoxesProcessed = qttBoxesProcessed;
			return this;
		}

		public HubBuilder setQttPalletsProcessed(int qttPalletsProcessed) {
			this.qttPalletsProcessed = qttPalletsProcessed;
			return this;
		}

		public HubBuilder setQttReconstructedParcelsProcessed(int qttReconstructedParcelsProcessed) {
			this.qttReconstructedParcelsProcessed = qttReconstructedParcelsProcessed;
			return this;
		}

		public HubBuilder setQttReconstructedPalletsProcessed(int qttReconstructedPalletsProcessed) {
			this.qttReconstructedPalletsProcessed = qttReconstructedPalletsProcessed;
			return this;
		}

		public HubBuilder setNumWavesProcessed(int numWavesProcessed) {
			this.numWavesProcessed = numWavesProcessed;
			return this;
		}

		public HubBuilder setOutboundConsignmentTotalWeight(double outboundConsignmentTotalWeight) {
			this.outboundConsignmentTotalWeight = outboundConsignmentTotalWeight;
			return this;
		}

		public HubBuilder setOutboundConsignmentTotalVolume(double outboundConsignmentTotalVolume) {
			this.outboundConsignmentTotalVolume = outboundConsignmentTotalVolume;
			return this;
		}
		
		public Hub build() {
			return new Hub(this);
		}

	}

	public String getDate() {
		return date;
	}

	public String getHubId() {
		return hubId;
	}

	public int getNumProcessedShipments() {
		return numProcessedShipments;
	}

	public int getNumProcessedXDocLoadingUnits() {
		return numProcessedXDocLoadingUnits;
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

	public double getOutboundConsignmentTotalWeight() {
		return outboundConsignmentTotalWeight;
	}

	public double getOutboundConsignmentTotalVolume() {
		return outboundConsignmentTotalVolume;
	}
	
	
}
