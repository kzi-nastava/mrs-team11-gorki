package ftn.mrs_team11_gorki.dto;

public class FinishedRideDTO {
    private Long rideId;
    private String rideStatus;     // FINISHED
    private String driverStatus;   // ACTIVE/BUSY
    private Boolean hasNextScheduledRide;

    public FinishedRideDTO() {}

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public String getRideStatus() { return rideStatus; }
    public void setRideStatus(String rideStatus) { this.rideStatus = rideStatus; }

    public String getDriverStatus() { return driverStatus; }
    public void setDriverStatus(String driverStatus) { this.driverStatus = driverStatus; }

    public Boolean getHasNextScheduledRide() { return hasNextScheduledRide; }
    public void setHasNextScheduledRide(Boolean hasNextScheduledRide) { this.hasNextScheduledRide = hasNextScheduledRide; }
}
