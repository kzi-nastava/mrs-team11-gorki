package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.time.LocalDateTime;

import java.util.List;

import org.hibernate.annotations.ManyToAny;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

@Entity
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    @ElementCollection
	private List<Location> locations;
	private double distance;
	private LocalDateTime estimatedTime;
	public Route(Long id, List<Location> locations, double distance, LocalDateTime estimatedTime) {
		this.id = id;
		this.locations = locations;
		this.distance = distance;
		this.estimatedTime = estimatedTime;
	}
	public Route() {
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<Location> getLocations() {
		return locations;
	}
	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	public LocalDateTime getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(LocalDateTime estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	
	
}
