package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

@Entity
public class Ride {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    @Enumerated(EnumType.STRING)
	private RideStatus status;
	
	private double price;
	private LocalDateTime scheduledTime;
	private LocalDateTime startingTime;
	private LocalDateTime endingTime;
	private Boolean panicActivated;
	private String cancellationReason;
	private String cancelledBy;
	
	@ManyToOne
	@JoinColumn(name = "driver_id")
	private Driver driver;
	
	@ManyToOne
	@JoinColumn(name = "route_id")
	private Route route;
	
	@Embedded
	private PriceConfig priceConfig;
	
	@ManyToMany
	@JoinTable(
	    name = "ride_passengers",
	    joinColumns = @JoinColumn(name = "ride_id"),
	    inverseJoinColumns = @JoinColumn(name = "passenger_id")
	)
	private List<Passenger> linkedPassengers;
	
	@ManyToOne
	@JoinColumn(name = "creator_id")
	private Passenger creator;
	
	public Ride() {
	}
	public Ride(Long id, RideStatus status, double price, LocalDateTime scheduledTime, LocalDateTime startingTime,
			LocalDateTime endingTime, Boolean panicActivated, String cancellationReason, String cancelledBy, Driver driver, Route route,
			PriceConfig priceConfig, List<Passenger> linkedPassengers, Passenger creator) {
		this.id = id;
		this.status = status;
		this.price = price;
		this.scheduledTime = scheduledTime;
		this.startingTime = startingTime;
		this.endingTime = endingTime;
		this.panicActivated = panicActivated;
		this.cancellationReason = cancellationReason;
		this.cancelledBy = cancelledBy;
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
	public String getCancelledBy() {
		return cancelledBy;
	}
	public void setCancelledBy(String cancelledBy) {
		this.cancelledBy = cancelledBy;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public PriceConfig getPriceConfig() {
		return priceConfig;
	}
	public void setPriceConfig(PriceConfig priceConfig) {
		this.priceConfig = priceConfig;
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
	
	
}
