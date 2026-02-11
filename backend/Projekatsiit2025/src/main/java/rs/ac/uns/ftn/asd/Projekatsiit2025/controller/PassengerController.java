package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.UserRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.route.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.PassengerService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {
	private final PassengerService passengerService;
  private final RideService rideService;
	
	public PassengerController(PassengerService passengerService, RideService rideService) {
		this.passengerService = passengerService;
    this.rideService = rideService;
	}
	
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
	@GetMapping(value = "/{id}/favourite-routes", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetRouteDTO>> getFavouriteRoutes(@PathVariable("id") Long id) {
		Collection<GetRouteDTO> routes = passengerService.getFavouriteRoutes(id);
	    return new ResponseEntity<Collection<GetRouteDTO>>(routes, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
	@PostMapping(value = "/{id}/favourite-routes/{routeId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetRouteDTO> addFavouriteRoute(@PathVariable("id") Long id, @PathVariable("routeId") Long routeId) {
	    GetRouteDTO route = passengerService.addToFavouriteRoutes(id, routeId);
	    return new ResponseEntity<GetRouteDTO>(route, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
	@DeleteMapping(value = "/{id}/favourite-routes/{routeId}")
	public ResponseEntity<?> deleteFavouriteRoute(@PathVariable("id") Long id, @PathVariable("routeId") Long routeId) {
	    passengerService.removeFromFavouriteRoutes(id, routeId);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
	@GetMapping("/{id}/ride/active")
    public ResponseEntity<DriverRideHistoryDTO> getActiveRide(@PathVariable Long id) {
        return ResponseEntity.ok(rideService.getActiveRideForPassenger(id));
    }

	@GetMapping("/{id}/rides/history")
	public ResponseEntity<Collection<UserRideHistoryDTO>> getUserRideHistory(
	        @PathVariable Long id,
	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate from,

	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate to) {

		 return ResponseEntity.ok(
		            rideService.getUserRideHistory(id, from, to)
		    );
	}
	
}