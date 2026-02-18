package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;

public class GetVehicleHomeDTO {
	@Positive(message = "Id must be positive")
	private Long id;

	@NotNull(message = "Current location is required")
	@Valid
	private LocationDTO currentLocation;

	@NotNull(message = "Vehicle availability is required")
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
