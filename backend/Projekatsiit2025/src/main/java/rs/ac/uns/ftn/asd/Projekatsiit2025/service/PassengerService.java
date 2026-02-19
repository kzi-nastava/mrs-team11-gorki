package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.location.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.exception.RouteAlreadyAddedException;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RideRepository;

@Service
public class PassengerService {
	private final PassengerRepository passengerRepository;
	private final RideRepository rideRepository;
	
	public PassengerService(PassengerRepository passengerRepository, RideRepository rideRepository) {
		this.passengerRepository = passengerRepository;
		this.rideRepository = rideRepository;
	}
	
	@Transactional(readOnly = true)
	public Collection<GetRouteDTO> getFavouriteRoutes(Long id){
		Collection<GetRouteDTO> dto = new ArrayList<GetRouteDTO>();
		Passenger passenger = passengerRepository.findById(id).get();
		for(Route route : passenger.getFavouriteRoutes()) {
			dto.add(mapToGetRouteDTO(route));
		}
		return dto;
	}
	
	@Transactional
	public GetRouteDTO addToFavouriteRoutes(Long id, Long rideId) {
		Passenger passenger = passengerRepository.findById(id).get();
		Route route = rideRepository.findById(rideId).get().getRoute();
		boolean exists = false;
		for(Route favouriteRoute : passenger.getFavouriteRoutes()) {
			if(route.getId().equals(favouriteRoute.getId())) {
				exists = true;
				break;
			}
		}
		if(!exists) {
			passenger.getFavouriteRoutes().add(route);
			passengerRepository.save(passenger);
			return mapToGetRouteDTO(route);
		}
		throw new RouteAlreadyAddedException("Route already added to favourites");
	}
	
	@Transactional
	public void removeFromFavouriteRoutes(Long passengerId, Long routeId) {
	    Passenger passenger = passengerRepository.findById(passengerId).get();
	    passenger.getFavouriteRoutes().removeIf(route -> route.getId().equals(routeId));
	    passengerRepository.save(passenger);
	}
	
	private GetRouteDTO mapToGetRouteDTO(Route route) {
		GetRouteDTO dto = new GetRouteDTO();
		dto.setId(route.getId());
		List<LocationDTO> locations = new ArrayList<LocationDTO>();
		for(Location location : route.getLocations()) {
			locations.add(mapToLocationDTO(location));
		}
		dto.setLocations(locations);
		dto.setDistance(route.getDistance());
		dto.setEstimatedTime(route.getEstimatedTime());
		return dto;
	}
	
	private LocationDTO mapToLocationDTO(Location location) {
		LocationDTO dto = new LocationDTO();
		dto.setAddress(location.getAddress());
		dto.setLatitude(location.getLatitude());
		dto.setLongitude(location.getLongitude());
		return dto;
	}
}
