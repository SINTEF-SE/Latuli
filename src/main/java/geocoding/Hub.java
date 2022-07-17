package geocoding;

public class Hub {
	
	private String additionalPartyIdentification;
	private String addressCode;
	private String coordinates;
	private String latitude;
	private String longitude;
	private String label;
	
	
	private Hub(HubBuilder builder) {
		this.additionalPartyIdentification = builder.additionalPartyIdentification;
		this.addressCode = builder.addressCode;
		this.coordinates = builder.coordinates;
		this.latitude = builder.latitude;
		this.longitude = builder.longitude;
		this.label = builder.label;
	}
	
	public static class HubBuilder {
		
		private String additionalPartyIdentification;
		private String addressCode;
		private String coordinates;
		private String latitude;
		private String longitude;
		private String label;
		
		public HubBuilder() {}

		public HubBuilder setAdditionalPartyIdentification(String additionalPartyIdentification) {
			this.additionalPartyIdentification = additionalPartyIdentification;
			return this;
		}

		public HubBuilder setAddressCode(String addressCode) {
			this.addressCode = addressCode;
			return this;
		}
		
		public HubBuilder setCoordinates(String coordinates) {
			this.coordinates = coordinates;
			return this;
		}

		public HubBuilder setLatitude(String latitude) {
			this.latitude = latitude;
			return this;
		}

		public HubBuilder setLongitude(String longitude) {
			this.longitude = longitude;
			return this;
		}

		public HubBuilder setLabel(String label) {
			this.label = label;
			return this;
		}
		
		public Hub build() {
			return new Hub(this);
		}	
		
		
	}

	public String getAdditionalPartyIdentification() {
		return additionalPartyIdentification;
	}

	public String getAddressCode() {
		return addressCode;
	}
	
	public String getCoordinates() {
		return coordinates;
	}

	public String getLatitude() {
		return latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public String getLabel() {
		return label;
	}


}
