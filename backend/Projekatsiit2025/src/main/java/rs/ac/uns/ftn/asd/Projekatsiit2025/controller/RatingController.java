package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedRatingDTO;

@RestController
@RequestMapping("/api/rides")
public class RatingController {
	
	@PostMapping(
	        value = "/{id}/rating",
	        consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE
	    )
	    public ResponseEntity<CreatedRatingDTO> rateRide(
	            @PathVariable Long id,
	            @RequestBody CreateRatingDTO dto) {

	        CreatedRatingDTO response = new CreatedRatingDTO();
	        response.setRatingId(1L);
	        response.setRideId(id);
	        response.setDriverRating(4.5);
	        response.setVehicleRating(4.0);
	        response.setCreatdAt(LocalDateTime.now());

	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }
}
