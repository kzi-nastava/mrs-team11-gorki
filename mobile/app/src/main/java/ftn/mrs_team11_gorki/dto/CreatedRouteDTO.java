package ftn.mrs_team11_gorki.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CreatedRouteDTO {
    private Long id;
    private List<LocationDTO> locations;
    private Double distance;
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

