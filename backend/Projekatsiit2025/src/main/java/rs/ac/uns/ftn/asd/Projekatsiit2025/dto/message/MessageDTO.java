package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.message;

import java.time.LocalDateTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;

public class MessageDTO {
	
	@NotNull(message = "Sender is required")
	private String sender;
	@NotNull(message = "Content is required")
    private String content;
	@NotNull(message = "Time is required")
	@FutureOrPresent(message = "Time must be in present or future")
    private LocalDateTime timeStamp;

    public MessageDTO() {}

    public MessageDTO(String sender, String content, LocalDateTime timeStamp) {
        this.sender = sender;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public LocalDateTime getTimeStamp() { return timeStamp; }
    public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
}
