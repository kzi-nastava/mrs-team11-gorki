package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
@Entity
public class PanicEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private LocalDateTime timeStamp;
	private Boolean resolved=false;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	@ManyToOne
	@JoinColumn(name = "ride_id")
	private Ride ride;
	
	public PanicEvent() {
	}
	public PanicEvent(Long id, LocalDateTime timeStamp, Boolean resolved, User user, Ride ride) {
		this.id = id;
		this.timeStamp = timeStamp;
		this.resolved = resolved;
		this.user = user;
		this.ride = ride;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDateTime getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(LocalDateTime timeStamp) {
		this.timeStamp = timeStamp;
	}
	public Boolean getResolved() {
		return resolved;
	}
	public void setResolved(Boolean resolved) {
		this.resolved = resolved;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Ride getRide() {
		return ride;
	}
	public void setRide(Ride ride) {
		this.ride = ride;
	}
}
