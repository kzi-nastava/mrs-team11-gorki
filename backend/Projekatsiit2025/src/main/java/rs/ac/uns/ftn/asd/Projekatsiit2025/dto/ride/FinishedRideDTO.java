package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

public class FinishedRideDTO {
	private Long rideId;
	private RideStatus rideStatus;
	private DriverStatus driverStatus;
	private Boolean hasNextScheduledRide;
	
	public FinishedRideDTO() {
		super();
	}
	public Long getRideId() {
		return rideId;
	}
	public void setRideId(Long rideId) {
		this.rideId = rideId;
	}
	public RideStatus getRideStatus() {
		return rideStatus;
	}
	public void setRideStatus(RideStatus rideStatus) {
		this.rideStatus = rideStatus;
	}
	public DriverStatus getDriverStatus() {
		return driverStatus;
	}
	public void setDriverStatus(DriverStatus driverStatus) {
		this.driverStatus = driverStatus;
	}
	public Boolean getHasNextScheduledRide() {
		return hasNextScheduledRide;
	}
	public void setHasNextScheduledRide(Boolean hasNextScheduledRide) {
		this.hasNextScheduledRide = hasNextScheduledRide;
	}
	
	
}
