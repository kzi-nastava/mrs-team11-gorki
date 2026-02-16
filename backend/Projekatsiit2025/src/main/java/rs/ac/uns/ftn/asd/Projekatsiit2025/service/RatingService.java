package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating.CreateRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating.CreatedRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Rating;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Ride;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RatingRepository;
@Service
public class RatingService {
	
    private final RideRepository rideRepository;
    private final RatingRepository ratingRepository;

    public RatingService(RideRepository rideRepository, RatingRepository ratingRepository) {
        this.rideRepository = rideRepository;
        this.ratingRepository=ratingRepository;
    }
    
    @Transactional
    public CreatedRatingDTO createRating(Long rideId, CreateRatingDTO dto) {
    	// 1️ Dobavi voznju iz rideRepository
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Voznja ne postoji"));

        // 2️ Provera da li je voznja zavrsena
        if (ride.getStatus() != RideStatus.FINISHED) {
            throw new RuntimeException("Voznja jos nije zavrsena");
        }
        
        //3 Proslo vise od 3 dana
        if (ride.getEndingTime() == null ||
                ride.getEndingTime().plusDays(3).isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Rok za ocenjivanje je istekao");
        }

        // 3️ Kreiranje ocene
        Rating rating = new Rating();
        rating.setRide(ride);
        rating.setDriverRating(dto.getDriverRating());
        rating.setVehicleRating(dto.getVehicleRating());
        rating.setComment(dto.getComment());
        rating.setCreatedAt(LocalDateTime.now());
        rating.setPassenger(ride.getCreator());

		Rating saved = ratingRepository.save(rating);

        // 4️ Mapiranje na DTO
        return mapRatingToDTO(saved);
       
    }
    
    private CreatedRatingDTO mapRatingToDTO(Rating rating) {
        CreatedRatingDTO dto = new CreatedRatingDTO();
        dto.setRatingId(rating.getId());
        dto.setRideId(rating.getRide().getId());
        dto.setDriverRating(rating.getDriverRating());
        dto.setVehicleRating(rating.getVehicleRating());
        dto.setCreatdAt(rating.getCreatedAt());
        dto.setCreatorId(rating.getPassenger().getId());
        return dto;
    }
    
    @Transactional
    public Long pendingLatestRideId(String email) {

        Ride ride = rideRepository
            .findFirstByCreator_EmailAndStatusOrderByEndingTimeDesc(email, RideStatus.FINISHED)
            .orElse(null);

        if (ride == null) {
            System.out.println("PENDING: no finished ride for " + email);
            return null;
        }

        System.out.println("PENDING: found rideId=" + ride.getId()
            + " endingTime=" + ride.getEndingTime()
            + " status=" + ride.getStatus());

        if (ride.getEndingTime() == null) {
            System.out.println("PENDING: endingTime is NULL -> returning null");
            return null;
        }

        if (ride.getEndingTime().isBefore(LocalDateTime.now().minusDays(3))) {
            System.out.println("PENDING: expired -> returning null");
            return null;
        }

        Long passengerId = ride.getCreator().getId();
        boolean alreadyRated = ratingRepository
            .findByRideIdAndPassengerId(ride.getId(), passengerId)
            .isPresent();

        System.out.println("PENDING: alreadyRated=" + alreadyRated + " passengerId=" + passengerId);

        return alreadyRated ? null : ride.getId();
    }
}
