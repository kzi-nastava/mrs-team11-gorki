package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;


import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating.CreateRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.rating.CreatedRatingDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RatingService;

@RestController
@RequestMapping("/api/rides")
public class RatingController {
	
	private final RatingService ratingService;
	
	public RatingController(RatingService ratingService) {
		this.ratingService=ratingService;
	}
	
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
	@PostMapping(
	        value = "/{id}/rating",
	        consumes = MediaType.APPLICATION_JSON_VALUE,
	        produces = MediaType.APPLICATION_JSON_VALUE
	    )
	    public ResponseEntity<CreatedRatingDTO> rateRide(
	            @PathVariable Long id,
	            @Valid @RequestBody CreateRatingDTO dto) {

			CreatedRatingDTO response=ratingService.createRating(id, dto);
	        return new ResponseEntity<>(response, HttpStatus.CREATED);
	    }
	@PreAuthorize("hasAuthority('ROLE_PASSENGER')")
    @GetMapping("/ratings/pending-latest")
    public ResponseEntity<Long> pendingLatest(Principal principal) {
		System.out.println("HIT pending-latest by " + (principal != null ? principal.getName() : "NULL"));
	    Long rideId = ratingService.pendingLatestRideId(principal.getName());
	    System.out.println("RETURN rideId=" + rideId);
	    return ResponseEntity.ok(rideId);
    }
}
