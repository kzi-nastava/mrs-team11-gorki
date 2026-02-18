package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;

public class GetRideTrackingDTO {
	
	@Positive(message = "Ride id must be positive")
	@NotNull(message = "Ride id is required")
    private Long rideId;
	
    private Location currentLocation;
    @NotNull(message = "Estimated time to destination is required")
    @PositiveOrZero(message = "Estimated time must be >= 0")
    private double estimatedTimeToDestination;

    public GetRideTrackingDTO() {
        super();
    }
    public Long getRideId() {
        return rideId;
    }
    public void setRideId(Long rideId) {
        this.rideId = rideId;
    }
    public Location getCurrentLocation() {
		return currentLocation;
	}
	public void setCurrentLocation(Location currentLocation) {
		this.currentLocation = currentLocation;
	}
	public double getEstimatedTimeToDestination() {
        return estimatedTimeToDestination;
    }
    public void setEstimatedTimeToDestination(double estimatedTimeToDestination) {
        this.estimatedTimeToDestination = estimatedTimeToDestination;
    }
}
