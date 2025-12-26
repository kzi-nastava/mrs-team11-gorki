package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.util.List;

public class Passenger extends User {
	private List<Route> favouriteRoutes;

	public Passenger() {
		super();
	}
	public Passenger(List<Route> favouriteRoutes) {
		super();
		this.favouriteRoutes = favouriteRoutes;
	}
	public List<Route> getFavouriteRoutes() {
		return favouriteRoutes;
	}
	public void setFavouriteRoutes(List<Route> favouriteRoutes) {
		this.favouriteRoutes = favouriteRoutes;
	}
		
}
