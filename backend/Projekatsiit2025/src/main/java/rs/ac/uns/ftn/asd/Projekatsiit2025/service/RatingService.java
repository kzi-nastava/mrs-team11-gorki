package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating.CreateRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating.CreatedRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.CreateUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.CreatedUserDTO;
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
    

}
