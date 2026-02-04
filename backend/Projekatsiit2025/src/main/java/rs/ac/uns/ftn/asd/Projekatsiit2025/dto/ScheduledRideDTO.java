package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;
import java.time.LocalDateTime;
import java.util.List;

public class ScheduledRideDTO {
    private GetUserDTO user;
    private Long rideId;
    private LocalDateTime startingTime;
    private GetRouteDTO route;
    private LocalDateTime date;
    private double price;
    private boolean canceled;
    private String canceledBy;
    private String cancellationReason;
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
