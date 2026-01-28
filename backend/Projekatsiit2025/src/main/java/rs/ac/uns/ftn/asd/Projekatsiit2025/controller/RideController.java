package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDateTime;
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
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.InconsistencyReportService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;

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

@RestController
@RequestMapping("/api/rides")
public class RideController {
	private final RideService rideService;
	private final InconsistencyReportService inconsistencyReportService;
	
	public RideController(InconsistencyReportService inconsistencyReportService,RideService rideService) {
	    this.inconsistencyReportService = inconsistencyReportService;
	    this.rideService = rideService;
	}
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedRideDTO> createRide(@RequestBody CreateRideDTO requestRide){
		CreatedRideDTO responseRide = rideService.createRide(requestRide);
		return new ResponseEntity<CreatedRideDTO>(responseRide, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/{id}/start", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetRideDTO> startRide(@PathVariable("id") Long id){
		GetRideDTO ride = rideService.startRide(id);
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

        // Poziv servisa da otkaže vožnju
        RideCancelRequestDTO response = rideService.cancelRide(
                id,
                request.getCancellationReason(),
                request.getCancelledBy()
        );

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