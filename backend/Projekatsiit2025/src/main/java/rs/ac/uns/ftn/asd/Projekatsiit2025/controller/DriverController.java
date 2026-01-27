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
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
	
	//RideService
    private final RideService rideService;
    public DriverController(RideService rideService) {
        this.rideService = rideService;
    }
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

		 return ResponseEntity.ok(
		            rideService.getDriverRideHistory(driverId, from, to)
		    );
	}
}
