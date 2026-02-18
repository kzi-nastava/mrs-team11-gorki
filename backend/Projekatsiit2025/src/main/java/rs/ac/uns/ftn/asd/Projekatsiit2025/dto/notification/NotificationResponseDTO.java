package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification;

import java.time.LocalDateTime;

public class NotificationResponseDTO {
    private Long id;
    private String purpose;
    private String content;
    private LocalDateTime createdAt;
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