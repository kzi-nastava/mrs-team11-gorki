package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;

public class CreatedDriverDTO {
	private CreatedUserDTO user;
	private CreatedVehicleDTO vehicle;
	private DriverStatus status;
	
	public CreatedDriverDTO() {
		super();
	}
	
	public CreatedDriverDTO(CreatedUserDTO user, CreatedVehicleDTO vehicle, DriverStatus status) {
		super();
		this.user = user;
		this.vehicle = vehicle;
		this.status = status;
	}
	
	public CreatedUserDTO getUser() {
		return user;
	}
	public void setUser(CreatedUserDTO user) {
		this.user = user;
	}
	public CreatedVehicleDTO getVehicle() {
		return vehicle;
	}
	public void setVehicle(CreatedVehicleDTO vehicle) {
		this.vehicle = vehicle;
	}

	public DriverStatus getStatus() {
		return status;
	}

	public void setStatus(DriverStatus status) {
		this.status = status;
	}
	
	
}
