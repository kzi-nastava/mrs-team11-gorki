package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public class CreateRatingDTO {
	
	@DecimalMin(value = "1.0", message = "Driver rating must be at least 1")
	@DecimalMax(value = "5.0", message = "Driver rating must be at most 5")
	private double driverRating;

	@DecimalMin(value = "1.0", message = "Vehicle rating must be at least 1")
	@DecimalMax(value = "5.0", message = "Vehicle rating must be at most 5")
	private double vehicleRating;
	
	@NotNull(message = "Comment is required")
    private String comment;
    
	public CreateRatingDTO() {
		super();
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
    
    
}
