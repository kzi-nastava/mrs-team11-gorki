package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class RideCancelRequestDTO {
	
	@NotNull(message = "CacnellationReason is required")
    private String cancellationReason;
	@NotNull(message = "Cacnellation by is required")
    private String cancelledBy;
	
	@Positive(message = "Actor id must be postive")
	@NotNull(message = "Actor id by required")
    private Long actorId;

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public String getCancelledBy() { return cancelledBy; }
    public void setCancelledBy(String cancelledBy) { this.cancelledBy = cancelledBy; }

    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }
}
