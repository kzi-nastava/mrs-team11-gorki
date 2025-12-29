package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetUserDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdateVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdatedVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.UserRole;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedDriverDTO> createDriver(@RequestBody CreateDriverDTO requestDriver){
		CreatedDriverDTO responseDriver = new CreatedDriverDTO();
		
		responseDriver.setUser(new CreatedUserDTO(1L, "marko.pavlovic2404004@gmail.com", "Marko", "Pavlovic", 648816145, "Mornarska 51", "Putanja do slike", false, UserRole.DRIVER));
		responseDriver.setVehicle(new CreatedVehicleDTO(1L, "Ford Focus", VehicleType.STANDARD, "NS-523-SV", 5, true, false));
		
		return new ResponseEntity<CreatedDriverDTO>(responseDriver, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/{id}/vehicle/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetVehicleDTO> getVehicle(@PathVariable("vehicleId") Long vehicleID){
		GetVehicleDTO vehicle = new GetVehicleDTO();
		
		vehicle.setId(1L);
		vehicle.setModel("Ford Focus");
		vehicle.setBabyTransport(true);
		vehicle.setPetFriendly(false);
		vehicle.setPlateNumber("NS-523-SV");
		vehicle.setSeats(5);
		vehicle.setType(VehicleType.STANDARD);
		
		if (vehicle == null) {
			return new ResponseEntity<GetVehicleDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GetVehicleDTO>(vehicle, HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}/vehicle/{vehicleID}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedVehicleDTO> updateVehicle(@PathVariable("vehicleId") Long vehicleID, @RequestBody UpdateVehicleDTO requestVehicle){
		UpdatedVehicleDTO responseVehicle = new UpdatedVehicleDTO();
		
		responseVehicle.setId(1L);
		responseVehicle.setModel("Ford Focus");
		responseVehicle.setBabyTransport(true);
		responseVehicle.setPetFriendly(false);
		responseVehicle.setPlateNumber("NS-523-SV");
		responseVehicle.setSeats(5);
		responseVehicle.setType(VehicleType.STANDARD);
		
		return new ResponseEntity<UpdatedVehicleDTO>(responseVehicle, HttpStatus.OK); 
	}
	
	@GetMapping(value = "/{id}/activity", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetDriverDTO> getDriverActivity(@PathVariable("id") Long id){
		GetDriverDTO driver = new GetDriverDTO();
		
		driver.setUser(new GetUserDTO(1L, "marko.pavlovic2404004@gmail.com", "Marko", "Pavlovic", 648816145, "Mornarska 51", "Putanja do slike", false, UserRole.DRIVER));
		driver.setActivityLast24h(2);
		
		if (driver == null) {
			return new ResponseEntity<GetDriverDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GetDriverDTO>(driver, HttpStatus.OK);
	}
	
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
	    ride1.setRoute(new Route(1L,locations,3.7,LocalDateTime.of(2025, 1, 10, 14, 55)));
	    ride1.setCanceled(false);
	    ride1.setCanceledBy(null);
	    ride1.setPrice(850.00);
	    ride1.setPanicActivated(false);
	    ride1.setPassengers(passengers1);

	    rides.add(ride1);
	    return new ResponseEntity<>(rides, HttpStatus.OK);
	}
}
