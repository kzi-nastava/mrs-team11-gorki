package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

public class RideCancelResponseDTO {

	@Positive(message = "Ride id must be positive")
	private Long rideId;

	@NotNull(message = "Ride status is required")
	private RideStatus rideStatus;

	@Size(max = 500, message = "Cancellation reason too long")
	private String cancellationReason;

	@Size(max = 100, message = "CancelledBy value too long")
	private String cancelledBy;


    public RideCancelResponseDTO(Long rideId, RideStatus rideStatus, String cancellationReason, String cancelledBy) {
        this.rideId = rideId;
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
        this.rideStatus = rideStatus;
    }
    
    public RideCancelResponseDTO() {}

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
