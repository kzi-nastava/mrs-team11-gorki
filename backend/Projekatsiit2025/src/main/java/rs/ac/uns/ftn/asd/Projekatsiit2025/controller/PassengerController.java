package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {
	
    private final RideService rideService;

    public PassengerController(RideService rideService) {
        this.rideService = rideService;
    }
	
	@GetMapping(value = "/{id}/favourite-routes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetRouteDTO>> getFavouriteRoutes(@PathVariable("id") Long id) {
		Collection<GetRouteDTO> routes = new ArrayList<GetRouteDTO>();
		
		GetRouteDTO route1 = new GetRouteDTO();
		route1.setDistance(7);
	    route1.setEstimatedTime(LocalDateTime.now().plusMinutes(15));
	    route1.setId(1L);
	    LocationDTO loc1 = new LocationDTO(45.2540, 19.8450, "Petrovaradinska tvrđava, Novi Sad");
	    LocationDTO loc2 = new LocationDTO(45.2421, 19.8223, "SPENS, Novi Sad");
	    LocationDTO loc3 = new LocationDTO(45.2671, 19.8335, "Dunavski park, Novi Sad");
	    List<LocationDTO> locations = new ArrayList<LocationDTO>();
	    locations.add(loc1);
	    locations.add(loc2);
	    locations.add(loc3);
	    route1.setLocations(locations);
	    
		GetRouteDTO route2 = new GetRouteDTO();
		route2.setDistance(7);
		route2.setEstimatedTime(LocalDateTime.now().plusMinutes(15));
		route2.setId(2L);
	    route2.setLocations(locations);
	    
		GetRouteDTO route3 = new GetRouteDTO();
		route3.setDistance(7);
		route3.setEstimatedTime(LocalDateTime.now().plusMinutes(15));
		route3.setId(3L);
	    route3.setLocations(locations);
	    
	    routes.add(route1);
	    routes.add(route2);
	    routes.add(route3);
		
	    return new ResponseEntity<Collection<GetRouteDTO>>(routes, HttpStatus.OK);
	}
	
	@PostMapping(value = "/{id}/favourite-routes/{routeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetRouteDTO> addFavouriteRoute(@PathVariable("id") Long id, @PathVariable("routeId") Long routeId) {
	    GetRouteDTO route = new GetRouteDTO();
	    
	    route.setDistance(7);
	    route.setEstimatedTime(LocalDateTime.of(0, 0, 0, 0, 15, 0));
	    route.setId(1L);
	    LocationDTO loc1 = new LocationDTO(45.2540, 19.8450, "Petrovaradinska tvrđava, Novi Sad");
	    LocationDTO loc2 = new LocationDTO(45.2421, 19.8223, "SPENS, Novi Sad");
	    LocationDTO loc3 = new LocationDTO(45.2671, 19.8335, "Dunavski park, Novi Sad");
	    List<LocationDTO> locations = new ArrayList<LocationDTO>();
	    locations.add(loc1);
	    locations.add(loc2);
	    locations.add(loc3);
	    route.setLocations(locations);
	    
	    return new ResponseEntity<GetRouteDTO>(route, HttpStatus.CREATED);
	}
	
	@DeleteMapping(value = "/{id}/favourite-routes/{routeId}")
	public ResponseEntity<?> deleteFavouriteRoute(@PathVariable("id") Long id, @PathVariable("routeId") Long routeId) {
	    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@GetMapping("/{id}/ride/active")
    public ResponseEntity<DriverRideHistoryDTO> getActiveRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getActiveRideForPassenger(id));
    }
	
}
