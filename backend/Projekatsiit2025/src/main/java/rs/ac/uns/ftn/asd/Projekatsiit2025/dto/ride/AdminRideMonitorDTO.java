package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.GetDriverInfoDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;

public class AdminRideMonitorDTO {
	  private GetRideDTO ride;
	  private GetDriverInfoDTO driver;
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
