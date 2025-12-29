package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;

public class GetVehicleDTO {
	
	    private Long id;
	    private Location currentLocation;
	    private boolean occupied;

	    public GetVehicleDTO() {
	        super();
	    }
		public Long getId() {
			return id;
		}
		public void setId(Long id) {
			this.id = id;
		}
		public Location getCurrentLocation() {
			return currentLocation;
		}
		public void setCurrentLocation(Location currentLocation) {
			this.currentLocation = currentLocation;
		}
		public boolean isOccupied() {
			return occupied;
		}
		public void setOccupied(boolean occupied) {
			this.occupied = occupied;
		}
	   
}
