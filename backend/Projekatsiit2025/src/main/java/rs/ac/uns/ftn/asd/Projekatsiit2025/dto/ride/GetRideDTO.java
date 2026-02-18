package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.driver.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger.GetPassengerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

public class GetRideDTO {
	
	@Positive(message = "Id must be positive")
	@NotNull(message = "Id is required")
	private Long id;

	@NotNull(message = "Status is required")
	private RideStatus status;

	@NotNull(message = "Price is required")
	@PositiveOrZero(message = "Price must be >= 0")
	private Double price;

	@NotNull(message = "Scheduled time is required")
	private LocalDateTime scheduledTime;

	@PastOrPresent(message = "Starting time cannot be in the future")
	private LocalDateTime startingTime;

	@PastOrPresent(message = "Ending time cannot be in the future")
	private LocalDateTime endingTime;

	@NotNull(message = "Panic flag is required")
	private Boolean panicActivated;

	@Size(max = 500, message = "Cancellation reason too long")
	private String cancellationReason;

	@Valid
	private GetDriverDTO driver;

	@NotNull(message = "Route is required")
	@Valid
	private GetRouteDTO route;

	@NotEmpty(message = "Linked passengers required")
	@Valid
	private List<GetPassengerDTO> linkedPassengers;

	@NotNull(message = "Creator is required")
	@Valid
	private GetPassengerDTO creator;

	
	public GetRideDTO() {
		super();
	}
	public GetRideDTO(Long id, RideStatus status, double price, LocalDateTime scheduledTime, LocalDateTime startingTime,
			LocalDateTime endingTime, Boolean panicActivated, String cancellationReason, GetDriverDTO driver,
			GetRouteDTO route, List<GetPassengerDTO> linkedPassengers, GetPassengerDTO creator) {
		super();
		this.id = id;
		this.status = status;
		this.price = price;
		this.scheduledTime = scheduledTime;
		this.startingTime = startingTime;
		this.endingTime = endingTime;
		this.panicActivated = panicActivated;
		this.cancellationReason = cancellationReason;
		this.driver = driver;
		this.route = route;
		this.linkedPassengers = linkedPassengers;
		this.creator = creator;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public RideStatus getStatus() {
		return status;
	}
	public void setStatus(RideStatus status) {
		this.status = status;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(LocalDateTime scheduledTime) {
		this.scheduledTime = scheduledTime;
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
	public Boolean getPanicActivated() {
		return panicActivated;
	}
	public void setPanicActivated(Boolean panicActivated) {
		this.panicActivated = panicActivated;
	}
	public String getCancellationReason() {
		return cancellationReason;
	}
	public void setCancellationReason(String cancellationReason) {
		this.cancellationReason = cancellationReason;
	}
	public GetDriverDTO getDriver() {
		return driver;
	}
	public void setDriver(GetDriverDTO driver) {
		this.driver = driver;
	}
	public GetRouteDTO getRoute() {
		return route;
	}
	public void setRoute(GetRouteDTO route) {
		this.route = route;
	}
	public List<GetPassengerDTO> getLinkedPassengers() {
		return linkedPassengers;
	}
	public void setLinkedPassengers(List<GetPassengerDTO> linkedPassengers) {
		this.linkedPassengers = linkedPassengers;
	}
	public GetPassengerDTO getCreator() {
		return creator;
	}
	public void setCreator(GetPassengerDTO creator) {
		this.creator = creator;
	}
	
}
