package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CreateRideDTO {
	private LocalDateTime scheduledTime;
	private CreateRouteDTO route;
	private List<String> linkedPassengersEmails;
	private Long creatorId;
	private Boolean babyTransport;
    private Boolean petFriendly;
    private String vehicleType;
	public CreateRideDTO() {
		super();
	}
	public CreateRideDTO(LocalDateTime scheduledTime, CreateRouteDTO route, List<String> linkedPassengersEmails, Long creatorId,
			Boolean babyTransport, Boolean petFriendly, String vehicleType) {
		super();
		this.scheduledTime = scheduledTime;
		this.route = route;
		this.linkedPassengersEmails = linkedPassengersEmails;
		this.creatorId = creatorId;
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
	public CreateRouteDTO getRoute() {
		return route;
	}
	public void setRoute(CreateRouteDTO route) {
		this.route = route;
	}
	public List<String> getLinkedPassengersEmails() {
		return linkedPassengersEmails;
	}
	public void setLinkedPassengers(List<String> linkedPassengersEmails) {
		this.linkedPassengersEmails = linkedPassengersEmails;
	}
	public Long getCreatorId() {
		return creatorId;
	}
	public void setCreator(Long creatorId) {
		this.creatorId = creatorId;
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
