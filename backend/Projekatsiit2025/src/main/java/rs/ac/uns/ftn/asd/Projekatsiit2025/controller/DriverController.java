package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreateDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.CreatedDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetDriverDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdateVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.UpdatedVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.DriverService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.RideService;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.VehicleService;
import java.time.LocalDate;
import java.util.Collection;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.DriverRideHistoryDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.FinishedRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.PassengerInRideDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Route;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {
	
	//RideService
	private final DriverService driverService;
    private final RideService rideService;
    private final VehicleService vehicleService;
    public DriverController(RideService rideService, DriverService driverService, VehicleService vehicleService) {
        this.rideService = rideService;
        this.driverService = driverService;
        this.vehicleService = vehicleService;
    }
    
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CreatedDriverDTO> createDriver(@RequestBody CreateDriverDTO requestDriver){
		CreatedDriverDTO responseDriver = driverService.createDriver(requestDriver);
		return new ResponseEntity<CreatedDriverDTO>(responseDriver, HttpStatus.CREATED);
	}
	
	@GetMapping(value = "/{id}/vehicle/{vehicleId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetVehicleDTO> getVehicle(@PathVariable("vehicleId") Long vehicleID){
		GetVehicleDTO vehicle = vehicleService.getById(vehicleID);
		if (vehicle == null) {
			return new ResponseEntity<GetVehicleDTO>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<GetVehicleDTO>(vehicle, HttpStatus.OK);
	}
	
	@PutMapping(value = "/{id}/vehicle/{vehicleId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<UpdatedVehicleDTO> updateVehicle(@PathVariable("vehicleId") Long vehicleID, @RequestBody UpdateVehicleDTO requestVehicle){
		UpdatedVehicleDTO responseVehicle = vehicleService.updateVehicle(vehicleID, requestVehicle);
		return new ResponseEntity<UpdatedVehicleDTO>(responseVehicle, HttpStatus.OK); 
	}
	
	@GetMapping(value = "/{id}/activity", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<GetDriverDTO> getDriverActivity(@PathVariable("id") Long id){
		GetDriverDTO driver = driverService.getActivity(id);
		return new ResponseEntity<GetDriverDTO>(driver, HttpStatus.OK);
	}
	
	@PreAuthorize("hasAuthority('ROLE_DRIVER')")
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

	@PutMapping("/{driverId}/rides/finish")
    public ResponseEntity<FinishedRideDTO> finishRide(
            @PathVariable Long driverId,
            @RequestBody FinishRideDTO dto) {

        FinishedRideDTO response = rideService.finishRide(driverId, dto);
        return ResponseEntity.ok(response);
    }
	

}
