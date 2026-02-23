package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;

public class CreatedRouteDTO {
	
	@Positive(message = "Id must be positive")
	private Long id;

	@NotEmpty(message = "Locations are required")
	@Size(min = 2, message = "At least start and end location required")
	@Valid
	private List<LocationDTO> locations;

	@NotNull(message = "Distance is required")
	@PositiveOrZero(message = "Distance must be >= 0")
	private Double distance;

	@NotNull(message = "Estimated time is required")
	private LocalDateTime estimatedTime;

	
	public CreatedRouteDTO() {
		super();
	}
	public CreatedRouteDTO(Long id, List<LocationDTO> locations, double distance, LocalDateTime estimatedTime) {
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
