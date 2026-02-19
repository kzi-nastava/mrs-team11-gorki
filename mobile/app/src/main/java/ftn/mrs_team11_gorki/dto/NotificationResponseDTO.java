package ftn.mrs_team11_gorki.dto;

import java.io.Serializable;

public class NotificationResponseDTO implements Serializable {

    private Long id;
    private String purpose;
    private String content;
    private String createdAt; // najbezbolnije: kao STRING (ISO sa backend-a)
    private Boolean read;

    public NotificationResponseDTO() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPurpose() { return purpose; }
    public void setPurpose(String purpose) { this.purpose = purpose; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public Boolean getRead() { return read; }
    public void setRead(Boolean read) { this.read = read; }
}