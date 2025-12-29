package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.time.LocalDateTime;

public class CreatedRatingDTO {
	
    private Long ratingId;
    private Long rideId;
    private double driverRating;
    private double vehicleRating;
    private LocalDateTime creatdAt;
    
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
