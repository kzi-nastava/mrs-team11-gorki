package ftn.mrs_team11_gorki.dto;

public class AdminSendMessageRequest {
    private long chatId;
    private String content;

    public AdminSendMessageRequest(long chatId, String content) {
        this.chatId = chatId;
        this.content = content;
    }

    public long getChatId() { return chatId; }
    public String getContent() { return content; }
}