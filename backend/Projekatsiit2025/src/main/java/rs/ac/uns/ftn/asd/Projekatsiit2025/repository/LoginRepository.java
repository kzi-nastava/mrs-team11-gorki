package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LoginRepository extends JpaRepository<User, Ride> {

}
