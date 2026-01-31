package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.util.Collection;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.service.VehicleService;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
	
	private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }
    
    //Home page raspored kola
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetVehicleDTO>> getAvailableVehicles(){
		
		return ResponseEntity.ok(vehicleService.getAllVehicles());
	}
}
