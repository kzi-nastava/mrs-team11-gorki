package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.time.LocalDateTime;
import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.PriceConfig;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

public class CreatedRideDTO {
	private Long id;
	private RideStatus status;
	private double price;
	private LocalDateTime scheduledTime;
	private GetDriverDTO driver;
	private CreatedRouteDTO route;
	private PriceConfig priceConfig;
	private List<GetPassengerDTO> linkedPassengers;
	private GetPassengerDTO creator;
	public CreatedRideDTO() {
		super();
	}
	public CreatedRideDTO(Long id, RideStatus status, double price, LocalDateTime scheduledTime,
			GetDriverDTO driver, CreatedRouteDTO route, PriceConfig priceConfig, List<GetPassengerDTO> linkedPassengers,
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
	public PriceConfig getPriceConfig() {
		return priceConfig;
	}
	public void setPriceConfig(PriceConfig priceConfig) {
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
