package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.time.LocalDateTime;

public class Rating {
	private Long id;
	private double driverRating;
	private double vehicleRating;
	private String comment;
	private LocalDateTime createdAt;
	private Ride ride;
	
	public Rating() {
		
	}
	public Rating(Long id, double driverRating, double vehicleRating, String comment, LocalDateTime createdAt,
			Ride ride) {
		this.id = id;
		this.driverRating = driverRating;
		this.vehicleRating = vehicleRating;
		this.comment = comment;
		this.createdAt = createdAt;
		this.ride = ride;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public Ride getRide() {
		return ride;
	}
	public void setRide(Ride ride) {
		this.ride = ride;
	}
	
}
