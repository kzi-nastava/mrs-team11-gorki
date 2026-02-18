package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class NotificationDTO {
	@NotNull(message = "Type is required")
    private String type;
	@NotNull(message = "RideId is required")
	@Positive(message = "RideId must be positive")
    private Long rideId;
	@NotNull(message = "Title is required")
    private String title;
	@NotNull(message = "Message is required")
    private String message;
	
	@NotNull(message = "Time is required")
	@FutureOrPresent(message = "Time must be in present or future")
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