package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification;

import java.time.LocalDateTime;

public class NotificationDTO {
    private String type;
    private Long rideId;
    private String title;
    private String message;
    private LocalDateTime createdAt;

    public NotificationDTO() {}

    public NotificationDTO(String type, Long rideId, String title, String message, LocalDateTime createdAt) {
        this.type = type;
        this.rideId = rideId;
        this.title = title;
        this.message = message;
        this.createdAt = createdAt;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Long getRideId() { return rideId; }
    public void setRideId(Long rideId) { this.rideId = rideId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}