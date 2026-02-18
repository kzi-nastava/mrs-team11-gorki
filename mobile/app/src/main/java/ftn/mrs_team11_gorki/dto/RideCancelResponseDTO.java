package ftn.mrs_team11_gorki.dto;

public class RideCancelResponseDTO {
    private Long rideId;
    private String rideStatus;
    private String cancellationReason;
    private String cancelledBy;

    public Long getRideId() { return rideId; }
    public String getRideStatus() { return rideStatus; }
    public String getCancellationReason() { return cancellationReason; }
    public String getCancelledBy() { return cancelledBy; }
}
