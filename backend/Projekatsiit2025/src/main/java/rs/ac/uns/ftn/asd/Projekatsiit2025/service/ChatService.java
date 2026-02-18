package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.chat.ChatDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.message.MessageDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Admin;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Chat;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Message;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.AdminRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.ChatRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;

@Service
public class ChatService {
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public ChatService(ChatRepository chatRepository,
                       UserRepository userRepository,
                       AdminRepository adminRepository,
                       SimpMessagingTemplate messagingTemplate) {
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.messagingTemplate = messagingTemplate;
    }

    // ===== MAPPERS =====

    private MessageDTO toDTO(Message m) {
        return new MessageDTO(m.getSender(), m.getContent(), m.getTimeStamp());
    }

    // payload koji ide adminu mora da ima chatId
    private AdminMessageDTO toAdminDTO(Chat chat, Message m) {
        return new AdminMessageDTO(
                chat.getId(),
                chat.getUser().getId(),
                m.getSender(),
                m.getContent(),
                m.getTimeStamp()
        );
    }

    private ChatDTO toDTO(Chat c) {
        Long adminId = (c.getAdmin() != null) ? c.getAdmin().getId() : null;
        List<MessageDTO> msgs = (c.getMessages() == null)
                ? List.of()
                : c.getMessages().stream().map(this::toDTO).toList();

        return new ChatDTO(c.getId(), c.getUser().getId(), adminId, msgs);
    }

    private User currentUser(Principal principal) {
        return userRepository.findByEmail(principal.getName()).orElseThrow();
    }

    private Admin currentAdmin(Principal principal) {
        return adminRepository.findByEmail(principal.getName()).orElseThrow();
    }

    // ===== USER SIDE =====

    @Transactional
    public ChatDTO getOrCreateMyChat(Principal principal) {
        User user = currentUser(principal);

        Chat chat = chatRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Chat c = new Chat();
                    c.setUser(user);
                    c.setMessages(new ArrayList<>());
                    // ako hoces default admin:
                    c.setAdmin(adminRepository.findByEmail("admin1@gmail.com").orElseThrow());
                    return chatRepository.save(c);
                });

        if (chat.getMessages() == null) {
            chat.setMessages(new ArrayList<>());
        }

        return toDTO(chat);
    }

    @Transactional
    public MessageDTO userSendMessage(Principal principal, String content) {
        User user = currentUser(principal);

        Chat chat = chatRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Chat c = new Chat();
                    c.setUser(user);
                    c.setMessages(new ArrayList<>());
                    // mozes i ovde default admin ako hoces:
                    // c.setAdmin(adminRepository.findByEmail("admin1@gmail.com").orElseThrow());
                    return chatRepository.save(c);
                });

        if (chat.getMessages() == null) {
            chat.setMessages(new ArrayList<>());
        }

        Message msg = new Message(user.getEmail(), content, LocalDateTime.now());
        chat.getMessages().add(msg);
        chatRepository.save(chat);

        // 1) user-u ide klasicni MessageDTO
        MessageDTO userPayload = toDTO(msg);
        messagingTemplate.convertAndSendToUser(
                user.getEmail(),
                "/queue/support",
                userPayload
        );

        // 2) admin-u ide AdminMessageDTO sa chatId
        AdminMessageDTO adminPayload = toAdminDTO(chat, msg);

        if (chat.getAdmin() != null) {
            messagingTemplate.convertAndSendToUser(
                    chat.getAdmin().getEmail(),
                    "/queue/support-admin",
                    adminPayload
            );
        } else {
            // ping svim adminima da se pojavio chat / poruka u "unassigned" chatu
            messagingTemplate.convertAndSend(
                    "/topic/support/admin",
                    new AdminChatPingDTO(chat.getId(), user.getId())
            );
        }

        return userPayload;
    }

    // ===== ADMIN SIDE =====

    @Transactional(readOnly = true)
    public List<ChatDTO> listAllChats() {
        return chatRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional
    public MessageDTO adminSendMessage(Principal principal, Long chatId, String content) {
        Admin admin = currentAdmin(principal);

        Chat chat = chatRepository.findById(chatId).orElseThrow();

        if (chat.getMessages() == null) {
            chat.setMessages(new ArrayList<>());
        }

        // dodeli admina ako nije dodeljen
        if (chat.getAdmin() == null) {
            chat.setAdmin(admin);
        }

        Message msg = new Message("ADMIN:" + admin.getEmail(), content, LocalDateTime.now());
        chat.getMessages().add(msg);
        chatRepository.save(chat);

        // 1) user dobija poruku (MessageDTO)
        MessageDTO userPayload = toDTO(msg);
        messagingTemplate.convertAndSendToUser(
                chat.getUser().getEmail(),   // ✅ EMAIL, ne ID
                "/queue/support",
                userPayload
        );

        // 2) admin dobija poruku nazad (AdminMessageDTO)
        AdminMessageDTO adminPayload = toAdminDTO(chat, msg);
        messagingTemplate.convertAndSendToUser(
                admin.getEmail(),            // ✅ EMAIL, ne ID
                "/queue/support-admin",
                adminPayload
        );

        return userPayload;
    }

    // ===== DTOs for admin realtime =====

    public static class AdminMessageDTO {
        private Long chatId;
        private Long userId;
        private String sender;
        private String content;
        private LocalDateTime timeStamp;

        public AdminMessageDTO() {}

        public AdminMessageDTO(Long chatId, Long userId, String sender, String content, LocalDateTime timeStamp) {
            this.chatId = chatId;
            this.userId = userId;
            this.sender = sender;
            this.content = content;
            this.timeStamp = timeStamp;
        }

        public Long getChatId() { return chatId; }
        public void setChatId(Long chatId) { this.chatId = chatId; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }

        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }

        public LocalDateTime getTimeStamp() { return timeStamp; }
        public void setTimeStamp(LocalDateTime timeStamp) { this.timeStamp = timeStamp; }
    }

    public static class AdminChatPingDTO {
        private Long chatId;
        private Long userId;

        public AdminChatPingDTO() {}
        public AdminChatPingDTO(Long chatId, Long userId) {
            this.chatId = chatId;
            this.userId = userId;
        }
        public Long getChatId() { return chatId; }
        public void setChatId(Long chatId) { this.chatId = chatId; }
        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}