package rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto;

import java.time.LocalDateTime;

public class PanicEventDTO {
    private Long rideId;
    private String triggeredBy; // "DRIVER" | "PASSENGER" (opciono)
    private LocalDateTime time;

    public PanicEventDTO() {}

    public PanicEventDTO(Long rideId, String triggeredBy, LocalDateTime time) {
        this.rideId = rideId;
        this.triggeredBy = triggeredBy;
        this.time = time;
    }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public String getTriggeredBy() { return triggeredBy; }
    public void setTriggeredBy(String triggeredBy) { this.triggeredBy = triggeredBy; }

    public LocalDateTime getTime() { return time; }
    public void setTime(LocalDateTime time) { this.time = time; }
}
