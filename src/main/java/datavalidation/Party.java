package datavalidation;

public class Party {
	
	private String additionalPartyIdentification;
	private String gln;
	private String hashCode;
	private String partyName;
	private String addressDetail;
	private String street;
	private String code3;
	private String code2;
	private String location;
	private String postalCode;
	private String modifiedOn;
	private String isHub;
	private String isShipper;
	private String isCarrier;
	private String isConsignor;
	private String isReadOnly;
	private String barCodeFormat;
	private String simplifiedCode;
	private String originalDataSource;
	private String coordinates;
	
	private Party(PartyBuilder builder) {
		
		this.additionalPartyIdentification = builder.additionalPartyIdentification;
		this.gln = builder.gln;
		this.hashCode = builder.hashCode;
		this.partyName = builder.partyName;
		this.addressDetail = builder.addressDetail;
		this.street = builder.street;
		this.code3 = builder.code3;
		this.code2 = builder.code2;
		this.location = builder.location;
		this.postalCode = builder.postalCode;
		this.modifiedOn = builder.modifiedOn;
		this.isHub = builder.isHub;
		this.isShipper = builder.isShipper;
		this.isCarrier = builder.isCarrier;
		this.isConsignor = builder.isConsignor;
		this.isReadOnly = builder.isReadOnly;
		this.barCodeFormat = builder.barCodeFormat;
		this.simplifiedCode = builder.simplifiedCode;
		this.originalDataSource = builder.originalDataSource;
		this.coordinates = builder.coordinates;
		
	}
	
	public static class PartyBuilder {
		
		private String additionalPartyIdentification;
		private String gln;
		private String hashCode;
		private String partyName;
		private String addressDetail;
		private String street;
		private String code3;
		private String code2;
		private String location;
		private String postalCode;
		private String modifiedOn;
		private String isHub;
		private String isShipper;
		private String isCarrier;
		private String isConsignor;
		private String isReadOnly;
		private String barCodeFormat;
		private String simplifiedCode;
		private String originalDataSource;
		private String coordinates;
	
		
		public PartyBuilder() {}
		


	public PartyBuilder setAdditionalPartyIdentification(String additionalPartyIdentification) {
		this.additionalPartyIdentification = additionalPartyIdentification;
		return this;
	}

	public PartyBuilder setGLN(String gln) {
		this.gln = gln;
		return this;
	}

	public PartyBuilder setHashCode(String hashCode) {
		this.hashCode = hashCode;
		return this;
	}

	public PartyBuilder setPartyName(String partyName) {
		this.partyName = partyName;
		return this;
	}

	public PartyBuilder setAddressDetail(String addressDetail) {
		this.addressDetail = addressDetail;
		return this;
	}

	public PartyBuilder setStreet(String street) {
		this.street = street;
		return this;
	}

	public PartyBuilder setCode3(String code3) {
		this.code3 = code3;
		return this;
	}

	public PartyBuilder setCode2(String code2) {
		this.code2 = code2;
		return this;
	}

	public PartyBuilder setLocation(String location) {
		this.location = location;
		return this;
	}

	public PartyBuilder setPostalCode(String postalCode) {
		this.postalCode = postalCode;
		return this;
	}

	public PartyBuilder setModifiedOn(String modifiedOn) {
		this.modifiedOn = modifiedOn;
		return this;
	}

	public PartyBuilder setIsHub(String isHub) {
		this.isHub = isHub;
		return this;
	}

	public PartyBuilder setIsShipper(String isShipper) {
		this.isShipper = isShipper;
		return this;
	}

	public PartyBuilder setIsCarrier(String isCarrier) {
		this.isCarrier = isCarrier;
		return this;
	}

	public PartyBuilder setIsConsignor(String isConsignor) {
		this.isConsignor = isConsignor;
		return this;
	}

	public PartyBuilder setIsReadOnly(String isReadOnly) {
		this.isReadOnly = isReadOnly;
		return this;
	}

	public PartyBuilder setBarCodeFormat(String barCodeFormat) {
		this.barCodeFormat = barCodeFormat;
		return this;
	}

	public PartyBuilder setSimplifiedCode(String simplifiedCode) {
		this.simplifiedCode = simplifiedCode;
		return this;
	}

	public PartyBuilder setOriginalDataSource(String originalDataSource) {
		this.originalDataSource = originalDataSource;
		return this;
	}
	
	public PartyBuilder setCoordinates(String coordinates) {
		this.coordinates = coordinates;
		return this;
	}

	public Party build() {
		return new Party(this);
	}
	
	}

	public String getAdditionalPartyIdentification() {
		return additionalPartyIdentification;
	}

	public String getGln() {
		return gln;
	}

	public String getHashCode() {
		return hashCode;
	}

	public String getPartyName() {
		return partyName;
	}

	public String getAddressDetail() {
		return addressDetail;
	}

	public String getStreet() {
		return street;
	}

	public String getCode3() {
		return code3;
	}

	public String getCode2() {
		return code2;
	}

	public String getLocation() {
		return location;
	}

	public String getPostalCode() {
		return postalCode;
	}

	public String getModifiedOn() {
		return modifiedOn;
	}

	public String getIsHub() {
		return isHub;
	}

	public String getIsShipper() {
		return isShipper;
	}

	public String getIsCarrier() {
		return isCarrier;
	}

	public String getIsConsignor() {
		return isConsignor;
	}

	public String getIsReadOnly() {
		return isReadOnly;
	}

	public String getBarCodeFormat() {
		return barCodeFormat;
	}

	public String getSimplifiedCode() {
		return simplifiedCode;
	}

	public String getOriginalDataSource() {
		return originalDataSource;
	}

	public String getCoordinates() {
		return coordinates;
	}
	
	

}
