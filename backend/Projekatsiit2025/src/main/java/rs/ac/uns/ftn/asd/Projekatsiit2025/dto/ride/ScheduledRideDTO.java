package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;

public class ScheduledRideDTO {
	@NotNull(message = "User is required")
	@Valid
	private GetUserDTO user;

	@Positive(message = "Ride id must be positive")
	@NotNull
	private Long rideId;

	@NotNull(message = "Starting time is required")
	@FutureOrPresent(message = "Starting time must be now or in the future")
	private LocalDateTime startingTime;

	@NotNull(message = "Route is required")
	@Valid
	private GetRouteDTO route;

	@NotNull(message = "Date is required")
	private LocalDateTime date;

	@NotNull(message = "Price is required")
	@PositiveOrZero(message = "Price must be >= 0")
	private Double price;

	@NotNull(message = "Canceled flag is required")
	private Boolean canceled;

	@Size(max = 100, message = "CanceledBy value too long")
	private String canceledBy;

	@Size(max = 500, message = "Cancellation reason too long")
	private String cancellationReason;

	@NotEmpty(message = "Passengers list must not be empty")
	@Valid
	private List<PassengerInRideDTO> passengers;


    public GetUserDTO getUser() {
        return user;
    }
    public void setUser(GetUserDTO user) {
        this.user = user;
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
    public GetRouteDTO getRoute() {
        return route;
    }
    public void setRoute(GetRouteDTO route) {
        this.route = route;
    }
    public LocalDateTime getDate() {
        return date;
    }
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
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
    public String getCancellationReason() {
        return cancellationReason;
    }
    public void setCancellationReason(String cancellationReason) {
        this.cancellationReason = cancellationReason;
    }
    public List<PassengerInRideDTO> getPassengers() {
		return passengers;
	}
	public void setPassengers(List<PassengerInRideDTO> passengers) {
		this.passengers = passengers;
	}
}
