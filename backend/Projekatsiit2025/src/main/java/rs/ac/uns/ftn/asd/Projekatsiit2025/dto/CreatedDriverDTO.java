package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

public class CreatedDriverDTO {
	private CreatedUserDTO user;
	private CreatedVehicleDTO vehicle;
	
	public CreatedDriverDTO() {
		super();
	}
	
	public CreatedDriverDTO(CreatedUserDTO user, CreatedVehicleDTO vehicle) {
		super();
		this.user = user;
		this.vehicle = vehicle;
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
	
}
