package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.util.List;

public class CreateRouteDTO {
	private List<LocationDTO> locations;

	public CreateRouteDTO() {
		super();
	}

	public CreateRouteDTO(List<LocationDTO> locations) {
		super();
		this.locations = locations;
	}

	public List<LocationDTO> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationDTO> locations) {
		this.locations = locations;
	}
	
}
