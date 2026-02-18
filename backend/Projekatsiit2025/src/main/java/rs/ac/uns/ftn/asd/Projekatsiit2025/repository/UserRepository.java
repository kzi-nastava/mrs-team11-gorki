package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
    
    List<User> findByRoleNot(UserRole role);
}

