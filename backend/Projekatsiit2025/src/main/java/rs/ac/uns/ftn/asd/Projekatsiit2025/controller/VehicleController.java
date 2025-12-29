package rs.ac.uns.ftn.asd.Projekatsiit2025.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Location;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.enums.VehicleType;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
	
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<GetVehicleDTO>> getAvailableVehicles(){
		Collection<GetVehicleDTO> vehicles=new ArrayList<>();
		
		GetVehicleDTO vehicle1=new GetVehicleDTO();
		vehicle1.setId(1L);
		vehicle1.setCurrentLocation(new Location(45.9942,19.4456,"Heroja Pinkija 32"));	
		vehicle1.setModel("Ford focus");
		vehicle1.setType(VehicleType.STANDARD);
		vehicle1.setBabyTransport(false);
		vehicle1.setPetFriendly(false);
		vehicle1.setPlateNumber("NS-123-NS");
		vehicle1.setSeats(4);
		
        GetVehicleDTO vehicle2 = new GetVehicleDTO();
        vehicle2.setId(2L);
		vehicle2.setCurrentLocation(new Location(44.3342,18.4456,"Bulevar Cara Lazara 17")); 
		vehicle2.setModel("Mercedes E220");
		vehicle2.setType(VehicleType.LUXURY);
		vehicle2.setBabyTransport(false);
		vehicle2.setPetFriendly(false);
		vehicle2.setPlateNumber("NS-222-NS");
		vehicle2.setSeats(4);
		
        GetVehicleDTO vehicle3 = new GetVehicleDTO();
        vehicle3.setId(3L);
		vehicle3.setCurrentLocation(new Location(44.5544,18.7856,"Bulevar Cara Lazara 50"));
		vehicle3.setModel("Yugo Coral 45");
		vehicle3.setType(VehicleType.STANDARD);
		vehicle3.setBabyTransport(false);
		vehicle3.setPetFriendly(false);
		vehicle3.setPlateNumber("NS-569-NS");
		vehicle3.setSeats(4);
		
        vehicles.add(vehicle1);
        vehicles.add(vehicle2);
        vehicles.add(vehicle3);
        
        return new ResponseEntity<>(vehicles,HttpStatus.OK);
	}
}
