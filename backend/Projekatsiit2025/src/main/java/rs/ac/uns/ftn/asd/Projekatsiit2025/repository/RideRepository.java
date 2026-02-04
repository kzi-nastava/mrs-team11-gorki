package rs.ac.uns.ftn.asd.Projekatsiit2025.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
	
    List<Ride> findByDriverIdAndStartingTimeBetween(
            @Param("driverId") Long driverId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to
    );
    
    Optional<Ride> findByDriverIdAndStatus(
            Long driverId,
            RideStatus status
        );

    Optional<Ride> findFirstByDriverIdAndStatusInOrderByStartingTimeDesc(
        Long driverId,
        List<RideStatus> statuses
    );
	List<Ride> findByDriverIdAndStatusAndStartingTimeBetween(
	        Long driverId,
	        RideStatus status,
	        LocalDateTime from,
	        LocalDateTime to
	);
    
    Ride findByCreator_IdAndStatus(Long passengerId, RideStatus status);
    
    //END OF A RIDE
    // pronadji voznju koja pripada tom driveru
    Ride findByIdAndDriver_Id(Long rideId, Long driverId);

    // sledeca zakazana voznja
    Ride findFirstByDriver_IdAndStatusOrderByScheduledTimeAsc(Long driverId, RideStatus status);

    List<Ride> findByCreator_IdAndStatusAndStartingTimeBetween(
	        Long creatorId,
	        RideStatus status,
	        LocalDateTime from,
	        LocalDateTime to
	);

    // admin pregled svih voznji u odredjenom periodu
    List<Ride> findAllByStatusAndStartingTimeBetween(
            RideStatus status,
            LocalDateTime from,
            LocalDateTime to
    );

}
