package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;

public class GetRideTrackingDTO {
    private Long rideId;
    private Location currentLocation;
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
