package rs.ac.uns.ftn.asd.Projekatsiit2025.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;

@Entity
public class Passenger extends User {
	@ManyToMany
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
