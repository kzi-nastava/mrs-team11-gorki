package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;

public class GetRouteDTO {
	
	@Positive(message = "Id must be positive")
	@NotNull(message = "Id is required")
	private Long id;
	
	@NotEmpty(message = "Locations are required")
	@Valid
	private List<LocationDTO> locations;
	
	@NotNull(message = "Distance is required")
	@PositiveOrZero(message = "Distance must be >= 0")
	private double distance;
	
	@NotNull(message = "Time is required")
	private LocalDateTime estimatedTime;
	
	public GetRouteDTO() {
		super();
	}
	
	public GetRouteDTO(Long id, List<LocationDTO> locations, double distance, LocalDateTime estimatedTime) {
		super();
		this.id = id;
		this.locations = locations;
		this.distance = distance;
		this.estimatedTime = estimatedTime;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public List<LocationDTO> getLocations() {
		return locations;
	}
	public void setLocations(List<LocationDTO> locations) {
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
