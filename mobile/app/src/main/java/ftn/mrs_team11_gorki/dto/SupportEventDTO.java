package ftn.mrs_team11_gorki.dto;

import ftn.mrs_team11_gorki.dto.MessageDTO;

public class SupportEventDTO {
    private Long chatId;
    private MessageDTO message;

    public Long getChatId() { return chatId; }
    public MessageDTO getMessage() { return message; }
}
