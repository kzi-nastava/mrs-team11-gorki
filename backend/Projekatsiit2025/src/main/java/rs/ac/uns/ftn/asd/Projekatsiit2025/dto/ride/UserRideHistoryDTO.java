package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import java.time.LocalDateTime;
import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;

public class UserRideHistoryDTO {
	
	private Long rideId;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private GetRouteDTO route;
    private boolean canceled;
    private String canceledBy; 
    private double price;
    private boolean panicActivated;
    private double averageRating;
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
