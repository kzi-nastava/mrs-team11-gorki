package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class RideEstimateResponseDTO {
	@NotNull(message = "Estimated time is required")
	private LocalDateTime estimatedTime;

	@NotNull(message = "Distance is required")
	@PositiveOrZero(message = "Distance must be >= 0")
	private Double distance;

	@NotBlank(message = "Route is required")
	@Size(max = 500, message = "Route description too long")
	private String route;


	public RideEstimateResponseDTO() {
		super();
	}
	
	public RideEstimateResponseDTO(LocalDateTime estimatedTime, double distance, String route) {
		super();
		this.estimatedTime = estimatedTime;
		this.distance = distance;
		this.route = route;
	}
	
    public LocalDateTime getEstimatedTime() {
        return estimatedTime;
    }
    
    public void setEstimatedTime(LocalDateTime estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    
    public double getDistance() {
        return distance;
    }
    
    public void setDistance(double distance) {
        this.distance = distance;
    }
    
    public String getRoute() {
        return route;
    }
    
    public void setRoute(String route) {
        this.route = route;
    }
    
}