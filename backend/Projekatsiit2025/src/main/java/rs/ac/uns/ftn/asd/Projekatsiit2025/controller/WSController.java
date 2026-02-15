package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.message.MessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.ChatService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto.AdminSendMessageRequest;
import rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto.SendMessageRequest;

@Controller
public class WSController {

    private final ChatService chatService;

    public WSController(ChatService chatService) {
        this.chatService = chatService;
    }

    // USER -> /app/support.send
    @MessageMapping("/support.send")
    public void userSend(SendMessageRequest req, Principal principal) {
    	  String email = principal.getName(); 
        chatService.userSendMessage(principal, req.getContent());
    }

    // ADMIN -> /app/support.adminSend
    @MessageMapping("/support.adminSend")
    public void adminSend(AdminSendMessageRequest req, Principal principal) {
        chatService.adminSendMessage(principal, req.getChatId(), req.getContent());
    }
}
