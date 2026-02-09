package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Passenger;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.PassengerRepository;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.RouteRepository;

@Service
public class PassengerService {
	private final PassengerRepository passengerRepository;
	private final RouteRepository routeRepository;
	
	public PassengerService(PassengerRepository passengerRepository, RouteRepository routeRepository) {
		this.passengerRepository = passengerRepository;
		this.routeRepository = routeRepository;
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
	public GetRouteDTO addToFavouriteRoutes(Long id, Long routeId) {
		Passenger passenger = passengerRepository.findById(id).get();
		Route route = routeRepository.findById(routeId).get();
		boolean exists = false;
		for(Route favouriteRoute : passenger.getFavouriteRoutes()) {
			if(route.getId().equals(favouriteRoute.getId())) {
				exists = true;
				break;
			}
		}
		if(!exists) {
			passenger.getFavouriteRoutes().add(route);
		}
		return mapToGetRouteDTO(route);
	}
	
	@Transactional
	public void removeFromFavouriteRoutes(Long passengerId, Long routeId) {
	    Passenger passenger = passengerRepository.findById(passengerId).get();
	    passenger.getFavouriteRoutes().removeIf(route -> route.getId().equals(routeId));
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
