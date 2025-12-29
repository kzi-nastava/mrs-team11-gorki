package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.time.LocalDateTime;
import java.util.List;

public class Route {
	private Long id;
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
