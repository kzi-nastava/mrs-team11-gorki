package ftn.mrs_team11_gorki.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CreatedRatingDTO implements Serializable {
    private Long ratingId;
    private Long rideId;
    private double driverRating;
    private double vehicleRating;
    private String creatdAt;

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
    public String getCreatdAt() {
        return creatdAt;
    }
    public void setCreatdAt(String creatdAt) {
        this.creatdAt = creatdAt;
    }

}
