package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.GetDriverInfoDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;

public class AdminRideMonitorDTO {
	@NotNull(message = "Ride is required")
	@Valid
	private GetRideDTO ride;

	@NotNull(message = "Driver info is required")
	@Valid
	private GetDriverInfoDTO driver;

	@NotNull(message = "Current location is required")
	@Valid
	private LocationDTO currentLocation;

	  
	  public AdminRideMonitorDTO() {
		  super();
	  }

	public GetRideDTO getRide() {
		return ride;
	}

	public void setRide(GetRideDTO ride) {
		this.ride = ride;
	}

	public GetDriverInfoDTO getDriver() {
		return driver;
	}

	public void setDriver(GetDriverInfoDTO driver) {
		this.driver = driver;
	}

	public LocationDTO getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(LocationDTO currentLocation) {
		this.currentLocation = currentLocation;
	}
	  
	  
}
