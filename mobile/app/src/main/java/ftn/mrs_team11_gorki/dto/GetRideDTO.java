package ftn.mrs_team11_gorki.dto;

import java.time.LocalDateTime;
import java.util.List;

public class GetRideDTO {
    private Long id;
    private String status;
    private Double price;
    private LocalDateTime scheduledTime;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private Boolean panicActivated;
    private String cancellationReason;
    private GetDriverDTO driver;
    private GetRouteDTO route;
    private List<GetPassengerDTO> linkedPassengers;
    private GetPassengerDTO creator;

    public GetRideDTO() {
        super();
    }
    public GetRideDTO(Long id, String status, double price, LocalDateTime scheduledTime, LocalDateTime startingTime,
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
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Double getPrice() {
        return price;
    }
    public void setPrice(Double price) {
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

