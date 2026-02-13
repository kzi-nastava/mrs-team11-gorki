package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
	private double driverRating;
	private double vehicleRating;
	private String comment;
	private LocalDateTime createdAt;
	
	@ManyToOne
	@JoinColumn(name = "ride_id")
	private Ride ride;
	
	@ManyToOne
	@JoinColumn(name="creator_id")
	private Passenger passenger;
	
	public Rating() {
		
	}
	public Rating(Long id, double driverRating, double vehicleRating, String comment, LocalDateTime createdAt,
			Ride ride,Passenger passenger) {
		this.id = id;
		this.driverRating = driverRating;
		this.vehicleRating = vehicleRating;
		this.comment = comment;
		this.createdAt = createdAt;
		this.ride = ride;
		this.passenger=passenger;
	}
	public Passenger getPassenger() {
		return passenger;
	}
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
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
