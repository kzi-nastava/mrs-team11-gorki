package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating;

import java.time.LocalDateTime;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

public class CreatedRatingDTO {
	
	@Positive(message = "Id must be positive")
	@NotNull(message = "Id is required")
    private Long ratingId;
	
	@Positive(message = "Ride id must be positive")
	@NotNull(message = "Ride id is required")
    private Long rideId;
    
	@DecimalMin(value = "1.0", message = "Driver rating must be at least 1")
	@DecimalMax(value = "5.0", message = "Driver rating must be at most 5")
	private double driverRating;

	@DecimalMin(value = "1.0", message = "Vehicle rating must be at least 1")
	@DecimalMax(value = "5.0", message = "Vehicle rating must be at most 5")
	private double vehicleRating;
    
	@NotNull(message = "Time is required")
	@PastOrPresent(message = "Time must be in past or present")
    private LocalDateTime creatdAt;
    
	@Positive(message = "Creator id must be positive")
	@NotNull(message = "Creator id is required")
    private Long creatorId;
    
	public double getCreatorId() {
		return creatorId;
	}
	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}
	public CreatedRatingDTO() {
		super();
	}
	public Long getRatingId() {
		return ratingId;
	}
	public void setRatingId(Long ratingId) {
		this.ratingId = ratingId;
	}
	public Long getRideId() {
		return rideId;
	}
	public void setRideId(Long rideId) {
		this.rideId = rideId;
	}
	public double getDriverRating() {
		return driverRating;
	}
	public void setDriverRating(double driverRating) {
		this.driverRating = driverRating;
	}
	public double getVehicleRating() {
		return vehicleRating;
	}
	public void setVehicleRating(double vehicleRating) {
		this.vehicleRating = vehicleRating;
	}
	public LocalDateTime getCreatdAt() {
		return creatdAt;
	}
	public void setCreatdAt(LocalDateTime creatdAt) {
		this.creatdAt = creatdAt;
	}
    
    
}
