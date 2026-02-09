package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

public class RideCancelRequestDTO {
    private String cancellationReason;
    private String cancelledBy;
    private Long actorId;

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public String getCancelledBy() { return cancelledBy; }
    public void setCancelledBy(String cancelledBy) { this.cancelledBy = cancelledBy; }

    public Long getActorId() { return actorId; }
    public void setActorId(Long actorId) { this.actorId = actorId; }
}
