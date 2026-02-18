package ftn.mrs_team11_gorki.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private String sender;
    private String content;
    private String timeStamp;

    public MessageDTO() {}

    public MessageDTO(String sender, String content, String timeStamp) {
        this.sender = sender;
        this.content = content;
        this.timeStamp = timeStamp;
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimeStamp() { return timeStamp; }
    public void setTimeStamp(String timeStamp) { this.timeStamp = timeStamp; }
}
