package rs.ac.uns.ftn.asd.Projekatsiit2025.dto;

import java.util.List;

public class GetPassengerDTO {
	private GetUserDTO user;
	private List<GetRouteDTO> favouriteRoutes;
	public GetPassengerDTO() {
		super();
	}
	public GetPassengerDTO(GetUserDTO user, List<GetRouteDTO> favouriteRoutes) {
		super();
		this.user = user;
		this.favouriteRoutes = favouriteRoutes;
	}
	public GetUserDTO getUser() {
		return user;
	}
	public void setUser(GetUserDTO user) {
		this.user = user;
	}
	public List<GetRouteDTO> getFavouriteRoutes() {
		return favouriteRoutes;
	}
	public void setFavouriteRoutes(List<GetRouteDTO> favouriteRoutes) {
		this.favouriteRoutes = favouriteRoutes;
	}
	
}
