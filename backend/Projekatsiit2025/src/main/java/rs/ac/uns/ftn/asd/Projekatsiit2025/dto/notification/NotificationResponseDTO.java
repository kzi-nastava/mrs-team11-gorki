package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class NotificationResponseDTO {
	
	@Positive(message = "Id must be positive")
	@NotNull(message = "Id is required")
    private Long id;
	@NotNull(message = "Purpose is required")
    private String purpose;
	@NotNull(message = "Content is required")
    private String content;
	
	@NotNull(message = "Time is required")
	@FutureOrPresent(message = "Time must be in present or future")
    private LocalDateTime createdAt;
	@NotNull(message = "Active flag is required")
    private Boolean read;

    public NotificationResponseDTO() {}

    public NotificationResponseDTO(Long id, String purpose, String content, LocalDateTime createdAt, Boolean read) {
        this.id = id;
        this.purpose = purpose;
        this.content = content;
        this.createdAt = createdAt;
        this.read = read;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public Boolean getRead() { return read; }
    public void setRead(Boolean read) { this.read = read; }
}