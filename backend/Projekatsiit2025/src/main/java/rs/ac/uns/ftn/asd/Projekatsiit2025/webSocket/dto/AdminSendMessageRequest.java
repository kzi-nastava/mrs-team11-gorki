package rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto;

import jakarta.validation.constraints.NotNull;

public class AdminSendMessageRequest {
	
	@NotNull(message = "Admin id is required")
	private Long chatId;
	@NotNull(message = "Content is required")
    private String content;

    public AdminSendMessageRequest() {}

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
