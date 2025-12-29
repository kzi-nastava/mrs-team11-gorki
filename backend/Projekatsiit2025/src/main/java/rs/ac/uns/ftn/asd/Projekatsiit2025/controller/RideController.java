package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedInconsistencyReportDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetRideTrackingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.DriverStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

@RestController
@RequestMapping("/api/rides")
public class RideController {
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
		    value = "/{id}/inconsistencies",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<CreatedInconsistencyReportDTO> reportInconsistency(
		        @PathVariable Long id,
		        @RequestBody CreateInconsistencyReportDTO dto) {

		    CreatedInconsistencyReportDTO response = new CreatedInconsistencyReportDTO();
		    response.setInconsistencyReportId(1L);
		    response.setDescription(dto.getDescription());

		    return new ResponseEntity<>(response, HttpStatus.CREATED);
		}
}
