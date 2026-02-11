package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.RideHistoryResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.ride.UserRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    
    private final RideService rideService;

    public AdminController(RideService rideService) {
        this.rideService = rideService;
    }

	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping(value = "/users/{id}/rides", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RideHistoryResponseDTO>> getRideHistory(
            @PathVariable Long id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to,
            @RequestParam(defaultValue = "startingTime") String sortBy) {

        List<RideHistoryResponseDTO> rideHistory = new ArrayList<>();

        List<Location> locations = new ArrayList<>();
        locations.add(new Location(45.2671, 19.8335, "Starting Point"));
        locations.add(new Location(45.2550, 19.8450, "Ending Point"));

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

        rideHistory.add(ride);

        return new ResponseEntity<List<RideHistoryResponseDTO>>(rideHistory, HttpStatus.OK);
    }

    @GetMapping("/{userId}/rides/history")
    public ResponseEntity<Collection<UserRideHistoryDTO>> getAdminRideHistory(
            @PathVariable Long userId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to) {

         return ResponseEntity.ok(
                    rideService.getAdminRideHistory(userId, from, to)
            );
    }
}
