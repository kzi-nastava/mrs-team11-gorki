package ftn.mrs_team11_gorki.dto;

public class CreateRatingDTO {
    public double driverRating;
    public double vehicleRating;
    public String comment;

    public CreateRatingDTO(double driverRating, double vehicleRating, String comment) {
        this.driverRating = driverRating;
        this.vehicleRating = vehicleRating;
        this.comment = comment;
    }
}