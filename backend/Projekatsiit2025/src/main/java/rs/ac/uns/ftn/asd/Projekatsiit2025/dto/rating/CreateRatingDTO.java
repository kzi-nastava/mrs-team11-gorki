package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating;

public class CreateRatingDTO {
    private double driverRating;
    private double vehicleRating;
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
