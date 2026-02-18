package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.chat.ChatDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.message.MessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.ChatService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto.AdminSendMessageRequest;

@RestController
@RequestMapping("/api/admin/support")
public class AdminChatController {

    private final ChatService chatService;

    public AdminChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // GET /api/admin/support/chats -> lista svih chatova (za admin panel)
    @GetMapping(value="/chats")
    public ResponseEntity<List<ChatDTO>> listChats() {
        return ResponseEntity.ok(chatService.listAllChats());
    }

    // POST /api/admin/support/messages -> fallback admin send preko REST-a
    @PostMapping(value="/messages")
    public ResponseEntity<MessageDTO> adminSend(Principal principal, @Valid @RequestBody AdminSendMessageRequest req) {
        return ResponseEntity.ok(chatService.adminSendMessage(principal, req.getChatId(), req.getContent()));
    }
}