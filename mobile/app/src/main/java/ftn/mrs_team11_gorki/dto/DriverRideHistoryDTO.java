package ftn.mrs_team11_gorki.dto;

import java.time.LocalDateTime;
import java.util.List;

public class DriverRideHistoryDTO {
    private Long rideId;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private GetRouteDTO route;
    private boolean canceled;
    private String canceledBy;
    private double price;
    private boolean panicActivated;
    private List<PassengerInRideDTO> passengers;

    public Long getRideId() { return rideId; }
    public LocalDateTime getStartingTime() { return startingTime; }
    public LocalDateTime getEndingTime() { return endingTime; }
    public GetRouteDTO getRoute() { return route; }
    public boolean isCanceled() { return canceled; }
    public String getCanceledBy() { return canceledBy; }
    public double getPrice() { return price; }
    public boolean isPanicActivated() { return panicActivated; }
    public List<PassengerInRideDTO> getPassengers() { return passengers; }
}
