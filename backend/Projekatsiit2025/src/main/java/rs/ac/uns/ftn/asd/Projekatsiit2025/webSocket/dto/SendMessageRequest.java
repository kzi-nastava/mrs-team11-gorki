package rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto;

import jakarta.validation.constraints.NotNull;

public class SendMessageRequest {
	@NotNull(message = "Content is required")
	private String content;

    public SendMessageRequest() {}

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
