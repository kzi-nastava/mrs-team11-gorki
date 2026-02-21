package ftn.mrs_team11_gorki.dto;

import java.util.List;

public class GetRouteDTO {
    private Long id;
    private List<LocationDTO> locations;
    private double distance;
    private String estimatedTime;

    public Long getId() {
        return id;
    }
    public List<LocationDTO> getLocations() {
        return locations;
    }
    public double getDistance() {
        return distance;
    }
    public String getEstimatedTime() {
        return estimatedTime;
    }
}
