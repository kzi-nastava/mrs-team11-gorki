package rs.ac.uns.ftn.asd.Projekatsiit2025.dto.passenger;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.user.GetUserDTO;

public class GetPassengerDTO {
	@Valid
	@NotNull(message = "User is required")
	private GetUserDTO user;
	@Valid
	@NotNull(message = "Favourite routes are required")
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
