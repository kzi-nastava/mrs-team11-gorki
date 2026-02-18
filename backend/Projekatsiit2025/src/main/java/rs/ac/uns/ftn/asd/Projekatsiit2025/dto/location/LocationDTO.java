package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class LocationDTO {
	
	@DecimalMin(value = "-90.0", message = "Latitude must be >= -90")
	@DecimalMax(value = "90.0", message = "Latitude must be <= 90")
	private double latitude;
	
	@DecimalMin(value = "-180.0", message = "Longitude must be >= -180")
	@DecimalMax(value = "180.0", message = "Longitude must be <= 180")
	private double longitude;
	
	@NotNull(message = "Address is required")
	private String address;
	
	public LocationDTO() {
		super();
	}
	
	public LocationDTO(double latitude, double longitude, String address) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
	}
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}
