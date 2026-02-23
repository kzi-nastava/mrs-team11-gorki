package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;

public class UserRideHistoryDTO {
	
	@Positive(message = "Ride id must be positive")
	@NotNull
	private Long rideId;

	@NotNull(message = "Starting time is required")
	@PastOrPresent(message = "Starting time cannot be in the future")
	private LocalDateTime startingTime;

	@PastOrPresent(message = "Ending time cannot be in the future")
	private LocalDateTime endingTime;

	@NotNull(message = "Route is required")
	@Valid
	private GetRouteDTO route;

	@NotNull(message = "Canceled flag is required")
	private Boolean canceled;

	@Size(max = 100, message = "CanceledBy value too long")
	private String canceledBy;

	@NotNull(message = "Price is required")
	@PositiveOrZero(message = "Price must be >= 0")
	private Double price;

	@NotNull(message = "Panic flag is required")
	private Boolean panicActivated;

	@NotNull(message = "Average rating is required")
	@DecimalMin(value = "0.0", message = "Average rating must be >= 0")
	@DecimalMax(value = "5.0", message = "Average rating must be <= 5")
	private Double averageRating;

	@NotEmpty(message = "Passengers list must not be empty")
	@Valid
	private List<PassengerInRideDTO> passengers;

    
	public UserRideHistoryDTO() {
		super();
	}
	public Long getRideId() {
		return rideId;
	}
	public void setRideId(Long rideId) {
		this.rideId = rideId;
	}
	public LocalDateTime getStartingTime() {
		return startingTime;
	}
	public void setStartingTime(LocalDateTime startingTime) {
		this.startingTime = startingTime;
	}
	public LocalDateTime getEndingTime() {
		return endingTime;
	}
	public void setEndingTime(LocalDateTime endingTime) {
		this.endingTime = endingTime;
	}
	public GetRouteDTO getRoute() {
		return route;
	}
	public void setRoute(GetRouteDTO route) {
		this.route = route;
	}
	public boolean isCanceled() {
		return canceled;
	}
	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
	public String getCanceledBy() {
		return canceledBy;
	}
	public void setCanceledBy(String canceledBy) {
		this.canceledBy = canceledBy;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isPanicActivated() {
		return panicActivated;
	}
	public void setPanicActivated(boolean panicActivated) {
		this.panicActivated = panicActivated;
	}
	public List<PassengerInRideDTO> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PassengerInRideDTO> passengers) {
		this.passengers = passengers;
	}
	public double getAverageRating() {
		return averageRating;
	}
	public void setAverageRating(double averageRating) {
		this.averageRating = averageRating;
	}
	
}
