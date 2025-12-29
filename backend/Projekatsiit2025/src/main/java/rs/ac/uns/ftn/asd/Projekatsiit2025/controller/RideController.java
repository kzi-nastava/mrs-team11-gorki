package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideCancelRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideEstimateRequestDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideStopResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.RideHistoryResponseDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.RideStatus;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    @PostMapping(value = "/estimate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RideHistoryResponseDTO> estimateRide(@RequestBody RideEstimateRequestDTO request) {

        List<Location> locations = List.of(
                new Location(45.2671, 19.8335, request.getStartingAddress()),
                new Location(45.2550, 19.8450, request.getEndingAddress())
        );
        Route route = new Route(locations, 5.0, LocalDateTime.now().plusMinutes(15));

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
        Route route = new Route(locations, 5.0, LocalDateTime.now().plusMinutes(15));

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
}
