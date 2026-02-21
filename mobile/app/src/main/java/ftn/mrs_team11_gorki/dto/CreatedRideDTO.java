package ftn.mrs_team11_gorki.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CreatedRideDTO {
    private Long id;
    private String status;
    private Double price;
    private LocalDateTime scheduledTime;
    private GetDriverDTO driver;
    private CreatedRouteDTO route;
    private PriceConfigDTO priceConfig;
    private List<GetPassengerDTO> linkedPassengers;
    private GetPassengerDTO creator;

    public CreatedRideDTO() {
        super();
    }
    public CreatedRideDTO(Long id, String status, double price, LocalDateTime scheduledTime,
                          GetDriverDTO driver, CreatedRouteDTO route, PriceConfigDTO priceConfig, List<GetPassengerDTO> linkedPassengers,
                          GetPassengerDTO creator) {
        super();
        this.id = id;
        this.status = status;
        this.price = price;
        this.scheduledTime = scheduledTime;
        this.driver = driver;
        this.route = route;
        this.priceConfig = priceConfig;
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
    public GetDriverDTO getDriver() {
        return driver;
    }
    public void setDriver(GetDriverDTO driver) {
        this.driver = driver;
    }
    public CreatedRouteDTO getRoute() {
        return route;
    }
    public void setRoute(CreatedRouteDTO route) {
        this.route = route;
    }
    public PriceConfigDTO getPriceConfig() {
        return priceConfig;
    }
    public void setPriceConfig(PriceConfigDTO priceConfig) {
        this.priceConfig = priceConfig;
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

