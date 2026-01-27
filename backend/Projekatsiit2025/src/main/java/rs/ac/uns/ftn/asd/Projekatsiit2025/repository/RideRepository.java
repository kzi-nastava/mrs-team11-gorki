package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
	
    List<Ride> findByDriverIdAndStartingTimeBetween(
            @Param("driverId") Long driverId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );

}
