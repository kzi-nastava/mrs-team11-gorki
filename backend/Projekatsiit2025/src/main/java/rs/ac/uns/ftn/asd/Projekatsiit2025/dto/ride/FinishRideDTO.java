package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

public class FinishRideDTO {
	
	private Long rideId;
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
