package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Chat;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {
	  Optional<Chat> findByUserId(Long userId);
	  
	  boolean existsByUserId(Long userId);

	  List<Chat> findByAdminId(Long adminId);
}
