package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.time.LocalDateTime;
import java.util.List;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;

public class CreateRideDTO {
	private LocalDateTime scheduledTime;
	private Route route;
	private List<Passenger> linkedPassengers;
	private Passenger creator;
	private Boolean babyTransport;
    private Boolean petFriendly;
    private String vehicleType;
	public CreateRideDTO() {
		super();
	}
	public CreateRideDTO(LocalDateTime scheduledTime, Route route, List<Passenger> linkedPassengers, Passenger creator,
			Boolean babyTransport, Boolean petFriendly, String vehicleType) {
		super();
		this.scheduledTime = scheduledTime;
		this.route = route;
		this.linkedPassengers = linkedPassengers;
		this.creator = creator;
		this.babyTransport = babyTransport;
		this.petFriendly = petFriendly;
		this.vehicleType = vehicleType;
	}
	public LocalDateTime getScheduledTime() {
		return scheduledTime;
	}
	public void setScheduledTime(LocalDateTime scheduledTime) {
		this.scheduledTime = scheduledTime;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public List<Passenger> getLinkedPassengers() {
		return linkedPassengers;
	}
	public void setLinkedPassengers(List<Passenger> linkedPassengers) {
		this.linkedPassengers = linkedPassengers;
	}
	public Passenger getCreator() {
		return creator;
	}
	public void setCreator(Passenger creator) {
		this.creator = creator;
	}
	public Boolean getBabyTransport() {
		return babyTransport;
	}
	public void setBabyTransport(Boolean babyTransport) {
		this.babyTransport = babyTransport;
	}
	public Boolean getPetFriendly() {
		return petFriendly;
	}
	public void setPetFriendly(Boolean petFriendly) {
		this.petFriendly = petFriendly;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
}
