package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.User;

public interface RegisterRepository extends JpaRepository<User, Ride> {

}
