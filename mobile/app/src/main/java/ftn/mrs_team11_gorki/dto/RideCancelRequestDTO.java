package ftn.mrs_team11_gorki.dto;

public class RideCancelRequestDTO {
    private String cancellationReason;
    private String cancelledBy; // "DRIVER" ili "PASSENGER"
    private Long actorId;

    public RideCancelRequestDTO(String cancellationReason, String cancelledBy, Long actorId) {
        this.cancellationReason = cancellationReason;
        this.cancelledBy = cancelledBy;
        this.actorId = actorId;
    }

    public String getCancellationReason() { return cancellationReason; }
    public String getCancelledBy() { return cancelledBy; }
    public Long getActorId() { return actorId; }
}
