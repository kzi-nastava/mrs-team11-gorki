package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetPassengerDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRouteDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.PriceConfig;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.InconsistencyReportService;

import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideCancelRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideEstimateRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideStopResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideHistoryResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import org.springframework.web.bind.annotation.GetMapping;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRideTrackingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

@RestController
@RequestMapping("/api/rides")
public class RideController {
	
	private final InconsistencyReportService inconsistencyReportService;
	public RideController(InconsistencyReportService inconsistencyReportService)
	{
		this.inconsistencyReportService=inconsistencyReportService;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedRideDTO> createRide(@RequestBody CreateRideDTO requestRide){
		CreatedRideDTO responseRide = new CreatedRideDTO();
		
		ArrayList<GetRouteDTO> routes = new ArrayList<GetRouteDTO>();
		
		GetRouteDTO route1 = new GetRouteDTO();
		route1.setDistance(7);
	    route1.setEstimatedTime(LocalDateTime.of(0, 0, 0, 0, 15, 0));
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
		route2.setEstimatedTime(LocalDateTime.of(0, 0, 0, 0, 15, 0));
		route2.setId(2L);
	    route2.setLocations(locations);
	    
		GetRouteDTO route3 = new GetRouteDTO();
		route3.setDistance(7);
		route3.setEstimatedTime(LocalDateTime.of(0, 0, 0, 0, 15, 0));
		route3.setId(3L);
	    route3.setLocations(locations);
	    
	    routes.add(route1);
	    routes.add(route2);
	    routes.add(route3);
	    
	    GetUserDTO user = new GetUserDTO();
		
		user.setActive(false);
		user.setAddress("Mornarska 51");
		user.setEmail("marko.pavlovic2404004@gmail.com");
		user.setFirstName("Marko");
		user.setId(2L);
		user.setLastName("Pavlovic");
		user.setPhoneNumber(648816145);
		user.setProfileImage("Putanja do slike");
		user.setRole(UserRole.PASSENGER);
		
		GetUserDTO user1 = new GetUserDTO();
		
		user1.setActive(false);
		user1.setAddress("Mornarska 51");
		user1.setEmail("marko.pavlovic2404004@gmail.com");
		user1.setFirstName("Marko");
		user1.setId(1L);
		user1.setLastName("Pavlovic");
		user1.setPhoneNumber(648816145);
		user1.setProfileImage("Putanja do slike");
		user1.setRole(UserRole.DRIVER);
		
		responseRide.setCancellationReason("");
		responseRide.setCreator(new GetPassengerDTO(user, routes));
		responseRide.setDriver(new GetDriverDTO(user1, 2));
		responseRide.setEndingTime(LocalDateTime.now().plusMinutes(15));
		responseRide.setId(1L);
		responseRide.setLinkedPassengers(null);
		responseRide.setPanicActivated(false);
		responseRide.setPrice(1000);
		responseRide.setScheduledTime(LocalDateTime.now());
		responseRide.setStartingTime(LocalDateTime.now());
		responseRide.setStatus(RideStatus.STARTED);
		responseRide.setPriceConfig(new PriceConfig(100, 200));
		
		return new ResponseEntity<CreatedRideDTO>(responseRide, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}/start", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetRideDTO> startRide(@PathVariable("id") Long id){
		GetRideDTO ride = new GetRideDTO();
		ArrayList<GetRouteDTO> routes = new ArrayList<GetRouteDTO>();
		
		GetRouteDTO route1 = new GetRouteDTO();
		route1.setDistance(7);
	    route1.setEstimatedTime(LocalDateTime.of(0, 0, 0, 0, 15, 0));
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
		route2.setEstimatedTime(LocalDateTime.of(0, 0, 0, 0, 15, 0));
		route2.setId(2L);
	    route2.setLocations(locations);
	    
		GetRouteDTO route3 = new GetRouteDTO();
		route3.setDistance(7);
		route3.setEstimatedTime(LocalDateTime.of(0, 0, 0, 0, 15, 0));
		route3.setId(3L);
	    route3.setLocations(locations);
	    
	    routes.add(route1);
	    routes.add(route2);
	    routes.add(route3);
	    
	    GetUserDTO user = new GetUserDTO();
		
		user.setActive(false);
		user.setAddress("Mornarska 51");
		user.setEmail("marko.pavlovic2404004@gmail.com");
		user.setFirstName("Marko");
		user.setId(2L);
		user.setLastName("Pavlovic");
		user.setPhoneNumber(648816145);
		user.setProfileImage("Putanja do slike");
		user.setRole(UserRole.PASSENGER);
		
		GetUserDTO user1 = new GetUserDTO();
		
		user1.setActive(false);
		user1.setAddress("Mornarska 51");
		user1.setEmail("marko.pavlovic2404004@gmail.com");
		user1.setFirstName("Marko");
		user1.setId(1L);
		user1.setLastName("Pavlovic");
		user1.setPhoneNumber(648816145);
		user1.setProfileImage("Putanja do slike");
		user1.setRole(UserRole.DRIVER);
		
		ride.setCancellationReason("");
		ride.setCreator(new GetPassengerDTO(user, routes));
		ride.setDriver(new GetDriverDTO(user1, 2));
		ride.setEndingTime(LocalDateTime.now().plusMinutes(15));
		ride.setId(1L);
		ride.setLinkedPassengers(null);
		ride.setPanicActivated(false);
		ride.setPrice(1000);
		ride.setRoute(route1);
		ride.setScheduledTime(LocalDateTime.now());
		ride.setStartingTime(LocalDateTime.now());
		ride.setStatus(RideStatus.STARTED);
		
		return new ResponseEntity<GetRideDTO>(ride, HttpStatus.OK);
	}
    @PostMapping(value = "/estimate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideHistoryResponseDTO> estimateRide(@RequestBody RideEstimateRequestDTO request) {

        List<Location> locations = List.of(
                new Location(45.2671, 19.8335, request.getStartingAddress()),
                new Location(45.2550, 19.8450, request.getEndingAddress())
        );
        Route route = new Route(1L,locations, 5.0, LocalDateTime.now().plusMinutes(15));

        RideHistoryResponseDTO dto = new RideHistoryResponseDTO();
        dto.setRoute(route);
        dto.setStartingAddress(request.getStartingAddress());
        dto.setEndingAddress(request.getEndingAddress());
        dto.setStartingTime(LocalDateTime.now());
        dto.setEndingTime(LocalDateTime.now().plusMinutes(15));
        dto.setPrice(500.0);
        dto.setStatus(RideStatus.REQUESTED);
        dto.setCancelledBy(null);
        dto.setPanicActivated(false);

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/cancel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideCancelRequestDTO> cancelRide(
            @PathVariable Long id,
            @RequestBody RideCancelRequestDTO request) {

        RideCancelRequestDTO response = new RideCancelRequestDTO();
        response.setCancellationReason(request.getCancellationReason());
        response.setCancelledBy(request.getCancelledBy());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/{id}/stop", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideStopResponseDTO> stopRide(@PathVariable Long id) {
        RideStopResponseDTO dto = new RideStopResponseDTO();
        dto.setStopAddress(new Location(45.2549, 19.8442, "Pozorišni trg 1"));
        dto.setPrice(1250);
        dto.setEndingTime(LocalDateTime.of(2025, 1, 10, 14, 30));

        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}/history", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RideHistoryResponseDTO>> getRideHistory(@PathVariable Long id) {

        List<Location> locations = List.of(
                new Location(45.2671, 19.8335, "Start Address"),
                new Location(45.2550, 19.8450, "End Address")
        );
        Route route = new Route(1L,locations, 5.0, LocalDateTime.now().plusMinutes(15));

        RideHistoryResponseDTO ride = new RideHistoryResponseDTO();
        ride.setRoute(route);
        ride.setStartingAddress("Starting Address");
        ride.setEndingAddress("Ending Address");
        ride.setStartingTime(LocalDateTime.now().minusHours(1));
        ride.setEndingTime(LocalDateTime.now());
        ride.setPrice(500.0);
        ride.setStatus(RideStatus.FINISHED);
        ride.setCancelledBy(null);
        ride.setPanicActivated(false);

        return new ResponseEntity<>(List.of(ride), HttpStatus.OK);
    }
	@PostMapping(
	        value = "/{id}/finish",
	        consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE
	    )
	    public ResponseEntity<FinishedRideDTO> finishRide(
	            @PathVariable Long id,
	            @RequestBody FinishRideDTO dto) {

	        FinishedRideDTO response = new FinishedRideDTO();
	        response.setRideId(id);
	        response.setRideStatus(RideStatus.FINISHED);
	        response.setDriverStatus(DriverStatus.ACTIVE);
	        response.setHasNextScheduledRide(false);

	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }
	
	@GetMapping(value = "/{id}/tracking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GetRideTrackingDTO> getRideTracking(@PathVariable Long id) {

        GetRideTrackingDTO ride = new GetRideTrackingDTO();
        ride.setRideId(id);
        ride.setCurrentLocation(new Location(45.9788,18.2354,"Maksima Gorkog 27"));
        ride.setEstimatedTimeToDestination(6.5);

        return new ResponseEntity<>(ride, HttpStatus.OK);
    }
	
	@PostMapping(
	        value = "/{rideId}/inconsistencies",
	        consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<CreatedInconsistencyReportDTO> reportInconsistency(
	        @PathVariable Long rideId,
	        @RequestBody CreateInconsistencyReportDTO dto) {

	    CreatedInconsistencyReportDTO response = inconsistencyReportService.createReport(rideId, dto);
	    return new ResponseEntity<>(response, HttpStatus.CREATED);
	}
}