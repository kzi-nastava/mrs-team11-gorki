package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.notification.NotificationResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.UserRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
	
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public NotificationController(NotificationService notificationService, UserRepository userRepository) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    @PreAuthorize("hasAnyAuthority('ROLE_PASSENGER','ROLE_DRIVER','ROLE_ADMIN')")
	@GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE )
	public ResponseEntity<List<NotificationResponseDTO>> byUserId(@PathVariable Long userId) {
	    return ResponseEntity.ok(notificationService.getAllForUserId(userId));
	}
    
    @PreAuthorize("hasAnyAuthority('ROLE_PASSENGER','ROLE_DRIVER','ROLE_ADMIN')")
    @PutMapping("/{id}/read/{userId}")
    public ResponseEntity<Void> markAsRead(
            @PathVariable Long id,
            @PathVariable Long userId) {

        notificationService.markAsRead(id, userId);
        return ResponseEntity.ok().build();
    }

}
