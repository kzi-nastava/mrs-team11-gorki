package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
}
