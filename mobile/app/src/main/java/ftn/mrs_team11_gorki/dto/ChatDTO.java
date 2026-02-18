package ftn.mrs_team11_gorki.dto;

import java.util.List;

public class ChatDTO {
    private Long id;
    private Long userId;
    private Long adminId;
    private List<MessageDTO> messages;

    public ChatDTO() {}

    public ChatDTO(Long id, Long userId, Long adminId, List<MessageDTO> messages) {
        this.id = id;
        this.userId = userId;
        this.adminId = adminId;
        this.messages = messages;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getAdminId() {
        return adminId;
    }

    public void setAdminId(Long adminId) {
        this.adminId = adminId;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public void setMessages(List<MessageDTO> messages) {
        this.messages = messages;
    }


}
