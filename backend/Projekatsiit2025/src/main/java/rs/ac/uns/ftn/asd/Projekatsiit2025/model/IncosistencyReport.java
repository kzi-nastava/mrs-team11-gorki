package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.time.LocalDateTime;

public class IncosistencyReport {
	private Long id;
	private String description;
	private LocalDateTime timeStamp;
	private Passenger passenger;
	private Ride ride;
	
	public IncosistencyReport() {
	}
	public IncosistencyReport(Long id, String description, LocalDateTime timeStamp, Passenger passenger, Ride ride) {
		this.id = id;
		this.description = description;
		this.timeStamp = timeStamp;
		this.passenger = passenger;
		this.ride = ride;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Passenger getPassenger() {
		return passenger;
	}
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}
	public Ride getRide() {
		return ride;
	}
	public void setRide(Ride ride) {
		this.ride = ride;
	}
	
	
}
