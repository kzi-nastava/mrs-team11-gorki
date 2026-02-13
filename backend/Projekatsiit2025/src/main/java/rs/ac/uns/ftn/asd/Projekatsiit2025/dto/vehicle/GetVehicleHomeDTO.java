package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.DriverStatusRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;

public class GetVehicleHomeDTO {
	private Long id;
	private LocationDTO currentLocation;
	private DriverStatus vehicleAvailability;
	
	public GetVehicleHomeDTO() {
		super();
	}
	
	public GetVehicleHomeDTO(Long id, LocationDTO currentLocation, DriverStatus driverStatus) {
		super();
		this.id = id;
		this.currentLocation = currentLocation;
		this.vehicleAvailability = driverStatus;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocationDTO getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(LocationDTO currentLocation) {
		this.currentLocation = currentLocation;
	}
	public DriverStatus getVehicleAvailability() {
		return vehicleAvailability;
	}
	public void setVehicleAvailability(DriverStatus driverStatus) {
		this.vehicleAvailability = driverStatus;
	}
	
}
