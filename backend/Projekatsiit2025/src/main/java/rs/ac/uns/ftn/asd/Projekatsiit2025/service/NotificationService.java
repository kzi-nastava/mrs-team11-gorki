package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification.NotificationDTO;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendRatingAvailable(String userEmail, Long rideId) {
        NotificationDTO dto = new NotificationDTO(
                "RATING_AVAILABLE",
                rideId,
                "Rate your ride",
                "Your ride has finished. You can rate the driver and vehicle within 3 days.",
                LocalDateTime.now()
        );

        messagingTemplate.convertAndSendToUser(userEmail, "/queue/notifications", dto);
    }
}