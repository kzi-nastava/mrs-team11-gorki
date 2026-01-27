package rs.ac.uns.ftn.asd.Projekatsiit2025.service;

import java.util.List;

import org.springframework.stereotype.Service;

import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.GetVehicleDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.dto.LocationDTO;
import rs.ac.uns.ftn.asd.Projekatsiit2025.model.Vehicle;
import rs.ac.uns.ftn.asd.Projekatsiit2025.repository.VehicleRepository;

@Service
public class VehicleService {
	
	private final VehicleRepository vehicleRepository;
	
	public VehicleService(VehicleRepository vehicleRepository) {
		this.vehicleRepository=vehicleRepository;
	}
	
	public List<GetVehicleDTO> getAllVehicles(){
		return vehicleRepository.findAll()
				.stream()
				.map(this::mapVehicleToDTO)
				.toList();
	}
	
	private GetVehicleDTO mapVehicleToDTO(Vehicle vehicle) {
		GetVehicleDTO dto = new GetVehicleDTO();
		dto.setId(vehicle.getId());
		dto.setModel(vehicle.getModel());
		dto.setType(vehicle.getType());
		dto.setPlateNumber(vehicle.getPlateNumber());
		dto.setBabyTransport(vehicle.getBabyTransport());
		dto.setPetFriendly(vehicle.getPetFriendly());
		dto.setSeats(vehicle.getSeats());
		
		LocationDTO location=new LocationDTO();
		location.setAddress(vehicle.getCurrentLocation().getAddress());
		location.setLatitude(vehicle.getCurrentLocation().getLatitude());
		location.setLongitude(vehicle.getCurrentLocation().getLongitude());
		
		dto.setCurrentLocation(location);
		
		return dto;
	}
}
