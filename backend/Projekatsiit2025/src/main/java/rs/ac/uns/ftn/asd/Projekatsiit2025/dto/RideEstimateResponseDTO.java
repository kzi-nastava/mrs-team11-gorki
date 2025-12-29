package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.time.LocalDateTime;

public class RideEstimateResponseDTO {
    private LocalDateTime estimatedTime;
    private double distance;
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