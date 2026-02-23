package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class FinishRideDTO {
	
	@Positive(message = "Ride id must be positive")
	@NotNull(message = "Ride id is required")
	private Long rideId;
	
	@NotNull(message = "Paid flag is required")
	private Boolean paid;
	
	public FinishRideDTO() {
		super();
	}
	public Long getRideId() {
		return rideId;
	}
	public void setRideId(Long rideId) {
		this.rideId = rideId;
	}
	public Boolean getPaid() {
		return paid;
	}
	public void setPaid(Boolean paid) {
		this.paid = paid;
	}
	
	
}
