package dataaggregation;

public class Capacity {
	
	private double historicalMaxCapacity;
	
	private Capacity(CapacityBuilder builder) {
		
		this.historicalMaxCapacity = builder.historicalMaxCapacity;
		
	}
	
	public static class CapacityBuilder {
		
		private double historicalMaxCapacity;
		
		public CapacityBuilder() {}
		
		public CapacityBuilder setHistoricalMaxCapacity(double historicalMaxCapacity) {
			this.historicalMaxCapacity = historicalMaxCapacity;
			return this;
		}
		
		public Capacity build() {
			return new Capacity(this);
		}

	}

	public double getHistoricalMaxCapacity() {
		return historicalMaxCapacity;
	}	

}

	

	
