package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
	
	List<Notification> findByUser_IdOrderByCreatedAtDesc(Long userId);
	
    Optional<Notification> findByIdAndUser_Id(Long id, Long userId);

    long countByUser_IdAndReadFalse(Long userId);
}
