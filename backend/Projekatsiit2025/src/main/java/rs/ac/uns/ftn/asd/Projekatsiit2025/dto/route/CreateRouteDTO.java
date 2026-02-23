package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;

public class CreateRouteDTO {
	
	@NotEmpty(message = "Locations are required")
	@Size(min = 2, message = "At least start and end location required")
	@Valid
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
