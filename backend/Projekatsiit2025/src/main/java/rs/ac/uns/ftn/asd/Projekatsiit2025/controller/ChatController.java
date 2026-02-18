package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.security.Principal;

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
import rs.ac.uns.ftn.asd.Projekatsiit2025.webSocket.dto.SendMessageRequest;

@RestController
@RequestMapping("/api/support")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // GET /api/support/chat -> vrati (ili kreiraj) moj chat + istoriju
    @GetMapping(value="/chat")
    public ResponseEntity<ChatDTO> myChat(Principal principal) {
        return ResponseEntity.ok(chatService.getOrCreateMyChat(principal));
    }

    // POST /api/support/messages -> fallback ako WS nije konektovan
    @PostMapping(value="/messages")
    public ResponseEntity<MessageDTO> sendMessage(Principal principal,@Valid @RequestBody SendMessageRequest req) {
        return ResponseEntity.ok(chatService.userSendMessage(principal, req.getContent()));
    }
}
