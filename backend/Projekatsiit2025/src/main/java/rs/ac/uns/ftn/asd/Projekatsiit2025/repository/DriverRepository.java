package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {

}
