package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.vehicle.GetVehicleDTO;

public class GetDriverInfoDTO {
	
	@Valid
	@NotNull(message = "User is required")
	private GetUserDTO user;
	@NotNull(message = "Activity is required")
	private double activityLast24h;
	@Valid
	@NotNull(message = "Vehicle is required")
	private GetVehicleDTO vehicle;
	
	public GetDriverInfoDTO(GetUserDTO user, double activityLast24h, GetVehicleDTO vehicle) {
		super();
		this.user = user;
		this.activityLast24h = activityLast24h;
		this.vehicle = vehicle;
	}

	public GetDriverInfoDTO() {
		super();
	}
	
	public GetVehicleDTO getVehicle() {
		return vehicle;
	}

	public void setVehicle(GetVehicleDTO vehicle) {
		this.vehicle = vehicle;
	}

	public GetUserDTO getUser() {
		return user;
	}
	public void setUser(GetUserDTO user) {
		this.user = user;
	}
	public double getActivityLast24h() {
		return activityLast24h;
	}
	public void setActivityLast24h(double activityLast24h) {
		this.activityLast24h = activityLast24h;
	}
}
