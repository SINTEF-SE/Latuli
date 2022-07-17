package dataaggregation;

import java.time.LocalDate;

public class HubAggregation {
	
	private String hubId;
	private HubPeriodicPerformance hubPeriodicPerformance;
	private Capacity capacity;
	

	private HubAggregation(HubBuilder builder) {
		
		this.hubId = builder.hubId;
		this.hubPeriodicPerformance = builder.hubPeriodicPerformance;
		this.capacity = builder.capacity;
	}
	
	public static class HubBuilder {
		
		private String hubId;
		
		private HubPeriodicPerformance hubPeriodicPerformance;
		private Capacity capacity;
		
		public HubBuilder() {}

		public HubBuilder setHubId(String hubId) {
			this.hubId = hubId;
			return this;
		}
		
		public HubBuilder setHubPeriodicPerformance (HubPeriodicPerformance hubPeriodicPerformance) {
			this.hubPeriodicPerformance = hubPeriodicPerformance;
			return this;
		}
		
		public HubBuilder setCapacity(Capacity capacity) {
			this.capacity = capacity;
			return this;
		}
		
		public HubAggregation build() {
			return new HubAggregation(this);
		}

	}


	public String getHubId() {
		return hubId;
	}
	

	public HubPeriodicPerformance getHubPeriodicPerformance() {
		return hubPeriodicPerformance;
	}


	public Capacity getCapacity() {
		return capacity;
	}
	
	
	
	
}
