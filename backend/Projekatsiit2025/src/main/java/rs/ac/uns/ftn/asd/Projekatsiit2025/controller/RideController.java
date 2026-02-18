package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.InconsistencyReportService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import org.springframework.web.bind.annotation.GetMapping;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.inconsistencyReport.CreateInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.inconsistencyReport.CreatedInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.CreateRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.CreatedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.GetRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideCancelRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideCancelResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideEstimateRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideHistoryResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideStopRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideStopResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.ScheduledRideDTO;

@RestController
@RequestMapping("/api/rides")
public class RideController {
	private final RideService rideService;
	private final InconsistencyReportService inconsistencyReportService;
	
	public RideController(InconsistencyReportService inconsistencyReportService,RideService rideService) {
	    this.inconsistencyReportService = inconsistencyReportService;
	    this.rideService = rideService;
	}
	
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedRideDTO> createRide(@RequestBody CreateRideDTO requestRide){
		CreatedRideDTO responseRide = rideService.createRide(requestRide);
		return new ResponseEntity<CreatedRideDTO>(responseRide, HttpStatus.CREATED);
	}
	
	@PreAuthorize("hasAuthority('ROLE_DRIVER')")
	@GetMapping(value = "/{id}/next-ride", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetRideDTO> getNextScheduledRide(@PathVariable("id") long id){
		GetRideDTO dto = rideService.getNextScheduledRide(id);
		if(dto == null) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<GetRideDTO>(dto, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_DRIVER')")
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

  	@PreAuthorize("hasAnyAuthority('ROLE_PASSENGER', 'ROLE_DRIVER')")
    @PostMapping(value = "/{id}/cancel", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideCancelResponseDTO> cancelRide(
            @PathVariable Long id,
            @RequestBody RideCancelRequestDTO request) {

        RideCancelResponseDTO response = rideService.cancelRide(id, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
  
    //Ovo treba PutMapping, i promena na frontu
   	@PreAuthorize("hasAuthority('ROLE_DRIVER')")
    @PostMapping(
    value = "/{id}/stop",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RideStopResponseDTO> stopRide(
            @PathVariable Long id,
            @RequestBody RideStopRequestDTO req) {

        Location stopLocation = new Location(req.getLatitude(), req.getLongitude(), req.getAddress());
        RideStopResponseDTO dto = rideService.stopRide(id, stopLocation);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
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

    @PutMapping(value = "/{id}/panic", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> panic(@PathVariable Long id) {
        rideService.activatePanic(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/scheduled-rides")
	public ResponseEntity<Collection<ScheduledRideDTO>> getScheduledRide(
	        @PathVariable Long userId,
	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate from,

	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate to) {

		 return ResponseEntity.ok(
		            rideService.getScheduledRide(userId, from, to)
		    );
	}
    

}