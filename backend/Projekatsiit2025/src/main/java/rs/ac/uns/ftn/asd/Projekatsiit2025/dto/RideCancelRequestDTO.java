package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

public class RideCancelRequestDTO {

    private Long rideId;
    private RideStatus rideStatus;
    private String cancellationReason;
    private String cancelledBy;

    public RideCancelRequestDTO(Long rideId, RideStatus rideStatus, String cancellationReason, String cancelledBy) {
        this.rideId = rideId;
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
        this.rideStatus = rideStatus;
    }
    
    public RideCancelRequestDTO() {}

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

    public String getCancellationReason() {
        return cancellationReason;
    }

    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }
}
