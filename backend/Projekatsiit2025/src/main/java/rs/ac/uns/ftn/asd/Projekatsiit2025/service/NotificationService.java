package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification.NotificationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification.NotificationResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Notification;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.NotificationRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationService(
            SimpMessagingTemplate messagingTemplate,
            NotificationRepository notificationRepository,
            UserRepository userRepository
    ) {
        this.messagingTemplate = messagingTemplate;
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }
    
    
    @Transactional
    public void sendRatingAvailable(String userEmail, Long rideId) {

        // 1) napravi DTO za realtime
        NotificationDTO dto = new NotificationDTO(
                "RATING_AVAILABLE",
                rideId,
                "Rate your ride",
                "Your ride has finished. You can rate the driver and vehicle within 3 days.",
                LocalDateTime.now()
        );

        // 2) nadji user-a
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        // 3) snimi u bazu 
        Notification n = new Notification();
        n.setPurpose(dto.getType()); 
        n.setContent(dto.getMessage()); 
        n.setCreatedAt(dto.getCreatedAt());
        n.setRead(false);
        n.setUser(user);

        notificationRepository.save(n);

        // 4) push preko WS (user destinacija)
        messagingTemplate.convertAndSendToUser(
                userEmail,
                "/queue/notifications",
                dto
        );
    }

    @Transactional
    public List<NotificationResponseDTO> getAllForUserId(Long userId) {
        return notificationRepository.findByUser_IdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    private NotificationResponseDTO toResponseDTO(Notification n) {
        return new NotificationResponseDTO(
                n.getId(),
                n.getPurpose(),
                n.getContent(),
                n.getCreatedAt(),
                n.getRead()
        );
    }
    
    @Transactional
    public void markAsRead(Long notificationId, Long userId) {

        Notification n = notificationRepository
                .findByIdAndUser_Id(notificationId, userId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        n.setRead(true);

        notificationRepository.save(n);
    }
    
    @Transactional
    public void createAndSend(String userEmail, Long rideId, String purpose, String content) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        Notification n = new Notification();
        n.setPurpose(purpose);
        n.setContent(content);
        n.setCreatedAt(LocalDateTime.now());
        n.setRead(false);
        n.setUser(user);

        notificationRepository.save(n);

        // realtime DTO za WS
        NotificationDTO dto = new NotificationDTO(
                purpose,              // type
                rideId,                 // rideId (NE KORISTIŠ) -> ostavi null
                purpose,              // title (može lepše)
                content,              // message
                n.getCreatedAt()
        );

        messagingTemplate.convertAndSendToUser(
                userEmail,
                "/queue/notifications",
                dto
        );
    }


}