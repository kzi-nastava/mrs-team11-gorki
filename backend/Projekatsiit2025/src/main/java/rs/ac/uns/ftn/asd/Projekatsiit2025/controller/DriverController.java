package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
	
	@GetMapping("/{driverId}/rides/history")
	public ResponseEntity<Collection<DriverRideHistoryDTO>> getDriverRideHistory(
	        @PathVariable Long driverId,
	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate from,

	        @RequestParam(required = false)
	        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	        LocalDate to) {

	    Collection<DriverRideHistoryDTO> rides = new ArrayList<>();

	    // ---- PUTNICI ZA PRVU VOZNJU ----
	    List<PassengerInRideDTO> passengers1 = new ArrayList<>();
	    PassengerInRideDTO p1 = new PassengerInRideDTO();
	    p1.setEmail("petar.petrovic@gmail.com");
	    p1.setFirstName("Petar");
	    p1.setLastName("Petrovic");
	    p1.setPhoneNumber("+38164859612");
	    PassengerInRideDTO p2 = new PassengerInRideDTO();
	    p2.setEmail("ana.anic@gmail.com");
	    p2.setFirstName("Ana");
	    p2.setLastName("Anic");
	    p2.setPhoneNumber("+38160456728");
	    passengers1.add(p1);
	    passengers1.add(p2);
	    List<Location> locations = new ArrayList<>();
	    locations.add(new Location(45.2671, 19.8335, "Trg slobode 1, Novi Sad"));
	    locations.add(new Location(45.2567, 19.8452, "Bulevar oslobođenja 45, Novi Sad"));
	    
	    // ---- PRVA VOZNJA ----
	    DriverRideHistoryDTO ride1 = new DriverRideHistoryDTO();
	    ride1.setRideId(1L);
	    ride1.setStartingTime(LocalDateTime.of(2025, 1, 10, 14, 30));
	    ride1.setEndingTime(LocalDateTime.of(2025, 1, 10, 15, 0));
	    ride1.setRoute(new Route(locations,3.7,LocalDateTime.of(2025, 1, 10, 14, 55)));
	    ride1.setCanceled(false);
	    ride1.setCanceledBy(null);
	    ride1.setPrice(850.00);
	    ride1.setPanicActivated(false);
	    ride1.setPassengers(passengers1);

	    rides.add(ride1);
	    return new ResponseEntity<>(rides, HttpStatus.OK);
	}
}
