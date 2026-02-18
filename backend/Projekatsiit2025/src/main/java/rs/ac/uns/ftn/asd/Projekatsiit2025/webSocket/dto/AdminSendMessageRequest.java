package rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto;

public class AdminSendMessageRequest {
	private Long chatId;
    private String content;

    public AdminSendMessageRequest() {}

    public Long getChatId() { return chatId; }
    public void setChatId(Long chatId) { this.chatId = chatId; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
