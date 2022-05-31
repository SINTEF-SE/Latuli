package geocoding;

public class Location {
	
	private String street;
	private String city;
	private String country;
	private String coordinates;
	
	private Location(LocationBuilder builder) {
		this.street = builder.street;
		this.city = builder.city;
		this.country = builder.country;
		this.coordinates = builder.coordinates;
	}

	public static class LocationBuilder {
		
		private String street;
		private String city;
		private String country;
		private String coordinates;
		
		public LocationBuilder() {}

		public LocationBuilder setStreet(String street) {
			this.street = street;
			return this;
		}

		public LocationBuilder setCity(String city) {
			this.city = city;
			return this;
		}

		public LocationBuilder setCountry(String country) {
			this.country = country;
			return this;
		}

		public LocationBuilder setCoordinates(String coordinates) {
			this.coordinates = coordinates;
			return this;
		}
		
		public Location build() {
			return new Location(this);
		}		
	}

	public String getStreet() {
		return street;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getCoordinates() {
		return coordinates;
	}
	
	
}
