package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.chat;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.message.MessageDTO;

public class ChatDTO {
	
	@NotNull(message = "Chat id is required")
	@Positive(message = "Id must be positive")
	private Long id;
	
    @NotNull(message = "userId is required")
    @Positive(message = "userId must be positive")	
    private Long userId;
    
    @NotNull(message = "adminId is required")
    @Positive(message = "adminId must be positive")
    private Long adminId;
    
    @NotNull(message = "messages list must not be null")
    @Valid
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
